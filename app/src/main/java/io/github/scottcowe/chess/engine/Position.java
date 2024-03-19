package io.github.scottcowe.chess.engine;

public class Position {
  private Board board;
  private boolean whitesMove;
  private int castling; // KQkq - 1111 - 15
  private int enPassentTarget;
  private int halfmoveClock;
  private int fullmoveCount;

  public Position(Board board, boolean whitesMove, int castling, int enPassentTarget, int halfmoveClock, int fullmoveCount) {
    this.board = board;
    this.whitesMove = whitesMove;
    this.castling = castling;
    this.enPassentTarget = enPassentTarget;
    this.halfmoveClock = halfmoveClock;
    this.fullmoveCount = fullmoveCount;
  }

  public void doMove(Move move) {
    // Check if move is legal
    //  If king would be in check after move played then illegal
   
    // Move piece
    //  If en passent then capture pawn
    //  If castling then move king and rook

    // Check if move causes checkmate or stalemate
  }

  public List<Move> getAllPseudoLegalMoves(boolean white) {
    List<Move> moves = new ArrayList<Move>();

    return moves;
  }

  // Returns a List of pseudo-legal pawn moves, given a pawn was at the given index
  public List<Move> getPseudoLegalPawnMoves(int index, int enPassentTargetIndex) {
    List<Move> moves = new ArrayList<Move>();

    int piece = this.getPiece(index);
    boolean isWhite = (piece & 8) == 0;

    // Square straight ahead is pseudo-legal, assuming no piece is there
    // cannot go off edge of board as it would promote before then
    if (this.getPiece(index + 8) == 0) {
      moves.add(new Move(index, index + 8));
    }

    // If on starting row then two spaces ahead is legal, assuming squares are unoccupied

    // Diagonal left and right are legal, assuming that pawn is not on left or right edge
    // Any piece of left edge if index % 8 == 0, right edge if index+1 % 8 == 0
    // TODO: Ensure captured piece is not own piece
    if (index % 8 != 0 && (this.getPiece(index + 7) != 0 || index + 7 == enPassentTargetIndex)) {
      moves.add(new Move(index, index + 7));
    }
    if ((index + 1) % 8 != 0 && (this.getPiece(index + 9) != 0 || index + 7 == enPassentTargetIndex)) {
      moves.add(new Move(index, index + 9));
    }

    return moves;
  }
}
