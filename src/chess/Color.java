package chess;

import chess.Move.CastlingMove;

public enum Color {

    WHITE(
            7, 0,
            new CastlingMove(4, 7, 6, 7), // kingside
            new CastlingMove(4, 7, 2, 7)  // queenside
    ),

    BLACK(
            0, 7,
            new CastlingMove(4, 0, 6, 0), // kingside
            new CastlingMove(4, 0, 2, 0)  // queenside
    );

    private final int startingRow;
    private final int promotionRow;
    private final CastlingMove castlingMoveKingSide;
    private final CastlingMove castlingMoveQueenSide;

    Color(int startingRow, int promotionRow, CastlingMove castlingMoveKingSide, CastlingMove castlingMoveQueenSide) {
        this.startingRow = startingRow;
        this.promotionRow = promotionRow;
        this.castlingMoveKingSide = castlingMoveKingSide;
        this.castlingMoveQueenSide = castlingMoveQueenSide;
    }

    public CastlingMove getCastlingMoveKingSide() {
        return castlingMoveKingSide;
    }

    public CastlingMove getCastlingMoveQueenSide() {
        return castlingMoveQueenSide;
    }

    public int getStartingRow() {
        return startingRow;
    }

    public int getPromotionRow() {
        return promotionRow;
    }
}
