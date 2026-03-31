package chess.board.model;

import chess.Color;

public record CastlingRights(boolean canCastleKingSide, boolean canCastleQueenSide) {

    public static CastlingRights fromFen(String fen, Color color) {
        boolean kingSide;
        boolean queenSide;
        if (color == Color.WHITE) {
            kingSide = fen.contains("K");
            queenSide = fen.contains("Q");
        } else {
            kingSide = fen.contains("k");
            queenSide = fen.contains("q");
        }
        return new CastlingRights(kingSide, queenSide);
    }
}
