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

    boolean isWhite = castling > 2;
    boolean kingside = castling == 8 || castling == 2;

    int fromIndex = 4 + (isWhite ? 0 : 56);
    int toIndex = fromIndex + (kingside ? 2 : -2);

    this.fromIndex = fromIndex;
    this.toIndex = toIndex;

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

  public int getCastlingRightsMask() {
    boolean isWhite = this.position.getBoard()[this.fromIndex].isWhite();
    
    boolean kingmove = this.position.getBoard()[this.fromIndex].getType().equals(Piece.Type.KING);
    boolean castling = this.type.equals(MoveType.CASTLING);

    if (kingmove || castling) {
      int mask = 0b11;

      if (isWhite) {
        mask = mask << 2;
      }

      return ~mask;
    }
      
    boolean kingside = this.fromIndex % 8 != 0;
    int mask = kingside ? 0b10 : 0b01;

    boolean rookmove = this.position.getBoard()[this.fromIndex].getType().equals(Piece.Type.ROOK);
    
    if (rookmove) {
      if (isWhite) {
        mask = mask << 2;
      }

      return ~mask;
    }

    Piece enemyRook = isWhite ? Piece.BLACK_ROOK: Piece.WHITE_ROOK;
    boolean capturingRook = this.position.getBoard()[this.toIndex].equals(enemyRook);
    boolean movingToStartingRookSquare = this.toIndex == (!isWhite ? 0 : 56) 
      || this.toIndex == (!isWhite ? 7 : 63); 

    // Should not matter that not accounting for rook moving back to original square 
    // as the mask will have not effect when and-ed on rights that have already been changed
    if (capturingRook && movingToStartingRookSquare) {
      if (!isWhite) {
        mask = mask << 2;
      }

      return ~mask;
    }

    return 0b1111;
  }

  public Piece[] applyToBoard() {
    Piece[] newBoard = this.position.getBoard().clone();

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
    String fromAlgebraic = Position.getAlgebraicFromIndex(this.fromIndex);
    String toAlgebraic = Position.getAlgebraicFromIndex(this.toIndex);

    String algebraic = fromAlgebraic + toAlgebraic;

    if (this.moveType.equals(MoveType.PROMOTION)) {
      char promoteToChar = this.promoteTo.getAsChar(); 
      algebraic += Character.toUpperCase(promoteToChar);
    }

    return algebraic;
  }

  // No validation is done - assumes movestring is correct and legal
  public static Move fromString(String string, Position pos) {
    String fromAlgebraic = string.substring(0, 2); 
    String toAlgebraic = string.substring(2, 4); 

    if (string.length() == 5) { // if promotion
      char promoteToChar = string[4];

      if (!pos.isWhitesMove()) {
        promoteToChar = Character.toLowerCase(promoteToChar);
      }

      Piece promoteTo = Piece.getFromChar(promoteToChar);

      Move move = new Move(MoveType.PROMOTION, pos)
        .setFromIndex(Position.getIndexFromAlgebraic(fromAlgebraic))
        .setToIndex(Position.getIndexFromAlgebraic(toAlgebraic))
        .setPromoteTo(promoteTo); 

      return move;
    }

    int fromIndex = Position.getIndexFromAlgebraic(fromAlgebraic);
    int toIndex = Position.getIndexFromAlgebraic(toIndex);

    int diff = Math.abs(fromIndex - toIndex);
    boolean kingmove = pos.getPieceAtIndex(fromIndex).getType().equals(Piece.PieceType.KING);

    if (kingmove && diff != 1 && diff != 8 && diff != 9) {
      int castling = 0;

      if (fromIndex - toIndex == 2) {
        castling = 0b01;
      }
      else {
        castling = 0b10;
      }

      if (fromIndex < 8) {
        castling = castling << 2;
      }

      Move move = new Move(MoveType.CASTLING, pos)
        .setCastling(castling);

      return move;
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
      return false;
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
