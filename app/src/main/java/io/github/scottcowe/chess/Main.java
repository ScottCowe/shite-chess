package io.github.scottcowe.chess;

import java.util.List;
import java.util.Scanner;

import io.github.scottcowe.chess.engine.*;

public class Main {
  public static void main(String[] args) {
    //simpleGame(); 

    Position pos = new Position("8/8/8/8/8/8/2N1N3/8 w KQkq - 0 1");
    System.out.println(pos);

    List<Move> moves = Position.getAllPseudoLegalMoves(pos, pos.isWhitesMove());
    moves = Position.removeIllegalMoves(moves);

    for (Move move : moves) {
      System.out.println(move);
    }
  }

  // TODO: Make castling work here, as well as promoting
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

      Move move = new Move(Move.MoveType.STANDARD, pos).setFromIndex(fromIndex).setToIndex(toIndex);

      List<Move> moves = Position.getAllPseudoLegalMoves(pos, pos.isWhitesMove());
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
