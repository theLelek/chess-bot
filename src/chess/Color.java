package chess;

import chess.Move.CastlingMove;
import chess.board.model.PieceBitboard;


public enum Color {

    WHITE(
            7, 0, 6, -1,
            new CastlingMove(4, 7, 6, 7),
            new CastlingMove(4, 7, 2, 7),
            PieceBitboard.WHITE_PIECES,
            PieceBitboard.BLACK_PIECES
    ),

    BLACK(
            0, 7, 1, 1,
            new CastlingMove(4, 0, 6, 0),
            new CastlingMove(4, 0, 2, 0),
            PieceBitboard.BLACK_PIECES,
            PieceBitboard.WHITE_PIECES
    );

    private final int homeRank;
    private final int backRank;
    private final int pawnStartingRow;
    private final int movingDirection;
    private final CastlingMove castlingMoveKingSide;
    private final CastlingMove castlingMoveQueenSide;
    private final PieceBitboard ownPieceBitboard;
    private final PieceBitboard opponentPieceBitboard;

    Color(int homeRank, int backRank, int pawnStartingRow, int movingDirection, CastlingMove castlingMoveKingSide, CastlingMove castlingMoveQueenSide, PieceBitboard ownPieceBitboard, PieceBitboard opponentPieceBitboard) {
        this.homeRank = homeRank;
        this.backRank = backRank;
        this.pawnStartingRow = pawnStartingRow;
        this.movingDirection = movingDirection;
        this.castlingMoveKingSide = castlingMoveKingSide;
        this.castlingMoveQueenSide = castlingMoveQueenSide;
        this.ownPieceBitboard = ownPieceBitboard;
        this.opponentPieceBitboard = opponentPieceBitboard;
    }

    public CastlingMove getCastlingMoveKingSide() {
        return castlingMoveKingSide;
    }

    public CastlingMove getCastlingMoveQueenSide() {
        return castlingMoveQueenSide;
    }

    public int getHomeRank() {
        return homeRank;
    }

    public int getBackRank() {
        return backRank;
    }

    public int getMovingDirection() {
        return movingDirection;
    }

    public PieceBitboard getOpponentPieceBitboard() {
        return opponentPieceBitboard;
    }

    public PieceBitboard getOwnPieceBitboard() {
        return ownPieceBitboard;
    }

    public int getPawnStartingRow() {
        return pawnStartingRow;
    }
}