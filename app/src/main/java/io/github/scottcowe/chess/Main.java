package io.github.scottcowe.chess;

import java.util.List;

import io.github.scottcowe.chess.engine.*;

public class Main {
  public static void main(String[] args) {
    Position pos = new Position();
    System.out.println(pos);

    List<Move> moves = pos.getAllPseudoLegalMoves();
    moves = Position.removeIllegalMoves(moves);

    Move move = new Move(12, 28);

    for (Move m : moves) {
      if (m.equals(move)) {
        pos = pos.doMove(move);
        break;
      }
    }

    System.out.println(pos);
  }
}
