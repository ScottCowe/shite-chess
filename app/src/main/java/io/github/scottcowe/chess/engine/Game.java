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

  public static boolean isCheckmate(Position pos) {
    // Get list of all pieces attacking king
    // If length is 0
    //  not checkmate
    // if length is 2
    //  get all legal king moves
    //  if none then mate
    //  if a move puts king out of check then not mate
    //  else mate

    return false;
  }

  public static boolean isStalemate(Position pos) {
    boolean inCheck = Position.inCheck(pos, pos.isWhitesMove());
    List<Move> moves = Position.getPseudoLegalMoves(pos);
    moves = Position.removeIllegalMoves(moves, pos);

    if (!inCheck && moves.size() == 0) {
      return true;
    }

    return false;
  }

  public static boolean isDrawByRepetition(Position pos, List<Position> positions) {
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
