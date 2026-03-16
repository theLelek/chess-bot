package chess.board;

public enum BoardPiece {
    WHITE_ROOK('R'), WHITE_KNIGHT('N'), WHITE_BISHOP('B'), WHITE_QUEEN('Q'), WHITE_KING('K'),
    BLACK_ROOK('r'), BLACK_KNIGHT('k'),  BLACK_BISHOP('b'), BLACK_QUEEN('q'), BLACK_KING('k');

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
}
