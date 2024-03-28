package io.github.scottcowe.chess.engine;

public class Move {
  private int fromIndex;
  private int toIndex;
  private boolean capture;

  public Move(int fromIndex, int toIndex) {
    this.fromIndex = fromIndex;
    this.toIndex = toIndex;
  }

  public Piece[] applyToBoard(Piece[] board) {
    Piece[] newBoard = board; 

    Piece from = newBoard[this.fromIndex];

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
