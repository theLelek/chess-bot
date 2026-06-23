package dev.lelek.chess.eval;

import dev.lelek.chess.BoardPosition;
import dev.lelek.chess.Color;
import dev.lelek.chess.BoardPiece;
import dev.lelek.chess.board.OccupancyBitboard;
import dev.lelek.chess.board.model.Board;

public class BoardEvaluation {

    // right now 4250 is base eval for each color
    public static final int DEFAULT_BOARD_VALUE = computeBoardValue(Board.initializeDefaultBoard());

    private static final int PAWN_VALUE = 100;
    private static final int KNIGHT_VALUE = 350;
    private static final int BISHOP_VALUE = 350;
    private static final int ROOK_VALUE = 525;
    private static final int QUEEN_VALUE = 1000;
    private static final int KING_VALUE = 0;

    public static int evaluate(Board board, Color color) {

        long bb = board.getBitBoardState().getBitboard(OccupancyBitboard.ALL_PIECES);
        while (bb != 0) {
            long lsb = bb & -bb;
            bb &= bb - 1;
            int bitBoardSquare = Long.numberOfTrailingZeros(lsb);

            BoardPosition boardPosition = new BoardPosition(bitBoardSquare);
            BoardPiece piece = board.getPieceList()[bitBoardSquare];



        }


        return -1;
    }

    static int computeBoardValue(Board board) {
        int value = 0;
        long bb = board.getBitBoardState().getBitboard(OccupancyBitboard.ALL_PIECES);
        while (bb != 0) {
            long lsb = bb & -bb;
            bb &= bb - 1;
            int bitBoardSquare = Long.numberOfTrailingZeros(lsb);
            BoardPiece piece = board.getPieceList()[bitBoardSquare];

            value += getValue(piece);
        }
        return value;
    }

    private static int getValue(BoardPiece piece) {
        return switch (piece) {
            case WHITE_PAWN, BLACK_PAWN -> PAWN_VALUE;
            case WHITE_KNIGHT, BLACK_KNIGHT -> KNIGHT_VALUE;
            case WHITE_BISHOP, BLACK_BISHOP -> BISHOP_VALUE;
            case WHITE_ROOK, BLACK_ROOK -> ROOK_VALUE;
            case WHITE_QUEEN, BLACK_QUEEN -> QUEEN_VALUE;
            case WHITE_KING, BLACK_KING -> KING_VALUE;
        };
    }
}