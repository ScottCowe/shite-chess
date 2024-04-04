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

  public Piece[] getBoard() {
    return this.board;
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

  public Piece getPieceAtIndex(int index) {
    return this.board[index];
  }

  public static List<Integer> getPieceIndexesOfType(Piece piece, Piece[] board) {
    List<Integer> indexes = new ArrayList<Integer>();

    for (int i = 0; i < 64; i++) {
      if (board[i].equals(piece)) {
        indexes.add(i);
      }
    }

    return indexes;
  }

  public Position doMove(Move move) {
    Piece[] newBoard = move.applyToBoard();
    int newCastlingRights = this.castlingRights & move.getCastlingRightsMask(); 
    int newEnPassentTargetIndex = move.getEnPassentTargetIndex();
    int newHalfmoveClock = move.getType().equals(Move.MoveType.IRREVERSIBLE) ? 0 : this.halfmoveClock + 1;
    int newFullmoveCounter = this.fullmoveCounter + (this.whitesMove ? 0 : 1);

    return new Position(newBoard, !this.whitesMove, newCastlingRights, newEnPassentTargetIndex, newHalfmoveClock, newFullmoveCounter);
  }

  public Position turnSwitch() {
    return new Position(this.board, !this.whitesMove, this.castlingRights, this.enPassentTargetIndex, this.halfmoveClock, this.fullmoveCounter);
  }

  public static List<Move> removeIllegalMoves(List<Move> pseudoLegalMoves, Position pos) {
    List<Move> moves = pseudoLegalMoves;

    List<Move> toRemove = new ArrayList<Move>();

    for (Move move : moves) {
      if (!Position.isMoveLegal(move, pos)) {
        toRemove.add(move);
      }
    }

    moves.removeAll(toRemove);

    return moves;
  }

  public static boolean isSquareAttacked(int index, Position pos) {
    Position turnSwitched = pos.turnSwitch();
    List<Move> moves = Position.getAllPseudoLegalMoves(turnSwitched);

    for (Move move : moves) {
      if (move.getType().equals(Move.MoveType.CASTLING)) {
        continue; // Can be ignored as this does not attack any pieces
      }

      if (move.getToIndex() == index) {
        return true;
      }
    }

    return false;
  }

  public static boolean isMoveLegal(Move move, Position pos) {
    boolean isWhite = pos.isWhitesMove();

    if (move.getType().equals(Move.MoveType.CASTLING)) {
      int castling = move.getCastling();
      boolean kingside = castling == 8 || castling == 2;
      int offset = isWhite ? 0 : 56;
      
      List<Integer> squaresToCheck = new ArrayList<Integer>();

      int i = 4;
      while (i != (kingside ? 7 : 1)) {
        squaresToCheck.add(i + offset);
        i += (kingside ? 1 : -1);
      }

      for(Integer index : squaresToCheck) {
        if (Position.isSquareAttacked(index, pos)) {
          return false;
        }
      }

      return true;
    }

    Position newPos = pos.doMove(move);
    List<Move> moves = Position.getAllPseudoLegalMoves(newPos);

    Piece king = pos.isWhitesMove() ? Piece.WHITE_KING : Piece.BLACK_KING;

    for (Move m : moves) {
      if (m.getType().equals(Move.MoveType.CASTLING)) {
        continue; // can be ignored as castling is not an attacking move
      }

      int toIndex = m.getToIndex();

      if (newPos.getBoard()[toIndex].equals(king)) {
        return false;
      }
    }

    return true;
  }

  public static List<Move> getAllPseudoLegalMoves(Position pos) {
    List<Move> moves = new ArrayList<Move>();

    for (Piece pieceType : Piece.values()) {
      if (pieceType != Piece.NONE && pieceType.isWhite() == pos.isWhitesMove()) {
        List<Integer> indexes = Position.getPieceIndexesOfType(pieceType, pos.getBoard());
        for (Integer index : indexes) {
          moves.addAll(Position.getPseudoLegalMovesForPiece(index, pos));
        }
      }
    }

    return moves;
  }

  public static List<Move> getPseudoLegalMovesForPiece(int index, Position pos) {
    List<Move> moves = new ArrayList<Move>();

    Piece[] board = pos.getBoard();

    Piece.Type type = board[index].getType();
    boolean isWhite = pos.getPieceAtIndex(index).isWhite();

    switch(type) {
      case Piece.Type.KING:
        moves = Position.getKingMoves(index, isWhite, pos); 
        break;
      case Piece.Type.QUEEN:
        moves = Position.getQueenMoves(index, isWhite, pos); 
        break;
      case Piece.Type.ROOK:
        moves = Position.getRookMoves(index, isWhite, pos); 
        break;
      case Piece.Type.BISHOP:
        moves = Position.getBishopMoves(index, isWhite, pos); 
        break;
      case Piece.Type.KNIGHT:
        moves = Position.getKnightMoves(index, isWhite, pos); 
        break;
      case Piece.Type.PAWN:
        moves = Position.getPawnMoves(index, isWhite, pos); 
        break;
    }

    return moves;
  }

  // TODO: Clean up code
  public static List<Move> getKingMoves(int index, boolean isWhite, Position pos) {
    int castlingRights = pos.getCastlingRights();
    Piece[] board = pos.getBoard();

    List<Move> moves = new ArrayList<Move>();

    boolean whiteKingside = (castlingRights & 8) == 8;
    boolean whiteQueenside = (castlingRights & 4) == 4;
    boolean blackKingside = (castlingRights & 2) == 2;
    boolean blackQueenside = (castlingRights & 1) == 1;

    if (board[5].equals(Piece.NONE) && board[6].equals(Piece.NONE) && whiteKingside && isWhite) {
      moves.add(new Move(Move.MoveType.CASTLING, pos).setCastling(8)); 
    }

    if (board[3].equals(Piece.NONE) && board[2].equals(Piece.NONE) && board[1].equals(Piece.NONE) && whiteQueenside && isWhite) {
      moves.add(new Move(Move.MoveType.CASTLING, pos).setCastling(4)); 
    }

    if (board[61].equals(Piece.NONE) && board[62].equals(Piece.NONE) && blackKingside && !isWhite) {
      moves.add(new Move(Move.MoveType.CASTLING, pos).setCastling(2)); 
    }

    if (board[59].equals(Piece.NONE) && board[58].equals(Piece.NONE) && board[57].equals(Piece.NONE) && blackQueenside && !isWhite) {
      moves.add(new Move(Move.MoveType.CASTLING, pos).setCastling(1)); 
    }

    for (int i = 0; i < 4; i++) {
      int moveIndex = Position.getStraightMoveIndexInDirection(index, i);

      if (moveIndex == -1) {
        continue;
      }

      Piece pieceAtIndex = board[moveIndex];
      
      if (pieceAtIndex.equals(Piece.NONE)) {
        moves.add(new Move(Move.MoveType.STANDARD, pos).setFromIndex(index).setToIndex(moveIndex));
      }
      else if (pieceAtIndex.isWhite() != isWhite) {
        moves.add(new Move(Move.MoveType.IRREVERSIBLE, pos).setFromIndex(index).setToIndex(moveIndex));
      }

      moveIndex = Position.getDiagonalMoveIndexInDirection(index, i);

      if (moveIndex == -1) {
        continue;
      }

      pieceAtIndex = board[moveIndex];
      
      if (pieceAtIndex.equals(Piece.NONE)) {
        moves.add(new Move(Move.MoveType.STANDARD, pos).setFromIndex(index).setToIndex(moveIndex));
      }
      else if (pieceAtIndex.isWhite() != isWhite) {
        moves.add(new Move(Move.MoveType.IRREVERSIBLE, pos).setFromIndex(index).setToIndex(moveIndex));
      }
    }

    return moves;
  }

  public static List<Move> getQueenMoves(int index, boolean isWhite, Position pos) {
    Piece[] board = pos.getBoard();

    List<Move> moves = new ArrayList<Move>();
    
    for (int i = 0; i < 4; i++) {
      int moveIndex = Position.getStraightMoveIndexInDirection(index, i);

      while (moveIndex != -1) {
        if (board[moveIndex].equals(Piece.NONE)) {
          moves.add(new Move(Move.MoveType.STANDARD, pos).setFromIndex(index).setToIndex(moveIndex));
          moveIndex = Position.getStraightMoveIndexInDirection(moveIndex, i);
        }
        else if (board[moveIndex].isWhite() != isWhite) {
          moves.add(new Move(Move.MoveType.IRREVERSIBLE, pos).setFromIndex(index).setToIndex(moveIndex));
          moveIndex = -1;
        }
        else {
          moveIndex = -1;
        }
      }

      moveIndex = Position.getDiagonalMoveIndexInDirection(index, i);

      while (moveIndex != -1) {
        if (board[moveIndex].equals(Piece.NONE)) {
          moves.add(new Move(Move.MoveType.STANDARD, pos).setFromIndex(index).setToIndex(moveIndex));
          moveIndex = Position.getDiagonalMoveIndexInDirection(moveIndex, i);
        }
        else if (board[moveIndex].isWhite() != isWhite) {
          moves.add(new Move(Move.MoveType.IRREVERSIBLE, pos).setFromIndex(index).setToIndex(moveIndex));
          moveIndex = -1;
        }
        else {
          moveIndex = -1;
        }
      }
    }

    return moves;
  }

  public static List<Move> getRookMoves(int index, boolean isWhite, Position pos) {
    Piece[] board = pos.getBoard();

    List<Move> moves = new ArrayList<Move>();
    
    for (int i = 0; i < 4; i++) {
      int moveIndex = Position.getStraightMoveIndexInDirection(index, i);

      while (moveIndex != -1) {
        if (board[moveIndex].equals(Piece.NONE)) {
          moves.add(new Move(Move.MoveType.STANDARD, pos).setFromIndex(index).setToIndex(moveIndex));
          moveIndex = Position.getStraightMoveIndexInDirection(moveIndex, i);
        }
        else if (board[moveIndex].isWhite() != isWhite) {
          moves.add(new Move(Move.MoveType.IRREVERSIBLE, pos).setFromIndex(index).setToIndex(moveIndex));
          moveIndex = -1;
        }
        else {
          moveIndex = -1;
        }
      }
    }

    return moves;
  }

  public static List<Move> getBishopMoves(int index, boolean isWhite, Position pos) {
    Piece[] board = pos.getBoard();

    List<Move> moves = new ArrayList<Move>();
    
    for (int i = 0; i < 4; i++) {
      int moveIndex = Position.getDiagonalMoveIndexInDirection(index, i);

      while (moveIndex != -1) {
        if (board[moveIndex].equals(Piece.NONE)) {
          moves.add(new Move(Move.MoveType.STANDARD, pos).setFromIndex(index).setToIndex(moveIndex));
          moveIndex = Position.getDiagonalMoveIndexInDirection(moveIndex, i);
        }
        else if (board[moveIndex].isWhite() != isWhite) {
          moves.add(new Move(Move.MoveType.IRREVERSIBLE, pos).setFromIndex(index).setToIndex(moveIndex));
          moveIndex = -1;
        }
        else {
          moveIndex = -1;
        }
      }
    }

    return moves;
  }

  public static List<Move> getKnightMoves(int index, boolean isWhite, Position pos) {
    Piece[] board = pos.getBoard();

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

      if (board[newIndex].isWhite() == isWhite && !board[newIndex].equals(Piece.NONE)) {
        continue;
      }

      if (board[newIndex].equals(Piece.NONE)) {
        moves.add(new Move(Move.MoveType.STANDARD, pos).setFromIndex(index).setToIndex(newIndex));
        continue;
      }

      moves.add(new Move(Move.MoveType.IRREVERSIBLE, pos).setFromIndex(index).setToIndex(newIndex));
    }

    return moves;
  }

  // TODO: You forgot to implement promotion, you fucking idiot
  public static List<Move> getPawnMoves(int index, boolean isWhite, Position pos) {
    int enPassentTargetIndex = pos.getEnPassentTargetIndex();
    Piece[] board = pos.getBoard();
    List<Move> moves = new ArrayList<Move>();

    int direction = isWhite ? 8 : -8;

    if (board[index + direction].equals(Piece.NONE)) {
      moves.add(new Move(Move.MoveType.IRREVERSIBLE, pos).setFromIndex(index).setToIndex(index + direction));
    }

    int startingRank = isWhite ? 1 : 6;

    boolean onStartingRank = (index - startingRank * 8) >= 0 && (index - startingRank * 8) <= 7;

    if (onStartingRank && board[index + 2 * direction].equals(Piece.NONE) && board[index + direction].equals(Piece.NONE)) {
      moves.add(new Move(Move.MoveType.IRREVERSIBLE, pos).setFromIndex(index).setToIndex(index + 2 * direction));
    }

    int endIndex = index + direction + 1;
    boolean goingOverEdge = (index + 1) % 8 == 0 && endIndex % 8 == 0;
    if (board[endIndex].isWhite() != isWhite && !board[endIndex].equals(Piece.NONE) && !goingOverEdge) {
      moves.add(new Move(Move.MoveType.IRREVERSIBLE, pos).setFromIndex(index).setToIndex(endIndex));
    }
    else if (endIndex == enPassentTargetIndex && !goingOverEdge) {
      moves.add(new Move(Move.MoveType.IRREVERSIBLE, pos).setFromIndex(index).setToIndex(enPassentTargetIndex).setEnPassent());
    }

    endIndex = index + direction - 1;
    goingOverEdge = index % 8 == 0 && (endIndex + 1) % 8 == 0;
    if (board[endIndex].isWhite() != isWhite && !board[endIndex].equals(Piece.NONE) && !goingOverEdge) {
      moves.add(new Move(Move.MoveType.IRREVERSIBLE, pos).setFromIndex(index).setToIndex(endIndex));
    }
    else if (endIndex == enPassentTargetIndex && !goingOverEdge) {
      moves.add(new Move(Move.MoveType.IRREVERSIBLE, pos).setFromIndex(index).setToIndex(enPassentTargetIndex).setEnPassent());
    }

    return moves;
  }

  // Direction - 0 for towards 8th, 1 for towards h, 2 for towards 1st, 3 for towards a
  private static int getStraightMoveIndexInDirection(int index, int direction) {
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
  private static int getDiagonalMoveIndexInDirection(int index, int direction) {
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

    if (!this.board.equals(pos.getBoard())) {
      return false;
    }

    if (this.whitesMove != pos.isWhitesMove()) {
      return false;
    }

    if (this.castlingRights != pos.getCastlingRights()) {
      return false;
    }

    if (this.enPassentTargetIndex != pos.getEnPassentTargetIndex()) {
      return false;
    }

    if (this.halfmoveClock != pos.getHalfmoveClock()) {
      return false;
    }

    if (this.fullmoveCounter != this.getFullmoveCounter()) {
      return false;
    }

    return true;
  }
}
