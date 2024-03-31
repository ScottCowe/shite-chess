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
}
