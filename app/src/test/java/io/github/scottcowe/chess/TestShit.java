package io.github.scottcowe.chess;

import org.junit.jupiter.api.Test;
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

    int depth3 = 0;

    HashMap<String, Integer> depth3Divide = divide(pos, 3);

    for (String s : depth3Divide.keySet()) {
      depth3 += depth3Divide.get(s);
      System.out.println(s + ": " + depth3Divide.get(s));
    }

    int depth1 = this.perft(pos, 1);
    int depth2 = this.perft(pos, 2);
    //int depth3 = this.perft(pos, 3);

    // Values from https://www.chessprogramming.org/Perft_Results
    assertEquals(depth1, 20);
    assertEquals(depth2, 400);
    assertEquals(depth3, 8902);
    //assertEquals(this.perft(pos, 4), 197281);
    //assertEquals(this.perft(pos, 5), 4865609);
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
