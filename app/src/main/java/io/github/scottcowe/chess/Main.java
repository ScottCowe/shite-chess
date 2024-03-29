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

    outer: 
    while (true) {
      System.out.println("What piece to move?");
      String piece = scanner.nextLine();

      int fromIndex = Position.getIndexFromAlgebraic(piece);

      System.out.println("Where to?");
      String square = scanner.nextLine();

      int toIndex = Position.getIndexFromAlgebraic(square);

      if (fromIndex == -1 || toIndex == -1) {
        System.out.println("Nuh uh");
        continue;
      }

      Move move = new Move(fromIndex, toIndex);

      List<Move> moves = pos.getAllPseudoLegalMoves();
      moves = Position.removeIllegalMoves(moves);

      for (Move m : moves) {
        if (m.equals(move)) {
          pos = pos.doMove(move);
          System.out.println(pos);
          continue outer;        
        }
      }

      System.out.println("Illegal");
    }
  }
}
