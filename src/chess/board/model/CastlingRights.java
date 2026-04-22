package chess.board.model;

import java.util.Objects;

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

    public static CastlingRights fromFen(String fen, boolean isWhite) {
        boolean kingSide;
        boolean queenSide;
        if (isWhite) {
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CastlingRights that = (CastlingRights) o;
        return canCastleKingSide == that.canCastleKingSide && canCastleQueenSide == that.canCastleQueenSide;
    }

    @Override
    public int hashCode() {
        return Objects.hash(canCastleKingSide, canCastleQueenSide);
    }
}
