package io.github.scottcowe.chess;

import java.util.List;
import java.util.Scanner;

import io.github.scottcowe.chess.engine.*;

public class Main {
  public static void main(String[] args) {
    simpleGame(); 
  }


  public static void simpleGame() {
    Position pos = new Position("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
    System.out.println(pos);

    Scanner scanner = new Scanner(System.in);

    while (true) {
      List<Move> moves = Position.getAllPseudoLegalMoves(pos); 
      moves = Position.removeIllegalMoves(moves, pos);

      String movesStr = "";

      for (Move move : moves) {
        movesStr += move + " "; 
      }

      System.out.println(movesStr);

      System.out.println("What move?");
      String moveString = scanner.nextLine();

      Move move = Move.fromString(moveString, pos);

      if (move == null) {
        System.out.println("Nuh uh");
        continue;
      }

      pos = pos.doMove(move);
      System.out.println(pos);
    }
  }
}
