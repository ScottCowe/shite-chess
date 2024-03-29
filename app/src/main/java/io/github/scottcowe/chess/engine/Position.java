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

  // For testing purposes
  public void setPieceAtIndex(int index, Piece piece) {
    this.board[index] = piece;
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

  public static int getIndexFromAlgebraic(String algebraic) {
    if (algebraic.length() != 2) {
      return -1;
    }

    char firstChar = algebraic.charAt(0);
    char secondChar = algebraic.charAt(1);

    int rows = Character.getNumericValue(secondChar) - 1;
    int cols = firstChar - 97;

    return rows * 8 + cols;
  }

  public static String getAlgebraicFromIndex(int index) {
    if (index == -1) {
      return "-";
    }

    int row = (int) (index / 8);
    int col = index % 8;

    return Character.toString(col + 'a') + Character.toString(row + '1'); 
  }

  public List<Integer> getPieceIndexesOfType(Piece piece) {
    List<Integer> indexes = new ArrayList<Integer>();

    for (int i = 0; i < 64; i++) {
      if (this.board[i].equals(piece)) {
        indexes.add(i);
      }
    }

    return indexes;
  }

  public Position doMove(Move move) {
    Piece[] newBoard = move.applyToBoard(this.board);
    int newCastlingRights = this.castlingRights & ~move.getCastling();
    int newEnPassentTargetIndex = move.getEnPassentTargetIndex();
    int newHalfmoveClock = move.shouldHalfmoveReset() ? 0 : this.halfmoveClock + 1;
    int newFullmoveCounter = this.fullmoveCounter + (this.whitesMove ? 0 : 1);

    return new Position(newBoard, !this.whitesMove, newCastlingRights, newEnPassentTargetIndex, newHalfmoveClock, newFullmoveCounter);
  }

  public static List<Move> removeIllegalMoves(List<Move> pseudoLegalMoves) {
    List<Move> moves = pseudoLegalMoves;
    return moves;
  }

  public List<Move> getAllPseudoLegalMoves() {
    List<Move> moves = new ArrayList<Move>();

    for (Piece pieceType : Piece.values()) {
      if (pieceType != Piece.NONE && pieceType.isWhite() == this.whitesMove) {
        List<Integer> indexes = this.getPieceIndexesOfType(pieceType);
        for (Integer index : indexes) {
          moves.addAll(this.getPseudoLegalMovesForPiece(index));
        }
      }
    }

    return moves;
  }

  public List<Move> getPseudoLegalMovesForPiece(int index) {
    List<Move> moves = new ArrayList<Move>();

    Piece.Type type = this.board[index].getType();
    boolean isWhite = this.getPieceAtIndex(index).isWhite();

    switch(type) {
      case Piece.Type.KING:
        moves = this.getKingMoves(index, isWhite); 
        break;
      case Piece.Type.QUEEN:
        moves = this.getQueenMoves(index, isWhite); 
        break;
      case Piece.Type.ROOK:
        moves = this.getRookMoves(index, isWhite); 
        break;
      case Piece.Type.BISHOP:
        moves = this.getBishopMoves(index, isWhite); 
        break;
      case Piece.Type.KNIGHT:
        moves = this.getKnightMoves(index, isWhite); 
        break;
      case Piece.Type.PAWN:
        moves = this.getPawnMoves(index, isWhite); 
        break;
    }

    return moves;
  }

  // TODO: Clean up code
  public List<Move> getKingMoves(int index, boolean isWhite) {
    List<Move> moves = new ArrayList<Move>();

    boolean whiteKingside = (this.castlingRights & 8) == 8;
    boolean whiteQueenside = (this.castlingRights & 4) == 4;
    boolean blackKingside = (this.castlingRights & 2) == 2;
    boolean blackQueenside = (this.castlingRights & 1) == 1;

    if (this.board[5].equals(Piece.NONE) && this.board[6].equals(Piece.NONE) && whiteKingside && isWhite) {
      moves.add(new Move(8)); 
    }

    if (this.board[3].equals(Piece.NONE) && this.board[2].equals(Piece.NONE) && this.board[1].equals(Piece.NONE) && whiteQueenside && isWhite) {
      moves.add(new Move(4));
    }

    if (this.board[61].equals(Piece.NONE) && this.board[62].equals(Piece.NONE) && blackKingside && !isWhite) {
      moves.add(new Move(2)); 
    }

    if (this.board[59].equals(Piece.NONE) && this.board[58].equals(Piece.NONE) && this.board[57].equals(Piece.NONE) && blackQueenside && !isWhite) {
      moves.add(new Move(1));
    }

    for (int i = 0; i < 4; i++) {
      int moveIndex = this.getStraightMoveIndexInDirection(index, i);

      if (moveIndex == -1) {
        continue;
      }

      Piece pieceAtIndex = this.board[moveIndex];
      
      if (pieceAtIndex.equals(Piece.NONE) || pieceAtIndex.isWhite() != isWhite) {
        moves.add(new Move(index, moveIndex));
      }

      moveIndex = this.getDiagonalMoveIndexInDirection(index, i);

      if (moveIndex == -1) {
        continue;
      }

      pieceAtIndex = this.board[moveIndex];
      
      if (pieceAtIndex.equals(Piece.NONE) || pieceAtIndex.isWhite() != isWhite) {
        moves.add(new Move(index, moveIndex));
      }
    }

    return moves;
  }

  public List<Move> getQueenMoves(int index, boolean isWhite) {
    List<Move> moves = new ArrayList<Move>();
    
    for (int i = 0; i < 4; i++) {
      int moveIndex = this.getStraightMoveIndexInDirection(index, i);

      while (moveIndex != -1) {
        if (this.board[moveIndex].equals(Piece.NONE)) {
          moves.add(new Move(index, moveIndex));
          moveIndex = this.getStraightMoveIndexInDirection(moveIndex, i);
        }
        else if (this.board[moveIndex].isWhite() != isWhite) {
          moves.add(new Move(index, moveIndex));
          moveIndex = -1;
        }
        else {
          moveIndex = -1;
        }
      }

      moveIndex = this.getDiagonalMoveIndexInDirection(index, i);

      while (moveIndex != -1) {
        if (this.board[moveIndex].equals(Piece.NONE)) {
          moves.add(new Move(index, moveIndex));
          moveIndex = this.getDiagonalMoveIndexInDirection(moveIndex, i);
        }
        else if (this.board[moveIndex].isWhite() != isWhite) {
          moves.add(new Move(index, moveIndex));
          moveIndex = -1;
        }
        else {
          moveIndex = -1;
        }
      }
    }

    return moves;
  }

  public List<Move> getRookMoves(int index, boolean isWhite) {
    List<Move> moves = new ArrayList<Move>();
    
    for (int i = 0; i < 4; i++) {
      int moveIndex = this.getStraightMoveIndexInDirection(index, i);

      while (moveIndex != -1) {
        if (this.board[moveIndex].equals(Piece.NONE)) {
          moves.add(new Move(index, moveIndex));
          moveIndex = this.getStraightMoveIndexInDirection(moveIndex, i);
        }
        else if (this.board[moveIndex].isWhite() != isWhite) {
          moves.add(new Move(index, moveIndex));
          moveIndex = -1;
        }
        else {
          moveIndex = -1;
        }
      }
    }

    return moves;
  }

  public List<Move> getBishopMoves(int index, boolean isWhite) {
    List<Move> moves = new ArrayList<Move>();
    
    for (int i = 0; i < 4; i++) {
      int moveIndex = this.getDiagonalMoveIndexInDirection(index, i);

      while (moveIndex != -1) {
        if (this.board[moveIndex].equals(Piece.NONE)) {
          moves.add(new Move(index, moveIndex));
          moveIndex = this.getDiagonalMoveIndexInDirection(moveIndex, i);
        }
        else if (this.board[moveIndex].isWhite() != isWhite) {
          moves.add(new Move(index, moveIndex));
          moveIndex = -1;
        }
        else {
          moveIndex = -1;
        }
      }
    }

    return moves;
  }

  public List<Move> getKnightMoves(int index, boolean isWhite) {
    List<Move> moves = new ArrayList<Move>();

    int[] offsets = { 10, -6, 6, -10, 15, 17, -15, -17 };

    for (int i = 0; i < 8; i++) {
      int newIndex = index + offsets[i];

      if (newIndex > 63 || newIndex < 0) {
        continue;
      }

      // if difference in rank between index and newIndex is not 2 and diff in col not 2
      int fromRank = index % 8;
      int toRank = newIndex % 8;
      int rankDiff = Math.abs(fromRank - toRank);

      if (rankDiff > 2) {
        continue; 
      }

      if (this.board[newIndex].isWhite() == isWhite) {
        continue;
      }

      moves.add(new Move(index, newIndex));
    }

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

    if (onStartingRank && this.board[index + 2 * direction].equals(Piece.NONE) && this.board[index + direction].equals(Piece.NONE)) {
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
  private int getStraightMoveIndexInDirection(int index, int direction) {
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
      return -1; 
    }

    if ((index + 1) % 8 == 0 && newIndex % 8 == 0) {
      return -1;
    }

    if (index % 8 == 0 && (newIndex + 1) % 8 == 0) {
      return -1;
    }

    return newIndex;
  }

  // Direction - 0 for towards h8, 1 for towards h1, 2 for towards a1, 3 for towards a8 
  private int getDiagonalMoveIndexInDirection(int index, int direction) {
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
      return -1;
    }

    if ((index + 1) % 8 == 0 && newIndex % 8 == 0) {
      return -1;
    }

    if (index % 8 == 0 && (newIndex + 1) % 8 == 0) {
      return -1;
    }

    return newIndex;
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
