package io.github.scottcowe.chess.engine;

public class Position {
  private Piece[] board;
  private boolean whitesMove;
  private int castlingRights;
  private int enPassentTargetIndex;
  private int halfmoveClock;
  private int fullmoveCounter;

  public Position(Piece[] board, boolean whitesMove, int castlingRights, int enPassentTargetIndex, int halfmoveClock, int fullmoveCounter) {
    this.board = board;
    this.whitesMove = whitesMove;
    this.castlingRights = castlingRights;
    this.enPassentTargetIndex = enPassentTargetIndex;
    this.halfmoveClock = halfmoveClock;
    this.fullmoveCounter = fullmoveCounter;
  }

  public Position(String fen) {
    String[] splitFen = fen.split(" ");

    // Do board
    
    this.whitesMove = splitFen[1] == "w";
    
    this.castlingRights = 0;

    if (splitFen[2].contains("K") {
      this.castlingRights += 8;
    }

    if (splitFen[2].contains("Q") {
      this.castlingRights += 4;
    }

    if (splitFen[2].contains("k") {
      this.castlingRights += 2;
    }

    if (splitFen[2].contains("q") {
      this.castlingRights += 1;
    }

    // Algebraic notation to index - todo

    this.halfmoveClock = Integer.parseInt(splitFen[4]);

    this.fullmoveCounter = Integer.parseInt(splitFen[5]);
  }

  public Position() {
    this("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
  }

  public Piece getPieceAtIndex(int index) {
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
    Piece[] newBoard = this.board;
    int newCastlingRights = this.castlingRights;
    int newEnPassentTargetIndex = this.enPassentTargetIndex;
    int newHalfmoveClock = this.halfmoveClock;
    int newFullmoveCounter = this.fullmoveCounter + (this.whitesMove ? 0 : 1);

    newBoard = move.applyToBoard(newBoard);

    return new Position(newBoard, !this.whitesMove, newCastlingRights, newEnPassentTarget, newHalfmoveClock, newFullmoveCounter);
  }

  @Override
  public String toString() {
    String seperator = " + - + - + - + - + - + - + - + - + \n";
    String board = seperator;
    String[] rows = new String[8];

    // For each index from end
    //  if 8|(i+1) push \n to board
    //  push "{piece} | " to board
    //  if 8|i 
    //    push " | "
    //    push seperator
    int currentRow = 7;
    for (int i = 63; i >= 0; i--) {
      if ((i + 1) % 8 == 0) {
        rows[currentRow] = " | \n";
      }

      rows[currentRow] = " | " + this.getPieceAtIndex(i).getAsChar() + rows[currentRow];

      if (i % 8 == 0) {
        currentRow--;
      }
    }

    // Write board
    for (int i = 7; i >= 0; i--) {
      board += rows[i];
      board += seperator;
    }

    return board;
  }
}
