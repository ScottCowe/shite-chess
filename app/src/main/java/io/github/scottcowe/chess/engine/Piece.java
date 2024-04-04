package io.github.scottcowe.chess.engine;

import java.util.List;
import java.util.ArrayList;

// I really hate this
// TODO: Find nicer way to do this
public enum Piece {
  WHITE_KING {
    @Override
    public char getAsChar() {
      return 'K';
    }

    @Override
    public Type getType() {
      return Type.KING;
    }
  },
  WHITE_QUEEN {
    @Override
    public char getAsChar() {
      return 'Q';
    }

    @Override
    public Type getType() {
      return Type.QUEEN;
    }
  },
  WHITE_ROOK {
    @Override
    public char getAsChar() {
      return 'R';
    }

    @Override
    public Type getType() {
      return Type.ROOK;
    }
  },
  WHITE_BISHOP {
    @Override
    public char getAsChar() {
      return 'B';
    }

    @Override
    public Type getType() {
      return Type.BISHOP;
    }
  },
  WHITE_KNIGHT {
    @Override
    public char getAsChar() {
      return 'N';
    }

    @Override
    public Type getType() {
      return Type.KNIGHT;
    }
  },
  WHITE_PAWN {
    @Override
    public char getAsChar() {
      return 'P';
    }

    @Override
    public Type getType() {
      return Type.PAWN;
    }
  },
  BLACK_KING {
    @Override
    public char getAsChar() {
      return 'k';
    }

    @Override
    public Type getType() {
      return Type.KING;
    }
  },
  BLACK_QUEEN {
    @Override
    public char getAsChar() {
      return 'q';
    }

    @Override
    public Type getType() {
      return Type.QUEEN;
    }
  },
  BLACK_ROOK {
    @Override
    public char getAsChar() {
      return 'r';
    }

    @Override
    public Type getType() {
      return Type.ROOK;
    }
  },
  BLACK_BISHOP {
    @Override
    public char getAsChar() {
      return 'b';
    }

    @Override
    public Type getType() {
      return Type.BISHOP;
    }
  },
  BLACK_KNIGHT {
    @Override
    public char getAsChar() {
      return 'n';
    }

    @Override
    public Type getType() {
      return Type.KNIGHT;
    }
  },
  BLACK_PAWN {
    @Override
    public char getAsChar() {
      return 'p';
    }

    @Override
    public Type getType() {
      return Type.PAWN;
    }
  },
  NONE;

  enum Type {
    KING,
    QUEEN,
    ROOK,
    BISHOP,
    KNIGHT,
    PAWN,
    NONE;
  }

  public char getAsChar() {
    return ' ';
  }

  public Type getType() {
    return Type.NONE;
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

  public static Piece getFromType(Type type, boolean isWhite) {
    switch (type) {
      case Type.KING:
        return isWhite ? WHITE_KING : BLACK_KING;
      case Type.QUEEN:
        return isWhite ? WHITE_QUEEN : BLACK_QUEEN;
      case Type.ROOK:
        return isWhite ? WHITE_ROOK : BLACK_ROOK;
      case Type.BISHOP:
        return isWhite ? WHITE_BISHOP : BLACK_BISHOP;
      case Type.KNIGHT:
        return isWhite ? WHITE_KNIGHT: BLACK_KNIGHT;
      case Type.PAWN:
        return isWhite ? WHITE_PAWN : BLACK_PAWN;
      default:
        return WHITE_QUEEN;
    }
  }
}
