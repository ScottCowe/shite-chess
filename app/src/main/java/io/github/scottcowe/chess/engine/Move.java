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

  // For standard, irreversible, promotion
  private int fromIndex;
  private int toIndex;
  
  // For promotion
  private Piece promoteTo;

  // For castling
  // Stored as nibble - 8 for white kingside, ... , 1 for black queenside
  private int castling;

  public Move(MoveType type, Piece[] board) {
    this.type = type;
    this.board = board;
    this.fromIndex = -1;
    this.toIndex = -1;
    this.promoteTo = Piece.NONE;
    this.castling = 0;
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

  public MoveType getType() {
    return this.type;
  }

  public int getCastling() {
    return this.castling;
  }

  public int getEnPassentTargetIndex() {
    return -1;
  }

  public Piece[] applyToBoard() {
    Piece[] newBoard = this.board;

    

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
