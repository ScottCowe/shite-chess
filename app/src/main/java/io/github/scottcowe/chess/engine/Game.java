package io.github.scottcowe.chess.engine;

public class Game {
  private Position[] positions; 

  public Game(Position[] positions) {
    this.positions = positions;
  }

  public Game() {
    super([]);
  }

  // 0b00 - ongoing
  // 0b01 - checkmate
  // 0b10 - stalemate
  // 0b11 - draw
  //
  // for checkmate and stalemate winner is whoever just moved
  public int getGameState() {
    return 0b00;
  }

  private boolean isCheckmate() {

  }
  
  private boolean isStalemate() {

  }

  private boolean isDraw() {

  }

  public static Game fromPGN(String pgn) {
    return new Game();
  }
}
