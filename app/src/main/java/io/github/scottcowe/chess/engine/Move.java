package io.github.scottcowe.chess.engine;

public class Move {
  private int from;
  private int to;

  public Move(int from, int to) {
    this.from = from;
    this.to = to;
  }

  public int getFrom() {
    return this.from;
  }

  public int getTo() {
    return this.to;
  }

  @Override
  public String toString() {
    return this.from + " " + this.to;
  }
}
