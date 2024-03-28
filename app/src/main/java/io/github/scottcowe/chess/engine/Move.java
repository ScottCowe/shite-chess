package io.github.scottcowe.chess.engine;

public class Move {
  private int fromIndex;
  private int toIndex;
  private boolean capture;
  private boolean enPassent;
  private Piece toPromoteTo = Piece.NONE;
  private int castling = 0; // 0 if none, 1 if kingside, 2 if queenside

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

  public Piece[] applyToBoard(Piece[] board) {
    // TODO: Exception for castling

    Piece[] newBoard = board; 
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

    this.capture = newBoard[this.toIndex] != Piece.NONE;

    newBoard[fromIndex] = Piece.NONE;
    newBoard[toIndex] = from;

    return newBoard;
  }

  public boolean isCapture() {
    return this.capture;
  }

  @Override
  public String toString() {
    return fromIndex + " " + toIndex;
  }
}
