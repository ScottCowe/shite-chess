package io.github.scottcowe.chess;

import java.util.List;

import io.github.scottcowe.chess.engine.*;

public class Main {
  public static void main(String[] args) {
    Position pos = new Position();
    System.out.println(pos);

    pos = pos.doMove(new Move(12, 28));
    System.out.println(pos);

    pos = pos.doMove(new Move(50, 34));
    System.out.println(pos);
  }
}
