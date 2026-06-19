package dev.lelek.chess.board;

import dev.lelek.chess.search.PieceMoveRules;

public enum BoardPiece implements BitboardIndexProvider {
    WHITE_PAWN('P', PieceMoveRules.WHITE_PAWN, 0),
    WHITE_KNIGHT('N', PieceMoveRules.KNIGHT, 1),
    WHITE_BISHOP('B', PieceMoveRules.BISHOP, 2),
    WHITE_ROOK('R', PieceMoveRules.ROOK, 3),
    WHITE_QUEEN('Q', PieceMoveRules.QUEEN, 4),
    WHITE_KING('K', PieceMoveRules.KING, 5),

    BLACK_PAWN('p', PieceMoveRules.BLACK_PAWN, 6),
    BLACK_KNIGHT('n', PieceMoveRules.KNIGHT, 7),
    BLACK_BISHOP('b', PieceMoveRules.BISHOP, 8),
    BLACK_ROOK('r', PieceMoveRules.ROOK, 9),
    BLACK_QUEEN('q', PieceMoveRules.QUEEN, 10),
    BLACK_KING('k', PieceMoveRules.KING, 11);

    private final char fen;
    private final PieceMoveRules moveRules;
    private final int bitboardIndex;

    BoardPiece(char fen, PieceMoveRules moveRules, int bitboardIndex) {
        this.fen = fen;
        this.moveRules = moveRules;
        this.bitboardIndex = bitboardIndex;
    }

    public static BoardPiece fromFen(char fen) {
        for (BoardPiece piece : BoardPiece.values()) {
            if (piece.fen == fen) {
                return piece;
            }
        }
        throw new IllegalArgumentException("Invalid fen for board piece: " + fen);
    }

    public boolean hasSameColor(BoardPiece currentPiece) {
        if (currentPiece == null) {
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

    public boolean isWhite() {
        String pieceName = this.toString();
        return pieceName.startsWith("WHITE");
    }

    public boolean isBlack() {
        return ! isWhite();
    }

    public char getFen() {
        return fen;
    }

    public PieceMoveRules getMoveRules() {
        return moveRules;
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

    public int getBitboardIndex() {
        return bitboardIndex;
    }
}