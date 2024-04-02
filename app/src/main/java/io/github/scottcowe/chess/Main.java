package io.github.scottcowe.chess;

import java.util.List;
import java.util.Scanner;

import io.github.scottcowe.chess.engine.*;

public class Main {
  public static void main(String[] args) {
    //simpleGame(); 
    

  }

  public static int perft(Position pos, int depth) {
    int nodes = 0;

    List<Move> moves = Position.getAllPseudoLegalMoves(pos); 
    moves = Position.removeIllegalMoves(moves, pos);

    if (depth == 1) {
      return moves.size();
    }

    for (int i = 0; i < moves.size(); i++) {
      Move move = moves.get(i);

      Position newPos = pos.doMove(move);
      nodes += perft(newPos, depth - 1);
    }

    return nodes;
  }

  public static void simpleGame() {
    Position pos = new Position();
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
