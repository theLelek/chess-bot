package chess.board.model;

import chess.Color;

public class CastlingRights {

    private final boolean canCastleKingSide;
    private final boolean canCastleQueenSide;

    public CastlingRights(boolean canCastleKingSide, boolean canCastleQueenSide) {
        this.canCastleKingSide = canCastleKingSide;
        this.canCastleQueenSide = canCastleQueenSide;
    }

    public CastlingRights(CastlingRights castlingRights) {
        this.canCastleKingSide = castlingRights.canCastleKingSide;
        this.canCastleQueenSide = castlingRights.canCastleQueenSide;
    }

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

    public boolean canCastleKingSide() {
        return canCastleKingSide;
    }

    public boolean canCastleQueenSide() {
        return canCastleQueenSide;
    }
}
