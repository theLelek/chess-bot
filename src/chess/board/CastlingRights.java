package chess.board;

public record CastlingRights(boolean canWhiteCastleKingSide, boolean canBlackCastleKingSide,
                             boolean canWhiteCastleQueenSide, boolean canBlackCastleQueenSide) {

    public static CastlingRights initializeByFen(String castlingRights) {
        boolean canWhiteCastleKingSide = false;
        boolean canBlackCastleKingSide = false;
        boolean canWhiteCastleQueenSide = false;
        boolean canBlackCastleQueenSide = false;
        if (castlingRights.contains("K")) {
            canWhiteCastleKingSide = true;
        }
        if (castlingRights.contains("k")) {
            canBlackCastleKingSide = true;
        }
        if (castlingRights.contains("Q")) {
            canWhiteCastleQueenSide = true;
        }
        if (castlingRights.contains("q")) {
            canBlackCastleQueenSide = true;
        }
        return new CastlingRights(canWhiteCastleKingSide, canBlackCastleKingSide, canWhiteCastleQueenSide, canBlackCastleQueenSide);
    }
}
