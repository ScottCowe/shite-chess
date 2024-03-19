package io.github.scottcowe.chess.engine;

public class Move {
  private int from;
  private int to;
  private int castling;
  private boolean enPassent;

  // Regular move
  public Move(int from, int to) {
    this.from = from;
    this.to = to;
    this.castling = 0;
    this.enPassent = false;
  }

  // castling KQkq - can only be 1, 2, 4, 8
  public Move(int castling) {
    this.from = -1;
    this.to = -1;
    this.castling = castling;
    this.enPassent = false;
  }

  // en passent
  public Move(int from, int to, boolean enPassent) {
    this.from = from;
    this.to = to;
    this.castling = 0;
    this.enPassent = enPassent;
  }

  public int getFrom() {
    return this.from;
  }

  public int getTo() {
    return this.to;
  }

  public int getCastling() {
    return this.castling;
  }

  public boolean isEnPassent() {
    return this.enPassent;
  }

  @Override
  public String toString() {
    return this.from + " " + this.to;
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Move)) {
      return false;
    }

    Move move = (Move) object;

    if (this.from != move.getFrom()) {
      return false;
    } 
    else if (this.to != move.getTo()) {
      return false;
    }
    else if (this.castling != move.getCastling()) {
      return false;
    }
    else if (this.enPassent != move.isEnPassent()) {
      return false;
    }

    return true;
  }
}
