package chess;

import chess.Move.CastlingMove;
import chess.board.BoardPiece;
import chess.board.OccupancyBitboard;


public enum Color {

    WHITE(
            7, 0, 6, -1,
            new CastlingMove(4, 7, 6, 7),
            new CastlingMove(4, 7, 2, 7),
            OccupancyBitboard.WHITE_PIECES,
            OccupancyBitboard.BLACK_PIECES,
            BoardPiece.WHITE_PAWN,
            BoardPiece.WHITE_KNIGHT,
            BoardPiece.WHITE_BISHOP,
            BoardPiece.WHITE_ROOK,
            BoardPiece.WHITE_QUEEN,
            BoardPiece.WHITE_KING

    ),

    BLACK(
            0, 7, 1, 1,
            new CastlingMove(4, 0, 6, 0),
            new CastlingMove(4, 0, 2, 0),
            OccupancyBitboard.BLACK_PIECES,
            OccupancyBitboard.WHITE_PIECES,
            BoardPiece.BLACK_PAWN,
            BoardPiece.BLACK_KNIGHT,
            BoardPiece.BLACK_BISHOP,
            BoardPiece.BLACK_ROOK,
            BoardPiece.BLACK_QUEEN,
            BoardPiece.BLACK_KING
    );

    private final int homeRank;
    private final int backRank;
    private final int pawnStartingRow;
    private final int movingDirection;

    private final CastlingMove castlingMoveKingSide;
    private final CastlingMove castlingMoveQueenSide;

    private final OccupancyBitboard ownOccupancyBitboard;
    private final OccupancyBitboard opponentOccupancyBitboard;

    private final BoardPiece pawnBoardPiece;
    private final BoardPiece knightBoardPiece;
    private final BoardPiece bishopBoardPiece;
    private final BoardPiece rookBoardPiece;
    private final BoardPiece queenBoardPiece;
    private final BoardPiece kingBoardPiece;

    Color(int homeRank, int backRank, int pawnStartingRow, int movingDirection, CastlingMove castlingMoveKingSide, CastlingMove castlingMoveQueenSide, OccupancyBitboard ownOccupancyBitboard, OccupancyBitboard opponentOccupancyBitboard, BoardPiece pawnBoardPiece, BoardPiece knightBoardPiece, BoardPiece bishopBoardPiece, BoardPiece rookBoardPiece, BoardPiece queenBoardPiece, BoardPiece kingBoardPiece) {
        this.homeRank = homeRank;
        this.backRank = backRank;
        this.pawnStartingRow = pawnStartingRow;
        this.movingDirection = movingDirection;
        this.castlingMoveKingSide = castlingMoveKingSide;
        this.castlingMoveQueenSide = castlingMoveQueenSide;
        this.ownOccupancyBitboard = ownOccupancyBitboard;
        this.opponentOccupancyBitboard = opponentOccupancyBitboard;
        this.pawnBoardPiece = pawnBoardPiece;
        this.knightBoardPiece = knightBoardPiece;
        this.bishopBoardPiece = bishopBoardPiece;
        this.rookBoardPiece = rookBoardPiece;
        this.queenBoardPiece = queenBoardPiece;
        this.kingBoardPiece = kingBoardPiece;
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

    public OccupancyBitboard getOpponentPieceBitboard() {
        return opponentOccupancyBitboard;
    }

    public OccupancyBitboard getOwnPieceBitboard() {
        return ownOccupancyBitboard;
    }

    public int getPawnStartingRow() {
        return pawnStartingRow;
    }

    public BoardPiece getPawnBoardPiece() {
        return pawnBoardPiece;
    }

    public BoardPiece getKnightBoardPiece() {
        return knightBoardPiece;
    }

    public BoardPiece getBishopBoardPiece() {
        return bishopBoardPiece;
    }

    public BoardPiece getRookBoardPiece() {
        return rookBoardPiece;
    }

    public BoardPiece getQueenBoardPiece() {
        return queenBoardPiece;
    }

    public BoardPiece getKingBoardPiece() {
        return kingBoardPiece;
    }
}