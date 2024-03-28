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

  public boolean isWhite() {
    return Character.isUpperCase(this.getAsChar());
  }

  public static Piece getFromChar(char piece) {
    switch(piece) {
      case 'K':
        return WHITE_KING;
      case 'Q':
        return WHITE_QUEEN;
      case 'R':
        return WHITE_ROOK;
      case 'B':
        return WHITE_BISHOP;
      case 'N':
        return WHITE_KNIGHT;
      case 'P':
        return WHITE_PAWN;
      case 'k':
        return BLACK_KING;
      case 'q':
        return BLACK_QUEEN;
      case 'r':
        return BLACK_ROOK;
      case 'b':
        return BLACK_BISHOP;
      case 'n':
        return BLACK_KNIGHT;
      case 'p':
        return BLACK_PAWN;
      default:
        return NONE;
    }
  }
}
