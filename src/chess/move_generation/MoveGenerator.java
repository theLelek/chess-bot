package chess.move_generation;

import chess.Move;
import chess.board.model.Board;
import chess.board.model.BoardPiece;

public class MoveGenerator {

    private final int DEPTH_LIMIT = 2;

    public static Move generateMove(Board board) {
        return null;
    }

//    private int minimax(Board board, int currentDepth) {
//        if (currentDepth == DEPTH_LIMIT) {
//            return BoardEvaluation.evaluate(board);
//        }
//
//        if (currentDepth % 2 == 0) {
//            int maxEvaluation = Integer.MIN_VALUE;
//            for (Move move : board.getPseudoLegalMovesWhite()) {
//                maxEvaluation = Math.max(minimax(null, currentDepth + 1), maxEvaluation);
//            }
//        }
//        return -1;
//    }
}
