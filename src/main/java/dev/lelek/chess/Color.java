package dev.lelek.chess;

import dev.lelek.chess.Move.CastlingMove;
import dev.lelek.chess.board.OccupancyBitboard;


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
            BoardPiece.WHITE_KING,

            BoardPiece.BLACK_PAWN,
            BoardPiece.BLACK_KNIGHT,
            BoardPiece.BLACK_BISHOP,
            BoardPiece.BLACK_ROOK,
            BoardPiece.BLACK_QUEEN,
            BoardPiece.BLACK_KING
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
            BoardPiece.BLACK_KING,

            BoardPiece.WHITE_PAWN,
            BoardPiece.WHITE_KNIGHT,
            BoardPiece.WHITE_BISHOP,
            BoardPiece.WHITE_ROOK,
            BoardPiece.WHITE_QUEEN,
            BoardPiece.WHITE_KING
    );

    private final int homeRank;
    private final int backRank;
    private final int pawnStartingRow;
    private final int movingDirection;

    private final CastlingMove castlingMoveKingSide;
    private final CastlingMove castlingMoveQueenSide;

    private final OccupancyBitboard ownOccupancyBitboard;
    private final OccupancyBitboard opponentOccupancyBitboard;

    private final BoardPiece pawn;
    private final BoardPiece knight;
    private final BoardPiece bishop;
    private final BoardPiece rook;
    private final BoardPiece queen;
    private final BoardPiece king;

    private final BoardPiece opponentPawn;
    private final BoardPiece opponentKnight;
    private final BoardPiece opponentBishop;
    private final BoardPiece opponentRook;
    private final BoardPiece opponentQueen;
    private final BoardPiece opponentKing;


    Color(int homeRank, int backRank, int pawnStartingRow, int movingDirection, CastlingMove castlingMoveKingSide, CastlingMove castlingMoveQueenSide, OccupancyBitboard ownOccupancyBitboard, OccupancyBitboard opponentOccupancyBitboard, BoardPiece pawn, BoardPiece knight, BoardPiece bishop, BoardPiece rook, BoardPiece queen, BoardPiece king, BoardPiece opponentPawn, BoardPiece opponentKnight, BoardPiece opponentBishop, BoardPiece opponentRook, BoardPiece opponentQueen, BoardPiece opponentKing) {
        this.homeRank = homeRank;
        this.backRank = backRank;
        this.pawnStartingRow = pawnStartingRow;
        this.movingDirection = movingDirection;
        this.castlingMoveKingSide = castlingMoveKingSide;
        this.castlingMoveQueenSide = castlingMoveQueenSide;
        this.ownOccupancyBitboard = ownOccupancyBitboard;
        this.opponentOccupancyBitboard = opponentOccupancyBitboard;
        this.pawn = pawn;
        this.knight = knight;
        this.bishop = bishop;
        this.rook = rook;
        this.queen = queen;
        this.king = king;
        this.opponentPawn = opponentPawn;
        this.opponentKnight = opponentKnight;
        this.opponentBishop = opponentBishop;
        this.opponentRook = opponentRook;
        this.opponentQueen = opponentQueen;
        this.opponentKing = opponentKing;
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

    public BoardPiece getPawn() {
        return pawn;
    }

    public BoardPiece getKnight() {
        return knight;
    }

    public BoardPiece getBishop() {
        return bishop;
    }

    public BoardPiece getRook() {
        return rook;
    }

    public BoardPiece getQueen() {
        return queen;
    }

    public BoardPiece getKing() {
        return king;
    }

    public OccupancyBitboard getOwnOccupancyBitboard() {
        return ownOccupancyBitboard;
    }

    public OccupancyBitboard getOpponentOccupancyBitboard() {
        return opponentOccupancyBitboard;
    }

    public BoardPiece getOpponentPawn() {
        return opponentPawn;
    }

    public BoardPiece getOpponentKnight() {
        return opponentKnight;
    }

    public BoardPiece getOpponentBishop() {
        return opponentBishop;
    }

    public BoardPiece getOpponentRook() {
        return opponentRook;
    }

    public BoardPiece getOpponentQueen() {
        return opponentQueen;
    }

    public BoardPiece getOpponentKing() {
        return opponentKing;
    }
}