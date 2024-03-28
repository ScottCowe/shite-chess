package io.github.scottcowe.chess.engine;

// TODO: Clean up this class, figure out way to reimplement so it is nicer
public class Move {
  private int fromIndex;
  private int toIndex;
  private boolean capture;
  private boolean enPassent;
  private Piece toPromoteTo = Piece.NONE;
  private int castling = 0; // 0 if none, 1 if black queenside, 2 if black kingside, 4 if white queenside, 8 if white kingside
  private int enPassentTargetIndex = -1;
  private boolean halfmoveReset = false;

  public Move(int fromIndex, int toIndex) {
    this.fromIndex = fromIndex;
    this.toIndex = toIndex;
  }

  public Move(int fromIndex, int toIndex, boolean enPassent) {
    this(fromIndex, toIndex);
    this.enPassent = enPassent;
  }

  public Move(int fromIndex, int toIndex, Piece toPromoteTo) {
    this(fromIndex, toIndex);
    this.toPromoteTo = toPromoteTo;
  }

  public Move(int castling) {
    this.castling = castling; 
  }

  // For testing
  public int getToIndex() {
    return this.toIndex;
  }

  public Piece[] applyToBoard(Piece[] board) {
    Piece[] newBoard = board; 

    if (this.castling != 0) {
      boolean isWhite = this.castling > 2;
      boolean kingside = this.castling == 2 || this.castling == 8;

      int kingIndex = 4 + (isWhite ? 0 : 56);
      int rookIndex = (kingside ? 7 : 0) + (isWhite ? 0 : 56);

      int newKingIndex = (kingside ? 6 : 2) + (isWhite ? 0 : 56);
      int newRookIndex = (kingside ? 5 : 3) + (isWhite ? 0 : 56);

      newBoard[kingIndex] = Piece.NONE;
      newBoard[rookIndex] = Piece.NONE;

      newBoard[newKingIndex] = isWhite ? Piece.WHITE_KING : Piece.BLACK_KING;
      newBoard[newRookIndex] = isWhite ? Piece.WHITE_ROOK : Piece.BLACK_ROOK;

      return newBoard;
    }

    Piece from = newBoard[this.fromIndex];

    if (this.enPassent) {
      int toCaptureIndex = -1;

      if (this.fromIndex > this.toIndex) {
        toCaptureIndex = this.toIndex - 8;
      }
      else {
        toCaptureIndex = this.toIndex + 8;
      }

      this.capture = true;

      newBoard[fromIndex] = Piece.NONE;
      newBoard[toIndex] = from;
      newBoard[toCaptureIndex] = Piece.NONE;

      return newBoard;
    }

    if (!this.toPromoteTo.equals(Piece.NONE)) {
      this.capture = newBoard[this.toIndex] != Piece.NONE;

      newBoard[fromIndex] = Piece.NONE;
      newBoard[toIndex] = this.toPromoteTo;

      return newBoard;
    }

    if (from.getType().equals(Piece.Type.PAWN) && Math.abs(this.fromIndex - this.toIndex) == 16) {
      int offset = from.equals(Piece.WHITE_PAWN) ? 8 : -8;
      this.enPassentTargetIndex = this.fromIndex + offset;
    }

    this.capture = newBoard[this.toIndex] != Piece.NONE;

    if (from.getType().equals(Piece.Type.PAWN) || this.capture) {
      this.halfmoveReset = true;
    }

    newBoard[fromIndex] = Piece.NONE;
    newBoard[toIndex] = from;

    return newBoard;
  }

  public boolean isCapture() {
    return this.capture;
  }

  public int getCastling() {
    return this.castling;
  }

  public int getEnPassentTargetIndex() {
    return this.enPassentTargetIndex;
  }

  public boolean shouldHalfmoveReset() {
    return this.halfmoveReset;
  }

  @Override
  public String toString() {
    if (this.castling == 8 || this.castling == 2) {
      return "O-O"; 
    }
    else if (this.castling == 4 || this.castling == 1) {
      return "O-O-O";
    }

    String fromAlgebraic = Position.getAlgebraicFromIndex(this.fromIndex);
    String toAlgebraic = Position.getAlgebraicFromIndex(this.toIndex);
    return fromAlgebraic + " " + toAlgebraic;
  }
}
