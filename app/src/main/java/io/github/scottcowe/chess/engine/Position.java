package io.github.scottcowe.chess.engine;

public class Position {
  private int[] board;
  private boolean whitesMove;
  private int castlingRights;
  private int enPassentTargetIndex;
  private int halfmoveClock;
  private int fullmoveCounter;

  public Position(int[] board, boolean whitesMove, int castlingRights, int enPassentTargetIndex, int halfmoveClock, int fullmoveCounter) {
    this.board = board;
    this.whitesMove = whitesMove;
    this.castlingRights = castlingRights;
    this.enPassentTargetIndex = enPassentTargetIndex;
    this.halfmoveClock = halfmoveClock;
    this.fullmoveCounter = fullmoveCounter;
  }

  public Position(String fen) {
    // Parse FEN and set stuff accordingly
  }

  public Position() {
    this("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
  }

  public int getPieceAtIndex(int index) {
    return this.board[index];
  }

  public boolean isWhitesMove() {
    return this.whitesMove;
  }

  public int getCastlingRights() {
    return this.castlingRights;
  }

  public int getEnPassentTargetIndex() {
    return this.enPassentTargetIndex;
  }

  public int getHalfmoveClock() {
    return this.halfmoveClock;
  }

  public int getFullmoveCounter() {
    return this.fullmoveCounter;
  }

  public Position doMove(Move move) {
    int[] newBoard = this.board;
    int newCastlingRights = this.castlingRights;
    int newEnPassentTargetIndex = this.enPassentTargetIndex;
    int newHalfmoveClock = this.halfmoveClock;
    int newFullmoveCounter = this.fullmoveCounter + (this.whitesMove ? 0 : 1);

    return new Position(newBoard, !this.whitesMove, newCastlingRights, newEnPassentTarget, newHalfmoveClock, newFullmoveCounter);
  }
}
