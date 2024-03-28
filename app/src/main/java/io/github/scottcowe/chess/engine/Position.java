package io.github.scottcowe.chess.engine;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

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
    this.board = new Piece[64];
    Arrays.fill(this.board, Piece.NONE); // Populates board with empty pieces

    String[] splitFen = fen.split(" ");

    // Parses board 
    String[] rows = splitFen[0].split("/");

    int currentIndex = 0;

    for (int i = rows.length - 1; i >= 0; i--) {
      String currentRow = rows[i];

      for (int j = 0; j < currentRow.length(); j++) {
        char currentChar = currentRow.charAt(j);

        if (Character.isDigit(currentChar)) {
          currentIndex += (currentChar - '0'); // Adds currentChar as integer to currentIndex
        }
        else {
          Piece piece = Piece.getFromChar(currentChar);
          this.board[currentIndex] = piece;
          currentIndex += 1;
        }
      }
    }

    this.whitesMove = splitFen[1].equals("w");

    this.castlingRights = 0;

    if (splitFen[2].contains("K")) {
      this.castlingRights += 8;
    }

    if (splitFen[2].contains("Q")) {
      this.castlingRights += 4;
    }

    if (splitFen[2].contains("k")) {
      this.castlingRights += 2;
    }

    if (splitFen[2].contains("q")) {
      this.castlingRights += 1;
    }

    this.enPassentTargetIndex = this.getIndexFromAlgebraic(splitFen[3]);

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

  public int getIndexFromAlgebraic(String algebraic) {
    if (algebraic.length() != 2) {
      return -1;
    }

    char firstChar = algebraic.charAt(0);
    char secondChar = algebraic.charAt(1);

    int rows = Character.getNumericValue(secondChar);
    int cols = firstChar - 97;

    return rows * 8 + cols;
  }

  public String getAlgebraicFromIndex(int index) {
    if (index == -1) {
      return "-";
    }

    int row = (int) (index / 8);
    int col = index % 8 + 1;

    return Character.toString(row) + Character.toString(col); 
  }

  public Position doMove(Move move) {
    Piece[] newBoard = this.board;
    int newCastlingRights = this.castlingRights;
    int newEnPassentTargetIndex = this.enPassentTargetIndex;
    int newHalfmoveClock = this.halfmoveClock;
    int newFullmoveCounter = this.fullmoveCounter + (this.whitesMove ? 0 : 1);

    newBoard = move.applyToBoard(newBoard);

    return new Position(newBoard, !this.whitesMove, newCastlingRights, newEnPassentTargetIndex, newHalfmoveClock, newFullmoveCounter);
  }

  // Returns a list of legal moves for the player to move in the current position
  public List<Move> getLegalMoves() {
    List<Move> moves = new ArrayList<Move>();
    return moves;
  }

  public List<Move> getPseudoLegalMoves() {
    List<Move> moves = new ArrayList<Move>();
    return moves;
  }

  public List<Move> getPawnMoves(int index, boolean isWhite) {
    List<Move> moves = new ArrayList<Move>();

    int direction = isWhite ? 8 : -8;

    if (this.board[index + direction].equals(Piece.NONE)) {
      moves.add(new Move(index, index + direction));
    }

    int startingRank = isWhite ? 1 : 6;

    boolean onStartingRank = (index - startingRank * 8) >= 0 && (index - startingRank * 8) <= 7;

    if (onStartingRank && this.board[index + 2 * direction].equals(Piece.NONE)) {
      moves.add(new Move(index, index + 2 * direction));
    }

    // if direction pm 1 contains enemy piece then that is legal move
    if (this.board[index + direction + 1].isWhite() != isWhite && !this.board[index + direction + 1].equals(Piece.NONE)) {
      moves.add(new Move(index, index + direction + 1));
    }

    if (this.board[index + direction - 1].isWhite() != isWhite && !this.board[index + direction - 1].equals(Piece.NONE)) {
      moves.add(new Move(index, index + direction - 1));
    }

    // if en passent target is set and pawn is in position then that is a legal move
    if (this.enPassentTargetIndex == index + direction + 1 || this.enPassentTargetIndex == index + direction - 1) {
      moves.add(new Move(index, this.enPassentTargetIndex, true));
    }

    return moves;
  }

  // Direction - 0 for towards 8th, 1 for towards h, 2 for towards 1st, 3 for towards a
  private Move getStraightMoveInDirection(int index, int direction) {
    int newIndex = index;

    switch(direction) {
      case 0:
        newIndex += 8;
        break;
      case 1:
        newIndex += 1;
        break;
      case 2:
        newIndex -= 8;
        break;
      case 3:
        newIndex -= 1;
        break;
    }
    
    if (newIndex < 0 || newIndex > 63) {
      return new Move(index, index); // Moving to current square is an error move
    }

    if ((index + 1) % 8 == 0 && newIndex % 8 == 0) {
      return new Move(index, index);
    }

    if (index % 8 == 0 && (newIndex + 1) % 8 == 0) {
      return new Move(index, index);
    }

    return new Move(index, newIndex);
  }

  // Direction - 0 for towards h8, 1 for towards h1, 2 for towards a1, 3 for towards a8 
  private Move getDiagonalMoveInDirection(int index, int direction) {
    int newIndex = index;

    switch(direction) {
      case 0:
        newIndex += 9;
        break;
      case 1:
        newIndex -= 9;
        break;
      case 2:
        newIndex -= 7;
        break;
      case 3:
        newIndex += 7;
        break;
    }
    
    if (newIndex < 0 || newIndex > 63) {
      return new Move(index, index); // Moving to current square is an error move
    }

    if ((index + 1) % 8 == 0 && newIndex % 8 == 0) {
      return new Move(index, index);
    }

    if (index % 8 == 0 && (newIndex + 1) % 8 == 0) {
      return new Move(index, index);
    }

    return new Move(index, newIndex);
  }

  @Override
  public String toString() {
    String seperator = " + - + - + - + - + - + - + - + - + \n";
    String board = seperator;
    String[] rows = new String[8];

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

    // Write additional info 
    board += "\nWhites move: " + this.whitesMove;
    board += "\nCastling rights: " + this.castlingRights;
    board += "\nEn passent target: " + this.getAlgebraicFromIndex(this.enPassentTargetIndex);
    board += "\nHalfmove clock: " + this.halfmoveClock;
    board += "\nFullmove count: " + this.fullmoveCounter;

    return board;
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Position)) {
      return false;
    }

    Position pos = (Position) object;

    // Check if board, move, ... is same

    return true;
  }
}
