package io.github.scottcowe.chess.engine;

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

  public Move[] getPossibleMovesFromIndex(int index) {
    return [];
  }
}
