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

    // Values from https://www.chessprogramming.org/Perft_Results
    assertEquals(this.perft(pos, 1), 20);
    assertEquals(this.perft(pos, 2), 400);
    assertEquals(this.perft(pos, 3), 8902);
    assertEquals(this.perft(pos, 4), 197281);
    assertEquals(this.perft(pos, 5), 4865609);
  }

  @Test
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
    assertEquals(this.perft(pos, 5), 674624); 
  }

  @Test
  void perftPositionThree() {
    Position pos = new Position("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1");
  
    assertEquals(this.perft(pos, 1), 6); 
    assertEquals(this.perft(pos, 2), 264); 
    assertEquals(this.perft(pos, 3), 9467); 
    assertEquals(this.perft(pos, 4), 422333); 
  }

  @Test
  void perftPositionFour() {
    Position pos = new Position("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8");

    assertEquals(this.perft(pos, 1), 44); 
    assertEquals(this.perft(pos, 2), 1486); 
    assertEquals(this.perft(pos, 3), 62379); 
    assertEquals(this.perft(pos, 4), 2103487); 
  }

  @Test
  void perftPositionFive() {
    Position pos = new Position("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10");

    assertEquals(this.perft(pos, 1), 46); 
    assertEquals(this.perft(pos, 2), 2079); 
    assertEquals(this.perft(pos, 3), 89890); 
    assertEquals(this.perft(pos, 4), 3894594); 
  }

  @Test
  @Disabled
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
