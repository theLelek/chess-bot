package chess.move_generation;

import chess.BoardPosition;
import chess.board.model.Board;
import chess.board.model.BoardPiece;

class BoardEvaluation {

    private static final int PAWN_VALUE = 100;
    private static final int KNIGHT_VALUE = 300;
    private static final int BISHOP_VALUE = 300;
    private static final int ROOK_VALUE = 500;
    private static final int QUEEN_VALUE = 900;
    private static final int KING_VALUE = 100_000;

    public static int evaluate(Board board) {
        int value = 0;
        for (BoardPosition position : board.getPiecesIndexes()) {
            BoardPiece piece = board.getBoardPieces()[position.y()][position.x()];
            switch (piece) {
                case WHITE_PAWN -> value += PAWN_VALUE;
                case BLACK_PAWN -> value -= PAWN_VALUE;

                case WHITE_KNIGHT -> value += KNIGHT_VALUE;
                case BLACK_KNIGHT -> value -= KNIGHT_VALUE;

                case WHITE_BISHOP -> value += BISHOP_VALUE;
                case BLACK_BISHOP -> value -= BISHOP_VALUE;

                case WHITE_ROOK -> value += ROOK_VALUE;
                case BLACK_ROOK -> value -= ROOK_VALUE;

                case WHITE_QUEEN -> value += QUEEN_VALUE;
                case BLACK_QUEEN -> value -= QUEEN_VALUE;

                case WHITE_KING -> value += KING_VALUE;
                case BLACK_KING -> value -= KING_VALUE;
            }
        }
        return value;
    }
}
