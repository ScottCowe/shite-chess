package io.github.scottcowe.chess.engine;

import java.util.List;
import java.util.ArrayList;

public class Move {
  public enum MoveType {
    STANDARD,
    IRREVERSIBLE,
    PROMOTION,
    CASTLING;
  }

  private MoveType type;
  private Position position;
  private int fromIndex;
  private int toIndex;
  private Piece promoteTo;
  private int castling;
  private boolean enPassent;

  public Move(MoveType type, Position position) {
    this.type = type;
    this.position = position;
    this.fromIndex = -1;
    this.toIndex = -1;
    this.promoteTo = Piece.NONE;
    this.castling = 0;
    this.enPassent = false;
  }

  public Move setFromIndex(int fromIndex) {
    this.fromIndex = fromIndex;
    return this;
  }
  
  public Move setToIndex(int toIndex) {
    this.toIndex = toIndex;
    return this;
  }

  public Move setPromoteTo(Piece piece) {
    this.promoteTo = piece;
    return this;
  }

  public Move setCastling(int castling) {
    this.castling = castling;
    return this;
  }

  public Move setEnPassent() {
    this.enPassent = true;
    return this;
  }

  public MoveType getType() {
    return this.type;
  }

  public Position getPosition() {
    return this.position;
  }

  public int getFromIndex() {
    return this.fromIndex;
  }

  public int getToIndex() {
    return this.toIndex;
  }

  public Piece getPromoteTo() {
    return this.promoteTo;
  }

  public int getCastling() {
    return this.castling;
  }

  public boolean isEnPassent() {
    return this.enPassent;
  }

  public int getEnPassentTargetIndex() {
    if (!this.getType().equals(MoveType.IRREVERSIBLE)) {
      return -1;
    }

    if (!this.position.getBoard()[this.fromIndex].getType().equals(Piece.Type.PAWN)) {
      return -1;
    }

    if (Math.abs(fromIndex - toIndex) != 16) {
      return -1;
    }

    int direction = (fromIndex - toIndex) / 16;

    return toIndex + 8 * direction;
  }

  public Piece[] applyToBoard() {
    Piece[] newBoard = this.position.getBoard();

    if (this.getType().equals(MoveType.STANDARD) || this.getType().equals(MoveType.IRREVERSIBLE)) {
      Piece fromPiece = this.position.getBoard()[this.fromIndex];

      newBoard[this.fromIndex] = Piece.NONE;
      newBoard[this.toIndex] = fromPiece;

      if (this.enPassent) {
        int capturedPiece = this.toIndex + (this.fromIndex > this.toIndex ? 8 : -8);
        newBoard[capturedPiece] = Piece.NONE;
      }
    }
    else if (this.getType().equals(MoveType.PROMOTION)) {
      newBoard[this.fromIndex] = Piece.NONE;
      newBoard[this.toIndex] = this.promoteTo;
    }
    else { // castling
      boolean isWhite = this.castling > 2;
      int offset = isWhite ? 0 : 56;
      boolean kingside = this.castling == 8 || this.castling == 2;

      int oldKingIndex = 4 + offset;
      int oldRookIndex = (kingside ? 7 : 0) + offset;

      int newKingIndex = (kingside ? 6 : 2) + offset;
      int newRookIndex = (kingside ? 5 : 3) + offset;

      newBoard[oldKingIndex] = Piece.NONE;
      newBoard[oldRookIndex] = Piece.NONE;

      newBoard[newKingIndex] = this.position.getBoard()[oldKingIndex];
      newBoard[newRookIndex] = this.position.getBoard()[oldRookIndex];
    }

    return newBoard;
  }

  @Override
  public String toString() {
    if (this.type == MoveType.CASTLING) {
      if (this.castling == 8 || this.castling == 2) {
        return "O-O";
      }

      return "O-O-O";
    }

    String fromAlgebraic = Position.getAlgebraicFromIndex(this.fromIndex);
    String toAlgebraic = Position.getAlgebraicFromIndex(this.toIndex);

    char fromChar = this.position.getBoard()[fromIndex].getAsChar();

    // Check if mutiple pieces could move to the square
    List<Move> allMoves = Position.getAllPseudoLegalMoves(this.position, this.position.isWhitesMove());
    allMoves = Position.removeIllegalMoves(allMoves);
    allMoves.remove(this);

    Piece[] board = this.position.getBoard();

    boolean sameRow = true;
    boolean sameCol = true;

    for (Move move : allMoves) {
      if (move.getToIndex() == this.toIndex && board[move.getFromIndex()] == board[this.fromIndex] /*&& !this.equals(move)*/) {
        if (Math.abs(this.fromIndex - move.getFromIndex()) % 8 != 0) {
          sameCol = false;
        }

        if (Math.abs(this.fromIndex - move.getFromIndex()) > 7) {
          sameRow = false;
        }
      }
    }

    String algebraic = toAlgebraic;

    if (!board[this.toIndex].equals(Piece.NONE)) { // If capture
      algebraic = "x" + algebraic;
    }

    if (!sameRow) {
      algebraic = Character.toString((int) (this.fromIndex / 8) + '1') + algebraic;
    }

    if (!sameCol) {
      algebraic = Character.toString(this.fromIndex % 8 + 'a') + algebraic;
    }

    algebraic = fromChar + algebraic;

    if (this.getType().equals(MoveType.PROMOTION)) {
      char promoteToChar = this.promoteTo.getAsChar();

      algebraic += "=" + promoteToChar;
    }

    return algebraic;
  }

  public static Move fromString(String string, Position pos) {
    boolean whitesMove = pos.isWhitesMove();

    if (string == "O-O") {
      int castling = 2 << (whitesMove ? 2 : 0);
      return new Move(MoveType.CASTLING, pos).setCastling(castling);
    }
    else if (string == "O-O-O") {
      int castling = 1 << (whitesMove ? 2 : 0);
      return new Move(MoveType.CASTLING, pos).setCastling(castling);
    }

    if (string.charAt(string.length() - 2) == '=') { // If promotion
      char promoteToChar = string.charAt(string.length() - 1);
      Piece promoteTo = Piece.getFromChar(promoteToChar);

      String toAlgebraic = string.substring(string.length() - 5, string.length() - 2);
      int toIndex = Position.getIndexFromAlgebraic(toAlgebraic); 

      if (string.length() == 4) { // No disambiguation
        return new Move(MoveType.PROMOTION, pos)
          .setFromIndex(toIndex + (whitesMove ? -8 : 8))
          .setToIndex(toIndex)
          .setPromoteTo(promoteTo);
      }

      int fromCol = string.charAt(0) - '1';
      int fromIndex = 48 + fromCol;

      return new Move(MoveType.PROMOTION, pos)
        .setFromIndex(fromIndex).setToIndex(toIndex).setPromoteTo(promoteTo);
    }

    String toAlgebraic = string.substring(string.length() - 2, string.length());
    int toIndex = Position.getIndexFromAlgebraic(toAlgebraic); 

    Piece fromPiece = Piece.getFromChar(string.charAt(0));

    boolean capture = string.contains("x");
    string.replace("x", "");

    MoveType moveType = capture ? MoveType.IRREVERSIBLE : MoveType.STANDARD;

    if (string.length() == 5) { // Will not be en passent as row is implied there, and therefore len would be 4
      String fromAlgebraic = string.substring(1, 4);
      int fromIndex = Position.getIndexFromAlgebraic(fromAlgebraic);

      return new Move(moveType, pos).setFromIndex(fromIndex).setToIndex(toIndex); 
    }

    if (pos.getBoard()[toIndex].equals(Piece.NONE) && capture) { // En passent
      int fromCol = string.charAt(0) - 'a';
      int fromIndex = fromCol + (pos.isWhitesMove() ? 4 : 3);

      return new Move(moveType, pos).setFromIndex(fromIndex).setToIndex(toIndex); 
    }

    int row = -1;
    int col = -1;

    char rowcol = string.charAt(1);

    if (rowcol > 64 && string.length() != 3) { // is col
      col = rowcol - 'a';
    }
    else if (string.length() != 3) {
      row = rowcol - '1';
    }

    List<Move> allMoves = Position.getAllPseudoLegalMoves(pos, pos.isWhitesMove());
    allMoves = Position.removeIllegalMoves(allMoves);

    for (Move move : allMoves) {
      if (!fromPiece.equals(pos.getBoard()[move.getFromIndex()])) {
        continue;
      }

      if (row == -1 && col == -1) {
        return new Move(moveType, pos).setFromIndex(move.getFromIndex()).setToIndex(toIndex);
      }
      else if (row == -1) {
        if (move.getFromIndex() % 8 != col) {
          continue;
        }

        int moveRow = (int) move.getFromIndex() / 8;
        int fromIndex = col + 8 * moveRow;

        return new Move(moveType, pos).setFromIndex(fromIndex).setToIndex(toIndex);
      }
      else {
        if ((int) move.getFromIndex() / 8 != row) {
          continue;
        }

        int moveCol = move.getFromIndex() % 8;
        int fromIndex = moveCol + 8 * row;

        return new Move(moveType, pos).setFromIndex(fromIndex).setToIndex(toIndex);
      }
    }

    return null;
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Move)) {
      return false;
    }

    Move move = (Move) object;

    if (!this.type.equals(move.getType())) {
      return false;
    }

    if (!this.position.equals(move.getPosition())) {
      //return false;
    }

    if (this.fromIndex != move.getFromIndex()) {
      return false;
    }

    if (this.toIndex != move.getToIndex()) {
      return false;
    }

    if (!this.promoteTo.equals(move.getPromoteTo())) {
      return false;
    }

    if (this.castling != move.getCastling()) {
      return false;
    }

    if (this.enPassent != move.isEnPassent()) {
      return false;
    }

    return true;
  }
}
