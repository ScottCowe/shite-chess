package io.github.scottcowe.chess.engine.variant;

public class Variant {
  private int[] board;

  public Variant(int numberSquares) {
    this.board = new int[numberSquares];
  }

  public int getPieceAtIndex(int index) {
    return this.board[index];
  }

  public void setPieceAtIndex(int index, int piece) {
    this.board[index] = piece;
  }

  public char getPieceByInt(int piece) {
    return '?';
  }

  public int getPieceByChar(char piece) {
    return -1;
  }

  public List<Integer> getPieceIndexesByType(int type) {
    return new ArrayList<Integer>();
  }
}
