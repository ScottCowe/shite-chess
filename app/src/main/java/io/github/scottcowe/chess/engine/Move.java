package io.github.scottcowe.chess.engine;

public class Move {
  private int fromIndex;
  private int toIndex;
  private boolean capture;

  public Move(fromIndex, toIndex) {
    this.fromIndex = fromIndex;
    this.toIndex = toIndex;
  }

  public int[] applyToBoard(int[] board) {
    int[] newBoard = board; 

    int from = newBoard[this.fromIndex];

    this.capture = newBoard[this.toIndex] != 0;

    newBoard[fromIndex] = 0;
    newBoard[toIndex] = from;

    return newBoard;
  }

  public boolean isCapture() {
    return this.capture;
  }
}
