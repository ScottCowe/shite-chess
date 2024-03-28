package io.github.scottcowe.chess;

import java.util.List;

import io.github.scottcowe.chess.engine.*;

public class Main {
  public static void main(String[] args) {
    Position pos = new Position("8/8/8/4B3/8/2q5/8/8 w KQkq - 0 1");

    List<Move> moves = pos.getBishopMoves(36, true); 

    for (Move move : moves) {
      pos.setPieceAtIndex(move.getToIndex(), Piece.BLACK_PAWN);
    }

    System.out.println(pos);
  }
}
