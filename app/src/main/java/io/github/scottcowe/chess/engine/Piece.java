package io.github.scottcowe.chess.engine;

import java.util.List;
import java.util.ArrayList;

public enum Piece {
  WHITE_KING {
    @Override
    public char getAsChar() {
      return 'K';
    }
  },
  WHITE_QUEEN {
    @Override
    public char getAsChar() {
      return 'Q';
    }
  },
  WHITE_ROOK {
    @Override
    public char getAsChar() {
      return 'R';
    }
  },
  WHITE_BISHOP {
    @Override
    public char getAsChar() {
      return 'B';
    }
  },
  WHITE_KNIGHT {
    @Override
    public char getAsChar() {
      return 'N';
    }
  },
  WHITE_PAWN {
    @Override
    public char getAsChar() {
      return 'P';
    }
  },
  BLACK_KING {
    @Override
    public char getAsChar() {
      return 'k';
    }
  },
  BLACK_QUEEN {
    @Override
    public char getAsChar() {
      return 'q';
    }
  },
  BLACK_ROOK {
    @Override
    public char getAsChar() {
      return 'r';
    }
  },
  BLACK_BISHOP {
    @Override
    public char getAsChar() {
      return 'b';
    }
  },
  BLACK_KNIGHT {
    @Override
    public char getAsChar() {
      return 'n';
    }
  },
  BLACK_PAWN {
    @Override
    public char getAsChar() {
      return 'p';
    }
  },
  NONE;

  public char getAsChar() {
    return ' ';
  }

  public Piece getFromChar(char piece) {
    switch(piece) {
      case 'k':
        return WHITE_KING;
        break;
      case 'q':
        return WHITE_QUEEN;
        break;
      case 'r':
        return WHITE_ROOK;
        break;
      case 'b':
        return WHITE_BISHOP;
        break;
      case 'n':
        return WHITE_KNIGHT;
        break;
      case 'p':
        return WHITE_PAWN;
        break;
      case 'K':
        return BLACK_KING;
        break;
      case 'Q':
        return BLACK_QUEEN;
        break;
      case 'R':
        return BLACK_ROOK;
        break;
      case 'B':
        return BLACK_BISHOP;
        break;
      case 'N':
        return BLACK_KNIGHT;
        break;
      case 'P':
        return BLACK_PAWN;
        break;
      default:
        return NONE;
        break;
    }
  }
}
