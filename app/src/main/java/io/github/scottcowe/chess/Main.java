package io.github.scottcowe.chess;

import java.util.List;

import io.github.scottcowe.chess.engine.*;

public class Main {
  public static void main(String[] args) {
    Position pos = new Position("rnbqkbnr/pppppppp/8/8/8/4K3/PPPPPPPP/RNBQ1BNR w KQkq - 0 1");
    System.out.println(pos);

    List<Move> moves = pos.getKingMoves(20, true); 

    for (Move move : moves) {
      System.out.println(move);
    }
  }
}
