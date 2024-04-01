package io.github.scottcowe.chess;

import java.util.List;
import java.util.Scanner;

import io.github.scottcowe.chess.engine.*;

public class Main {
  public static void main(String[] args) {
    simpleGame(); 
  }

  public static void simpleGame() {
    Position pos = new Position();
    System.out.println(pos);

    Scanner scanner = new Scanner(System.in);

    while (true) {
      List<Move> moves = Position.getAllPseudoLegalMoves(pos); 
      moves = Position.removeIllegalMoves(moves);

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
