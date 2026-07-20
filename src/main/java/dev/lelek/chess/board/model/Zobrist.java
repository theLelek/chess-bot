package dev.lelek.chess.board.model;

import dev.lelek.chess.BoardPiece;
import dev.lelek.chess.BoardPosition;
import dev.lelek.chess.board.OccupancyBitboard;

import java.util.SplittableRandom;

final class Zobrist {

    static final long SEED = 0;

    static final long[][] Piece_SQUARE_KEYS = new long[BoardPiece.values().length][Board.SIZE * Board.SIZE];
    static final long[] CASTLING_KEYS = new long[4];
    static final long[] EN_PASSANT_KEYS = new long[8];
    static final long SIDE_TO_MOVE_KEY;

    static {
        SplittableRandom random = new SplittableRandom(SEED);
        for (int i = 0; i < Piece_SQUARE_KEYS.length; i++) {
            for (int j = 0; j < Piece_SQUARE_KEYS[i].length; j++) {
                Piece_SQUARE_KEYS[i][j] = random.nextLong();
            }
        }
        for (int i = 0; i < CASTLING_KEYS.length; i++) CASTLING_KEYS[i] = random.nextLong();
        for (int i = 0; i < EN_PASSANT_KEYS.length; i++) EN_PASSANT_KEYS[i] = random.nextLong();
        SIDE_TO_MOVE_KEY = random.nextLong();
    }

    static long fromBoard(Board board) {
        long hash = getPieceSquareHash(board) ^ getCastlingRightsHash(board);
        if (board.isEnPassantCaptureAvailable()) hash ^= getEnPassantKeys(board.getEnPassantTargetSquare());
        if (board.isBlackToMove()) hash ^= SIDE_TO_MOVE_KEY;
        return hash;
    }

    private static long getPieceSquareHash(Board board) {
        long hash = 0;

        long bb = board.getBitBoardState().getBitboard(OccupancyBitboard.ALL_PIECES);
        while (bb != 0) {
            long lsb = bb & -bb;
            bb &= bb - 1;
            int bitBoardSquare = Long.numberOfTrailingZeros(lsb);

            BoardPosition position = new BoardPosition(bitBoardSquare);
            BoardPiece piece = board.getPieceAt(position);
            hash ^= Piece_SQUARE_KEYS[piece.ordinal()][position.getBitBoardSquare()];
        }
        return hash;
    }

    private static long getCastlingRightsHash(Board board) {
        long hash = 0;
        CastlingRights white = board.getCastlingRightsWhite();
        CastlingRights black = board.getCastlingRightsBlack();
        if (white.canCastleKingSide())  hash ^= CASTLING_KEYS[0];
        if (white.canCastleQueenSide()) hash ^= CASTLING_KEYS[1];
        if (black.canCastleKingSide())  hash ^= CASTLING_KEYS[2];
        if (black.canCastleQueenSide()) hash ^= CASTLING_KEYS[3];
        return hash;
    }

    static long getPieceSquareKey(BoardPiece piece, BoardPosition position) {
        return Piece_SQUARE_KEYS[piece.ordinal()][position.getBitBoardSquare()];
    }

    static long getKingSideCastleWhiteKey() {
        return CASTLING_KEYS[0];
    }

    static long getQueenSideCastleWhiteKey() {
        return CASTLING_KEYS[1];
    }

    static long getKingSideCastleBlackKey() {
        return CASTLING_KEYS[2];
    }

    static long getQueenSideCastleBlackKey() {
        return CASTLING_KEYS[3];
    }

    static long getEnPassantKeys(BoardPosition position) {
        if (position == null) return 0;
        return EN_PASSANT_KEYS[position.getX()];
    }
}