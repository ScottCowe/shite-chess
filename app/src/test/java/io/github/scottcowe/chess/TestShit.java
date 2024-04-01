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

    assertEquals(this.perft(pos, 1), 20);
    assertEquals(this.perft(pos, 2), 400);
  }

  private int perft(Position pos, int depth) {
    int nodes = 0;

    List<Move> moves = Position.getAllPseudoLegalMoves(pos); 
    moves = Position.removeIllegalMoves(moves);

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
