package io.github.scottcowe.chess.bot;

import io.github.scottcowe.chess.engine.*;

import java.util.List;

public class Bot { 
  private Position pos;

  public Bot() {

  }

  public void setPosition(Position pos, List<Move> moves) {
    this.pos = pos;
  
    for (Move move : moves) {
      this.pos = this.pos.doMove(move);
    }
  }
}
