package io.github.scottcowe.chess.engine;

import java.util.List;

public class Game {
  private List<Position> positions;
  private int result; // 00 - ongoing, 01 - draw, 10 - white win, 11 - black win

  public Game(List<Position> positions, int result) {
    this.positions = positions;
    this.result = result;
  }

  public void doMove(Move move) {
    // Apply move to position
    // Append position to positions
    // Check for checkmate, stalemate, or draw by repetition
  }

  public boolean isCheckmate(Position pos) {
    return false;
  }

  public boolean isStalemate(Position pos) {
    return false;
  }

  public boolean isDrawByRepetition(Position pos) {
    return false;
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
