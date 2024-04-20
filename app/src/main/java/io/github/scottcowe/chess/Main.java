package io.github.scottcowe.chess;

import java.util.List;
import java.util.Scanner;

import io.github.scottcowe.chess.engine.*;
import io.github.scottcowe.chess.uci.*;

public class Main {
  public static void main(String[] args) {
    //simpleGame();
    //simpleGame(new Position("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1")); 
    uci();
  }

  public static void uci() {
    UCI uci = UCI.getInstance();
    uci.init();
  }

  public static void simpleGame() {
    Position pos = new Position();
    simpleGame(pos);
  }

  public static void simpleGame(Position pos) {
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

      // TODO: Maybe fix this
      if (move == null /*|| !moves.contains(move)*/) {
        System.out.println("Nuh uh");
        continue;
      }

      pos = pos.doMove(move);
      System.out.println(pos);
    }
  }
}
