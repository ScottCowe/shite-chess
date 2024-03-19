package io.github.scottcowe.chess.engine;

import java.util.List;
import java.util.ArrayList;

public class Board {
  // Each piece is stored as a nibble
  //  first bit contains info on piece's color - 0 if white, 1 if black 
  //  remaining bits contains data on what piece it is
  //    000 - none
  //    001 - king
  //    010 - queen
  //    011 - rook
  //    100 - bishop
  //    101 - knight
  //    110  - pawn
  //  ex: 0001 (1) is white king
  //  0000 is empty square
  //
  //  a1 is index 0, a2 index 1, ...
  //  b1 is index 8, c1 is index 16, ...
  private int[] boardArray = new int[64];

  public void setPiece(int piece, int index) {
    this.boardArray[index] = piece; 
  }

  public int getPiece(int index) {
    return this.boardArray[index];
  }

  public List<Integer> getPieceIndexesByNibble(int piece) {
    List<Integer> indexes = new ArrayList<Integer>();

    for (int i = 0; i < 64; i++) {
      if (this.getPiece(i) == piece) {
        indexes.add(i);
      }
    }

    return indexes;
  }

  public static char getPieceByNibble(int piece) {
    boolean isWhite = (piece & 8) == 0;
    char pieceChar = ' '; 

    switch(piece & 7) {
      case 1:
        pieceChar = 'K';
        break;
      case 2:
        pieceChar = 'Q';
        break;
      case 3:
        pieceChar = 'R';
        break; 
      case 4:
        pieceChar = 'B';
        break;
      case 5:
        pieceChar = 'N';
        break;
      case 6:
        pieceChar = 'P';
        break;
    }

    return isWhite ? pieceChar : Character.toLowerCase(pieceChar);
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

      rows[currentRow] = " | " + Board.getPieceByNibble(this.getPiece(i)) + rows[currentRow];

      if (i % 8 == 0) {
        currentRow--;
      }
    }

    for (int i = 7; i >= 0; i--) {
      board += rows[i];
      board += seperator;
    }

    return board;
  }
}
