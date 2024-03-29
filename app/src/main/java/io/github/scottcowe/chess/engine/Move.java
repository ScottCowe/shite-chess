package io.github.scottcowe.chess.engine;

public class Move {
  public enum MoveType {
    STANDARD,
    IRREVERSIBLE,
    PROMOTION,
    CASTLING;
  }

  private MoveType type;
  private Piece[] board;
  private int fromIndex;
  private int toIndex;
  private Piece promoteTo;
  private int castling;
  private boolean enPassent;

  public Move(MoveType type, Piece[] board) {
    this.type = type;
    this.board = board;
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

  public int getCastling() {
    return this.castling;
  }

  public int getEnPassentTargetIndex() {
    if (!this.getType().equals(MoveType.IRREVERSIBLE)) {
      return -1;
    }

    if (!this.board[this.fromIndex].getType().equals(Piece.Type.PAWN)) {
      return -1;
    }

    if (Math.abs(fromIndex - toIndex) != 16) {
      return -1;
    }

    int direction = (fromIndex - toIndex) / 16;

    return toIndex + 8 * direction;
  }

  public Piece[] applyToBoard() {
    Piece[] newBoard = this.board;

    if (this.getType().equals(MoveType.STANDARD) || this.getType().equals(MoveType.IRREVERSIBLE)) {
      Piece fromPiece = this.board[this.fromIndex];

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

      newBoard[newKingIndex] = this.board[oldKingIndex];
      newBoard[newRookIndex] = this.board[oldRookIndex];
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

    char fromChar = this.board[fromIndex].getAsChar();

    // Check if mutiple pieces could move to the square


    return "";
  }

  public static Move fromString(String string) {
    return null;
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Move)) {
      return false;
    }

    Move move = (Move) object;

    if (this.toString() != move.toString()) {
      return false;
    }

    return true;
  }
}
