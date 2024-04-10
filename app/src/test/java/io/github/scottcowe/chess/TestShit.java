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
  @Disabled
  void perftDefaultPosition() {
    Position pos = new Position();

    // Values from https://www.chessprogramming.org/Perft_Results
    assertEquals(this.perft(pos, 1), 20);
    assertEquals(this.perft(pos, 2), 400);
    assertEquals(this.perft(pos, 3), 8902);
    assertEquals(this.perft(pos, 4), 197281);
    assertEquals(this.perft(pos, 5), 4865609);
  }

  @Test
  @Disabled
  void perftPositionOne() {
    Position pos = new Position("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");

    assertEquals(this.perft(pos, 1), 48);
    assertEquals(this.perft(pos, 2), 2039);
    assertEquals(this.perft(pos, 3), 97862);
    assertEquals(this.perft(pos, 4), 4085603);
  }

  @Test
  void perftPositionTwo() {
    Position pos = new Position("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1");

    assertEquals(this.perft(pos, 1), 14); 
    assertEquals(this.perft(pos, 2), 191); 
    assertEquals(this.perft(pos, 3), 2812); 
    assertEquals(this.perft(pos, 4), 43238); 
    assertEquals(this.perft(pos, 5), 674328); 
  }

  @Test
  void divideTestPos() {
    Position pos = new Position("8/2p5/3p4/1P5r/KR3p1k/8/4P1P1/8 b - - 0 1");
    int depth = 1;

    int result = 0;

    HashMap<String, Integer> results = divide(pos, depth);

    for (String s : results.keySet()) {
      result += results.get(s);
      System.out.println(s + ": " + results.get(s));
    }

    System.out.println(result);

    assertEquals(1 + 1, 3);
  }

  private static int perft(Position pos, int depth) {
    int nodes = 0;

    List<Move> moves = Position.getAllPseudoLegalMoves(pos); 
    moves = Position.removeIllegalMoves(moves, pos);

    if (depth == 1) {
      return moves.size();
    }

    if (depth == 0) {
      return 1;
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
