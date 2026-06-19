package dev.lelek.chess.search;

import dev.lelek.chess.Color;
import dev.lelek.chess.board.BoardPiece;
import dev.lelek.chess.board.OccupancyBitboard;
import dev.lelek.chess.board.model.Board;

class BoardEvaluation {

    private static final int PAWN_VALUE = 100;
    private static final int KNIGHT_VALUE = 300;
    private static final int BISHOP_VALUE = 300;
    private static final int ROOK_VALUE = 500;
    private static final int QUEEN_VALUE = 900;
    private static final int KING_VALUE = 100_000;

    public static int evaluate(Board board, Color color) {
        int value = 0;
        long bb = board.getBitBoardState().getBitboard(OccupancyBitboard.ALL_PIECES); // copy
        while (bb != 0) {
            long lsb = bb & -bb;
            bb &= bb - 1;

            BoardPiece piece = board.getPieceList()[Long.numberOfTrailingZeros(lsb)];
            value += getValue(piece);
        }
        return (color == Color.WHITE) ? value : -value;
    }

    private static int getValue(BoardPiece piece) {
        int value = 0;
        switch (piece) {
            case BoardPiece.WHITE_PAWN:
                value += PAWN_VALUE;
                break;
            case BoardPiece.BLACK_PAWN:
                value -= PAWN_VALUE;
                break;

            case BoardPiece.WHITE_KNIGHT:
                value += KNIGHT_VALUE;
                break;
            case BoardPiece.BLACK_KNIGHT:
                value -= KNIGHT_VALUE;
                break;

            case BoardPiece.WHITE_BISHOP:
                value += BISHOP_VALUE;
                break;
            case BoardPiece.BLACK_BISHOP:
                value -= BISHOP_VALUE;
                break;

            case BoardPiece.WHITE_ROOK:
                value += ROOK_VALUE;
                break;
            case BoardPiece.BLACK_ROOK:
                value -= ROOK_VALUE;
                break;

            case BoardPiece.WHITE_QUEEN:
                value += QUEEN_VALUE;
                break;
            case BoardPiece.BLACK_QUEEN:
                value -= QUEEN_VALUE;
                break;

            case BoardPiece.WHITE_KING:
                value += KING_VALUE;
                break;
            case BoardPiece.BLACK_KING:
                value -= KING_VALUE;
                break;
        }
        return value;
    }
}
