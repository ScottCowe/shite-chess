package io.github.scottcowe.chess;

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

  public Board() {
    this.setPiece(1, 0);
    this.setPiece(9, 63);
  }

  public void setPiece(int piece, int index) {
    this.boardArray[index] = piece; 
  }

  public int getPiece(int index) {
    return this.boardArray[index];
  }

  public static char getPieceByNibble(int piece) {
    if (piece == 1) {
      return 'K';
    }
    else if (piece == 9) {
      return 'p';
    }
    return ' ';
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
      rows[currentRow] = " | " + this.getPieceByNibble(this.getPiece(i)) + rows[currentRow];
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
