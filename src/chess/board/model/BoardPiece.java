package chess.board.model;

import chess.board.PieceMoveRules;

public enum BoardPiece {
    WHITE_ROOK('R', PieceMoveRules.ROOK),
    WHITE_KNIGHT('N', PieceMoveRules.KNIGHT),
    WHITE_BISHOP('B',  PieceMoveRules.BISHOP),
    WHITE_QUEEN('Q', PieceMoveRules.QUEEN),
    WHITE_KING('K',  PieceMoveRules.KING),
    WHITE_PAWN('P', PieceMoveRules.PAWN),
    BLACK_ROOK('r', PieceMoveRules.ROOK),
    BLACK_KNIGHT('n', PieceMoveRules.KNIGHT),
    BLACK_BISHOP('b', PieceMoveRules.BISHOP),
    BLACK_QUEEN('q', PieceMoveRules.QUEEN),
    BLACK_KING('k', PieceMoveRules.KING),
    BLACK_PAWN('p', PieceMoveRules.PAWN),;

    private final char fortsythEdwardsNotation;
    private final PieceMoveRules rules;

    BoardPiece(char fen,  PieceMoveRules rules) {
        this.fortsythEdwardsNotation = fen;
        this.rules = rules;
    }

    public boolean isWhite() {
        String pieceName = this.toString();
        return pieceName.startsWith("WHITE");
    }

    public boolean isBlack() {
        return !isWhite();
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