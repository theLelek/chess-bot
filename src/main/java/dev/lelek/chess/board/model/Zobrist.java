package dev.lelek.chess.board.model;

import dev.lelek.chess.BoardPiece;
import dev.lelek.chess.BoardPosition;
import dev.lelek.chess.Color;
import dev.lelek.chess.board.OccupancyBitboard;

import java.util.SplittableRandom;

final class Zobrist {

    static final long SEED = 0;

    static final long[][] Piece_SQUARE_KEYS = new long[BoardPiece.values().length][Board.SIZE * Board.SIZE];
    static final long[] CASTLING_KEYS = new long[4];
    static final long[] EN_PASSANT_KEYS = new long[8];
    static final long SIDE_TO_MOVE_KEYS;

    static {
        SplittableRandom random = new SplittableRandom(SEED);
        for (int i = 0; i < Piece_SQUARE_KEYS.length; i++) {
            for (int j = 0; j < Piece_SQUARE_KEYS[i].length; j++) {
                Piece_SQUARE_KEYS[i][j] = random.nextLong();
            }
        }
        for (int i = 0; i < CASTLING_KEYS.length; i++) CASTLING_KEYS[i] = random.nextLong();
        for (int i = 0; i < EN_PASSANT_KEYS.length; i++) EN_PASSANT_KEYS[i] = random.nextLong();
        SIDE_TO_MOVE_KEYS = random.nextLong();
    }

    public static long fromBoard(Board board) {
        long hash = getPieceSquareHash(board) ^ getCastlingRightsHash(board);
        if (isEnPassantCaptureAvailable(board)) hash ^= EN_PASSANT_KEYS[board.getEnPassantTargetSquare().getX()];
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
            hash = hash ^ Piece_SQUARE_KEYS[piece.ordinal()][position.getBitBoardSquare()];
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

    private static boolean isEnPassantCaptureAvailable(Board board) {
        Color color = board.isWhiteToMove() ? Color.WHITE : Color.BLACK;
        BoardPosition position = board.getEnPassantPiecePosition();
        if (position == null) return false;

        BoardPosition positionLeft = position.getX() == 0 ? null : new BoardPosition(position.getX() - 1, position.getY());
        BoardPosition positionRight = position.getX() == Board.SIZE - 1 ? null : new BoardPosition(position.getX() + 1, position.getY());

        if (positionLeft != null && board.getPieceAt(positionLeft) == color.getPawn()) return true;
        if (positionRight != null && board.getPieceAt(positionRight) == color.getPawn()) return true;

        return false;
    }

    static long getPieceSquareKey(BoardPiece piece, BoardPosition position) {
        return Piece_SQUARE_KEYS[piece.ordinal()][position.getBitBoardSquare()];
    }
}