package chess.board.model;

import chess.board.PieceMoveRules;

public enum BoardPiece {
    WHITE_ROOK('R', PieceMoveRules.ROOK),
    WHITE_KNIGHT('N', PieceMoveRules.KNIGHT),
    WHITE_BISHOP('B',  PieceMoveRules.BISHOP),
    WHITE_QUEEN('Q', PieceMoveRules.QUEEN),
    WHITE_KING('K',  PieceMoveRules.KING),
    WHITE_PAWN('P', PieceMoveRules.WHITE_PAWN),

    BLACK_ROOK('r', PieceMoveRules.ROOK),
    BLACK_KNIGHT('n', PieceMoveRules.KNIGHT),
    BLACK_BISHOP('b', PieceMoveRules.BISHOP),
    BLACK_QUEEN('q', PieceMoveRules.QUEEN),
    BLACK_KING('k', PieceMoveRules.KING),
    BLACK_PAWN('p', PieceMoveRules.BLACK_PAWN);

    private final char fen;
    private final PieceMoveRules moveRules;

    BoardPiece(char fen, PieceMoveRules moveRules) {
        this.fen = fen;
        this.moveRules = moveRules;
    }

    public char getFen() {
        return fen;
    }

    public PieceMoveRules getMoveRules() {
        return moveRules;
    }

    public boolean isWhite() {
        String pieceName = this.toString();
        return pieceName.startsWith("WHITE");
    }

    public boolean isBlack() {
        return !isWhite();
    }

    public boolean hasSameColor(BoardPiece currentPiece) {
        if(currentPiece == null) {
            throw new IllegalArgumentException();
        }
        return this.isWhite() == currentPiece.isWhite();
    }

    public boolean hasOppositeColor(BoardPiece currentPiece) {
        if(currentPiece == null) {
            return false;
        }
        return this.isBlack() == currentPiece.isWhite();
    }

    public boolean isRook() {
        return fen == 'r' || fen == 'R';
    }

    public boolean isKnight() {
        return fen == 'n' || fen == 'N';
    }

    public boolean isBishop() {
        return fen == 'b' || fen == 'B';
    }

    public boolean isQueen() {
        return fen == 'q' || fen == 'Q';
    }

    public boolean isKing() {
        return fen == 'k' || fen == 'K';
    }

    public boolean isPawn() {
        return fen == 'p' || fen == 'P';
    }

    public int getHomeRank() {
        return (isWhite()) ? Board.SIZE - 1 : 0;
    }

    public static int getHomeRank(boolean isWhiteToMove) {
        return (isWhiteToMove) ? Board.SIZE - 1 : 0;
    }

    public int getBackRank() {
        return (isWhite()) ? 0: Board.SIZE - 1;
    }

    public static int getBackRank(boolean isWhiteToMove) {
        return (isWhiteToMove) ? 0 : Board.SIZE - 1;
    }

    public static BoardPiece getByFen(char fen) {
        for (BoardPiece piece : BoardPiece.values()) {
            if (piece.fen == fen) {
                return piece;
            }
        }
        return null;
    }
}