package io.github.scottcowe.chess;

import java.util.List;
import java.util.Scanner;

import io.github.scottcowe.chess.engine.*;

public class Main {
  public static void main(String[] args) {
    simpleGame(); 

    /*Position pos = new Position("1n6/8/8/8/8/8/8/8 b KQkq - 0 1");
    System.out.println(pos);

    List<Move> possibleMoves = Position.getAllPseudoLegalMoves(pos, pos.isWhitesMove());
    possibleMoves = Position.removeIllegalMoves(possibleMoves);

    String moves = "";

    for (Move move : possibleMoves) {
      moves += move + " "; 
    }

    System.out.println(moves);*/
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
        System.out.println("Nuh uh");
        continue;
      }

      pos = pos.doMove(move);
      System.out.println(pos);
    }
  }
}
