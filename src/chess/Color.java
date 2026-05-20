package chess;

import chess.Move.CastlingMove;
import chess.board.model.PieceBitboard;


public enum Color {

    WHITE(
            7, 0, 6, -1,
            new CastlingMove(4, 7, 6, 7),
            new CastlingMove(4, 7, 2, 7),
            PieceBitboard.WHITE_PIECES,
            PieceBitboard.BLACK_PIECES,
            PieceBitboard.WHITE_PAWNS,
            PieceBitboard.WHITE_KNIGHTS,
            PieceBitboard.WHITE_BISHOPS,
            PieceBitboard.WHITE_ROOKS,
            PieceBitboard.WHITE_QUEEN,
            PieceBitboard.WHITE_KING

    ),

    BLACK(
            0, 7, 1, 1,
            new CastlingMove(4, 0, 6, 0),
            new CastlingMove(4, 0, 2, 0),
            PieceBitboard.BLACK_PIECES,
            PieceBitboard.WHITE_PIECES,
            PieceBitboard.BLACK_PAWNS,
            PieceBitboard.BLACK_KNIGHTS,
            PieceBitboard.BLACK_BISHOPS,
            PieceBitboard.BLACK_ROOKS,
            PieceBitboard.BLACK_QUEEN,
            PieceBitboard.BLACK_KING
    );

    private final int homeRank;
    private final int backRank;
    private final int pawnStartingRow;
    private final int movingDirection;

    private final CastlingMove castlingMoveKingSide;
    private final CastlingMove castlingMoveQueenSide;

    private final PieceBitboard ownPieceBitboard;
    private final PieceBitboard opponentPieceBitboard;
    private final PieceBitboard pawnsBitboard;
    private final PieceBitboard knightsBitboard;
    private final PieceBitboard bishopsBitboard;
    private final PieceBitboard rooksBitboard;
    private final PieceBitboard queenBitboard;
    private final PieceBitboard kingBitboard;

    Color(int homeRank, int backRank, int pawnStartingRow, int movingDirection, CastlingMove castlingMoveKingSide, CastlingMove castlingMoveQueenSide, PieceBitboard ownPieceBitboard, PieceBitboard opponentPieceBitboard, PieceBitboard pawnsBitboard, PieceBitboard knightsBitboard, PieceBitboard bishopsBitboard, PieceBitboard rooksBitboard, PieceBitboard queenBitboard, PieceBitboard kingBitboard) {
        this.homeRank = homeRank;
        this.backRank = backRank;
        this.pawnStartingRow = pawnStartingRow;
        this.movingDirection = movingDirection;
        this.castlingMoveKingSide = castlingMoveKingSide;
        this.castlingMoveQueenSide = castlingMoveQueenSide;
        this.ownPieceBitboard = ownPieceBitboard;
        this.opponentPieceBitboard = opponentPieceBitboard;
        this.pawnsBitboard = pawnsBitboard;
        this.knightsBitboard = knightsBitboard;
        this.bishopsBitboard = bishopsBitboard;
        this.rooksBitboard = rooksBitboard;
        this.queenBitboard = queenBitboard;
        this.kingBitboard = kingBitboard;
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

    public PieceBitboard getPawnsBitboard() {
        return pawnsBitboard;
    }

    public PieceBitboard getKnightsBitboard() {
        return knightsBitboard;
    }

    public PieceBitboard getBishopsBitboard() {
        return bishopsBitboard;
    }

    public PieceBitboard getRooksBitboard() {
        return rooksBitboard;
    }

    public PieceBitboard getQueenBitboard() {
        return queenBitboard;
    }

    public PieceBitboard getKingBitboard() {
        return kingBitboard;
    }
}