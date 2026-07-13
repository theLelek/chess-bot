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

    public Color getColor() {
        return switch (this) {
            case WHITE_PAWN,
                 WHITE_KNIGHT,
                 WHITE_BISHOP,
                 WHITE_ROOK,
                 WHITE_QUEEN,
                 WHITE_KING -> Color.WHITE;

            case BLACK_PAWN,
                 BLACK_KNIGHT,
                 BLACK_BISHOP,
                 BLACK_ROOK,
                 BLACK_QUEEN,
                 BLACK_KING -> Color.BLACK;
        };
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

    public char toFen() {
        return fen;
    }

    public boolean isWhite() {
        return getColor() == Color.WHITE;
    }

    public boolean isBlack() {
        return ! isWhite();
    }

    public boolean isRook() {
        return this == WHITE_ROOK || this == BLACK_ROOK;
    }

    public boolean isKnight() {
        return this == WHITE_KNIGHT || this == BLACK_KNIGHT;
    }

    public boolean isBishop() {
        return this == WHITE_BISHOP || this == BLACK_BISHOP;
    }

    public boolean isQueen() {
        return this == WHITE_QUEEN || this == BLACK_QUEEN;
    }

    public boolean isKing() {
        return this == WHITE_KING || this == BLACK_KING;
    }

    public boolean isPawn() {
        return this == WHITE_PAWN || this == BLACK_PAWN;
    }

    public int getBitboardIndex() {
        return bitboardIndex;
    }
}