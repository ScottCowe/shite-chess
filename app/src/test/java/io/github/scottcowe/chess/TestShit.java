package io.github.scottcowe.chess;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import io.github.scottcowe.chess.engine.*;

public class TestShit {
  @Test 
  void testPseudolegalMoveGeneration() {
    Position pos = new Position();
    List<Move> moves = Position.getAllPseudoLegalMoves(pos);
    assertEquals(moves.size(), 20);
  }

  @Test
  void perftDefaultPosition() {
    Position pos = new Position();

    // Values from https://www.chessprogramming.org/Perft_Results
    assertEquals(this.perft(pos, 1), 20);
    assertEquals(this.perft(pos, 2), 400);
    assertEquals(this.perft(pos, 3), 8902);
    //assertEquals(this.perft(pos, 4), 197281);
    //assertEquals(this.perft(pos, 5), 4865609);
  }

  private int perft(Position pos, int depth) {
    int nodes = 0;

    List<Move> moves = Position.getAllPseudoLegalMoves(pos); 
    moves = Position.removeIllegalMoves(moves, pos);

    if (depth == 1) {
      return moves.size();
    }

    for (int i = 0; i < moves.size(); i++) {
      Move move = moves.get(i);
      Position newPos = pos.doMove(move);
      nodes += this.perft(newPos, depth - 1);
    }

    return nodes;
  }
}
