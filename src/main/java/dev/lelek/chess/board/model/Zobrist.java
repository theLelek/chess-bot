package dev.lelek.chess.board.model;

import dev.lelek.chess.BoardPiece;
import dev.lelek.chess.BoardPosition;
import dev.lelek.chess.Color;
import dev.lelek.chess.board.OccupancyBitboard;

import java.util.SplittableRandom;

final class Zobrist {

    private static final long SEED = 0;

    private static final long[][] Piece_Square_KEYS = new long[BoardPiece.values().length][Board.SIZE * Board.SIZE];
    private static final long[] CASTLING_KEYS = new long[4];
    private static final long[] EN_PASSANT_KEYS = new long[8];
    private static final long SIDE_TO_MOVE_KEYS;

    static {
        SplittableRandom random = new SplittableRandom(SEED);
        for (int i = 0; i < Piece_Square_KEYS.length; i++) {
            for (int j = 0; j < Piece_Square_KEYS[i].length; j++) {
                Piece_Square_KEYS[i][j] = random.nextLong();
            }
        }
        for (int i = 0; i < CASTLING_KEYS.length; i++) CASTLING_KEYS[i] = random.nextLong();
        for (int i = 0; i < EN_PASSANT_KEYS.length; i++) EN_PASSANT_KEYS[i] = random.nextLong();
        SIDE_TO_MOVE_KEYS = random.nextLong();
    }

    public static long fromBoard(Board board) {
        long hash = getPieceSquareHash(board) ^ getCastlingRightsHash(board);
        if (board.getEnPassantTargetSquare() != null) hash ^= EN_PASSANT_KEYS[board.getEnPassantTargetSquare().getY()];
        if (board.isBlackToMove()) hash ^= SIDE_TO_MOVE_KEYS;
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
            hash = hash ^ Piece_Square_KEYS[piece.ordinal()][position.getBitBoardSquare()];
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

    private boolean enPassantCaptureAvailable(Board board, int doublePushedFile, boolean whiteJustMoved) {
        Color color = board.isWhiteToMove() ? Color.WHITE : Color.BLACK;
        int captureRank = color.get
        long enemyPawns = board.getBitBoardState().getBitboard(
                whiteJustMoved ? OccupancyBitboard.BLACK_PAWNS : OccupancyBitboard.WHITE_PAWNS
        );

        long adjacentMask = 0;
        if (doublePushedFile > 0) adjacentMask |= squareMask(doublePushedFile - 1, captureRank);
        if (doublePushedFile < 7) adjacentMask |= squareMask(doublePushedFile + 1, captureRank);

        return (enemyPawns & adjacentMask) != 0;
    }
}