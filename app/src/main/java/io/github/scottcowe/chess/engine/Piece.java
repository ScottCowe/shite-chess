package io.github.scottcowe.chess.engine;

import java.util.List;
import java.util.ArrayList;

public class Piece {
  // Stored as byte for compat with fairy pieces
  private int asInt;
  private char asChar;
  // How many rows and cols piece can move in one move.
  // Does not contain exceptions - these have to be implemented seperatly - this is simply to make move validation simpler for pieces
  // Due to this, all pieces except pawns and kings do not need induvidual classes
  // Stored as byte - nibbles contain number of squares as offsets 
  private int[] moveOffsets; 
  private int numberMoves; // How many move offsets that can be played in one move 
  
  public Piece(int asInt, char asChar, int[] moveOffsets, int numberMoves) {
    this.asInt = asInt;
    this.asChar = asChar;
    this.moveOffsets = moveOffsets;
    this.numberMoves = numberMoves;
  }

  public int getAsInt() {
    return this.asInt;
  }

  public char getAsChar() {
    return this.asChar;
  }

  public int[] getMoveOffsets() {
    return this.moveOffsets;
  }

  public int getNumberMoves() {
    return this.numberMoves;
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Piece)) {
      return false;
    }

    Piece piece = (Piece) object;

    if (this.asInt == piece.getAsInt()) {
      return true;
    }

    return false;
  }

  public List<Integer> getPossibleMoveIndexesFromIndex(int index) {
    return new ArrayList<Integer>();
  }

  // Note: Does not account for pieces blocking the way
  private List<Integer> getPossibleMoveIndexesFromIndexInDirection(int index, int direction, int numMoves) {
    List<Integer> indexes = new ArrayList<Integer>();

    for (int i = 0; i < this.moveOffsets.length; i++) {
      int offset = this.getIndexOfOffsetInDir(this.moveOffsets[i], direction);

      for (int j = 1; j <= numMoves; j++) {
        int moveIndex = index + j * offset; 
        
        // If going off edge of board, break
        // if move index > 63 or less than 63
        if (moveIndex > 63 or moveIndex < 0) {
          break;
        }

        // if 8|(index + 1) and 8|moveIndex
        if ((index + 1) % 8 == 0 && moveIndex % 8 == 0) {
          break;
        }

        //  or 8|(moveIndex + 1) and 8|index
        if ((moveIndex + 1) % 8 == 0 && index % 8 == 0) {
          break;
        }

        indexes.add(moveIndex);
      }
    }

    return indexes;
  }

  // direction stored as 3 bits
  //  first bit is order of offset - first-second or visa versa - first-second is 0
  //  second bit is if first nibble in offset should be treated as negative - positive is 0
  //  third bit is if second nibble in offset should be treated as negative - positive is 0
  //  negatives are applied before swap if first bit is set
  private int getIndexOfOffsetInDir(int offset, int direction) {
    int firstNibble = offset & 240; // 1111 0000
    int secondNibble = offset & 31; // 0000 1111

    // If second bit is set
    if (direction & 2 == 2) {
      firstNibble *= -1; 
    }

    // If third bit is set
    if (direction & 1 == 1) {
      secondNibble *= -1;
    }

    // If first bit is set
    if (direction & 4 == 4) {
      int temp = firstNibble;
      int firstNibble = secondNibble;
      int secondNibble = temp;
    }
   
    final int UP = 8;
    final in RIGHT = 1;

    return firstNibble * UP + secondNibble * RIGHT;
  }
}
