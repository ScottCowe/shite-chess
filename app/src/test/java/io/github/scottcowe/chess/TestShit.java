package io.github.scottcowe.chess;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;

import io.github.scottcowe.chess.engine.*;

public class TestShit {
  @Test
  void perftDefaultPosition() {
    Position pos = new Position();

    int depth1 = this.perft(pos, 1);
    int depth2 = this.perft(pos, 2);
    int depth3 = this.perft(pos, 3);

    // Values from https://www.chessprogramming.org/Perft_Results
    assertEquals(depth1, 20);
    assertEquals(depth2, 400);
    assertEquals(depth3, 8902);
    //assertEquals(this.perft(pos, 4), 197281);
    //assertEquals(this.perft(pos, 5), 4865609);
  }

  @Test
  @Disabled
  void divideTestPos() {
    Position pos = new Position("rnbqkbnr/1ppppppp/8/p7/8/7P/PPPPPPP1/RNBQKBNR w KQkq - 0 1");
    int depth = 1;

    int result = 0;

    HashMap<String, Integer> results = divide(pos, depth);

    for (String s : results.keySet()) {
      result += results.get(s);
      System.out.println(s + ": " + results.get(s));
    }

    assertEquals(1 + 1, 3);
  }

  private static int perft(Position pos, int depth) {
    int nodes = 0;

    List<Move> moves = Position.getAllPseudoLegalMoves(pos); 
    moves = Position.removeIllegalMoves(moves, pos);

    if (depth == 1) {
      return moves.size();
    }

    for (int i = 0; i < moves.size(); i++) {
      Move move = moves.get(i);
      Position newPos = pos.doMove(move);

      nodes += perft(newPos, depth - 1);
    }

    return nodes;
  }

  private static HashMap<String, Integer> divide(Position pos, int depth) {
    HashMap<String, Integer> results = new HashMap<String, Integer>(); 

    List<Move> moves = Position.getAllPseudoLegalMoves(pos); 
    moves = Position.removeIllegalMoves(moves, pos);

    for (int i = 0; i < moves.size(); i++) {
      Move move = moves.get(i);
      Position newPos = pos.doMove(move);

      int perft = perft(newPos, depth - 1);
      String moveStr = Position.getAlgebraicFromIndex(move.getFromIndex()) 
        + Position.getAlgebraicFromIndex(move.getToIndex());

      results.put(moveStr, perft);
    }

    return results;
  }
}
