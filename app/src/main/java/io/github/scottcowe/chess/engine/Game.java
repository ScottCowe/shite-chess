package io.github.scottcowe.chess.engine;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Game {
  private List<Position> positions;
  private int result; // 00 - ongoing, 01 - draw, 10 - white win, 11 - black win

  public Game(List<Position> positions, int result) {
    this.positions = positions;
    this.result = result;
  }

  public Game(Position initialPos) {
    List<Position> positions = new ArrayList<Position>();
    positions.add(initialPos);
    
    this.positions = positions;
    this.result = 0b00;
  }

  public Game() {
    this(new Position()); 
  }

  public void doMove(Move move) {
    if (this.result != 0b00) {
      System.out.println("cannot play move in completed game");
      return;
    }

    Position pos = this.getCurrentPosition().doMove(move);
    this.positions.add(pos);

    // Check for checkmate, stalemate, or draw by repetition
    if (Game.isCheckmate(pos)) {
      this.result = pos.isWhitesMove() ? 0b11 : 0b10;
    }
    else if (Game.isStalemate(pos) || Game.isDrawByRepetition(pos, this.positions)) {
      this.result = 0b01;
    }
  }

  // TODO: Check that logic is sound (and that it actually works)

  public static boolean isCheckmate(Position pos) {
    List<Move> moves = Position.getAllPseudoLegalMoves(pos.turnSwitch());
    moves = Position.removeIllegalMoves(moves, pos.turnSwitch());

    boolean inCheck = Position.inCheck(pos, pos.isWhitesMove());

    if (inCheck && moves.size() == 0) {
      return true;
    }

    return false;
  }

  public static boolean isStalemate(Position pos) {
    boolean inCheck = Position.inCheck(pos, pos.isWhitesMove());
    List<Move> moves = Position.getAllPseudoLegalMoves(pos);
    moves = Position.removeIllegalMoves(moves, pos);

    if (!inCheck && moves.size() == 0) {
      return true;
    }

    return false;
  }

  public static boolean isDrawByRepetition(Position pos, List<Position> positions) {
    return false;
  }

  public boolean ended() {
    return this.result != 0b00;
  }

  public void addPosition(Position pos) {
    this.positions.add(pos);
  }

  public List<Position> getPositions() {
    return this.positions;
  }

  public Position getPositionAtIndex(int index) {
    return this.positions.get(index);
  }

  public Position getCurrentPosition() {
    return this.positions.get(this.positions.size() - 1);
  }

  public int getResult() {
    return this.result;
  }
}
