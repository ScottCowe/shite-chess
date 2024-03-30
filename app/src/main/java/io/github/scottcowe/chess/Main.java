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
      System.out.println("What move?");
      String moveString = scanner.nextLine();

      Move move = Move.fromString(moveString, pos);

      if (move == null) {
        System.out.println("Nuh uh - could be illegal, or ambigous (shouldn't be)");
        continue;
      }

      pos = pos.doMove(move);
      System.out.println(pos);
    }
  }
}
