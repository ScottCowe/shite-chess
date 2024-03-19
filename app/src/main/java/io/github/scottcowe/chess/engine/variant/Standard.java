package io.github.scottcowe.chess.engine.variant;

public class Standard extends Variant {
  private int[] board;

  public Standard() {
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
    this.board = new int[64];
  }

  public int getPieceAtIndex(int index) {
    return this.board[index];
  }

  public void setPieceAtIndex(int index, int piece) {
    this.board[index] = piece;
  }

  public char getPieceByInt(int piece) {
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

  public int getPieceByChar(char piece) {
    boolean isWhite = piece == Character.toUpperCase(piece);
    int pieceInt = -1;

    switch(Character.toUpperCase(piece)) {
      case 'K':
        pieceInt = 1;
        break;
      case 'Q':
        pieceInt = 2;
        break;
      case 'R':
        pieceInt = 3;
        break;
      case 'B':
        pieceInt = 4;
        break;
      case 'N':
        pieceInt = 5;
        break;
      case 'P':
        pieceInt = 6;
        break;
    }

    return pieceInt;
  }

  public List<Integer> getPieceIndexesByType(int type) {
    List<Integer> indexes = new ArrayList<Integer>();

    for (int i = 0; i < 64; i++) {
      if (this.getPiece(i) == piece) {
        indexes.add(i);
      }
    }

    return indexes;
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

      rows[currentRow] = " | " + Board.getPieceByNibble(this.getPieceAtIndex(i)) + rows[currentRow];

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
