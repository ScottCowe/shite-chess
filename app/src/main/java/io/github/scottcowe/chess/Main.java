package io.github.scottcowe.chess;

import java.util.List;

import io.github.scottcowe.chess.engine.*;

public class Main {
  public static void main(String[] args) {
    Position pos = new Position();
    System.out.println(pos);

    List<Move> moves = pos.getPawnMoves(9, true);

    for (Move move : moves) {
      System.out.println(move);
    }
  }
}
