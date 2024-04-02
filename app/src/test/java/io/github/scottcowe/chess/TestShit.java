package io.github.scottcowe.chess;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;

import io.github.scottcowe.chess.engine.*;

public class TestShit {
  private List<String> moves = new ArrayList<String>();
  private List<Integer> count = new ArrayList<Integer>();

  @Test
  void perftDefaultPosition() {
    Position pos = new Position();

    int depth1 = this.perft(pos, 1);
    int depth2 = this.perft(pos, 2);
    int depth3 = this.perft(pos, 3);

    for(int i = 0; i < this.moves.size(); i++) {
      System.out.println(this.moves.get(i) + ": " + this.count.get(i));
    }

    // Values from https://www.chessprogramming.org/Perft_Results
    assertEquals(depth1, 20);
    assertEquals(depth2, 400);
    assertEquals(depth3, 8902);
    //assertEquals(this.perft(pos, 4), 197281);
    //assertEquals(this.perft(pos, 5), 4865609);
  }

  // https://www.chessprogramming.org/Perft_Results
  /*@Test
  void perftKnownPositions() {
    Position pos = new Position("08/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1");

    assertEquals(this.perft(pos, 1), 14);
    assertEquals(this.perft(pos, 2), 191);
    assertEquals(this.perft(pos, 3), 2812);
  }*/

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
}
