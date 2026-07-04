package dev.lelek.chess;

import dev.lelek.chess.board.BitboardIndexProvider;

public enum BoardPiece implements BitboardIndexProvider {

    WHITE_PAWN('P', 0),
    WHITE_KNIGHT('N', 1),
    WHITE_BISHOP('B', 2),
    WHITE_ROOK('R', 3),
    WHITE_QUEEN('Q', 4),
    WHITE_KING('K', 5),

    BLACK_PAWN('p', 6),
    BLACK_KNIGHT('n', 7),
    BLACK_BISHOP('b', 8),
    BLACK_ROOK('r', 9),
    BLACK_QUEEN('q', 10),
    BLACK_KING('k', 11);

    private final char fen;
    private final int bitboardIndex;

    BoardPiece(char fen, int bitboardIndex) {
        this.fen = fen;
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

    public boolean hasColor(Color color) {
        return color == Color.WHITE ? isWhite() : isBlack();
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