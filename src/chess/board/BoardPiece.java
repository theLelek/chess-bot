package chess.board;

public enum BoardPiece {
    WHITE_ROOK('R'), WHITE_KNIGHT('N'), WHITE_BISHOP('B'), WHITE_QUEEN('Q'), WHITE_KING('K'), WHITE_PAWN('P'),
    BLACK_ROOK('r'), BLACK_KNIGHT('n'),  BLACK_BISHOP('b'), BLACK_QUEEN('q'), BLACK_KING('k'), BLACK_PAWN('p');

    private final char fortsythEdwardsNotation;

    BoardPiece(char fen) {
        this.fortsythEdwardsNotation = fen;
    }

    public boolean isWhite() {
        String pieceName = this.toString();
        if (pieceName.startsWith("WHITE")) {
            return true;
        }
        return false;
    }


    public boolean isBlack() {
        return ! isWhite();
    }

    public char getFortsythEdwardsNotation() {
        return fortsythEdwardsNotation;
    }

    public static BoardPiece getByFen(char fen) {
        for (BoardPiece piece : BoardPiece.values()) {
            if (piece.fortsythEdwardsNotation == fen) {
                return piece;
            }
        }
        return null;
    }
}