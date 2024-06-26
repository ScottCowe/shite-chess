package io.github.scottcowe.chess;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.swing.text.Position;

import io.github.scottcowe.chess.engine.Game;
import io.github.scottcowe.chess.engine.Move;
import io.github.scottcowe.chess.uci.UCI;

public class Main {
  public static void main(String[] args) {
    //simpleGame(new Position()); 
    uci();
  }

  public static void uci() {
    UCI uci = UCI.getInstance();
    uci.setBot(new Bot());
    Thread uciThread = new Thread(uci);
    uciThread.start();
  }

  public static void simpleGame(Position pos) {
    Game game = new Game(pos);

    System.out.println(game.getCurrentPosition());

    Scanner scanner = new Scanner(System.in);

    while (!game.ended()) {
      List<Move> moves = Position.getAllPseudoLegalMoves(game.getCurrentPosition());
      moves = Position.removeIllegalMoves(moves, game.getCurrentPosition());
      List<String> moveStrings = moves.stream().map(m -> m.toString()).collect(Collectors.toList());

      System.out.println(Arrays.toString(moveStrings.toArray()));

      System.out.println("\nWhat move?");
      String moveStr = scanner.nextLine();

      Move move = Move.fromString(moveStr, game.getCurrentPosition());

      boolean legal = moveStrings.contains(move.toString());

      if (move == null || !legal) {
        System.out.println("Nuh uh");
        continue;
      }

      game.doMove(move);

      System.out.println(game.getCurrentPosition());
    }

    System.out.println("Game ended");
    System.out.println("Result: " + game.getResult()); 
  }
}
