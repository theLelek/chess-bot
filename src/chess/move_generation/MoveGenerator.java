package chess.move_generation;

import chess.LegalMoves;
import chess.Move.Move;
import chess.board.model.Board;

public class MoveGenerator {

    private final int DEPTH_LIMIT = 2;
    private LegalMoves legalMovesWhite = null;
    private LegalMoves legalMovesBlack = null;


    public static Move generateMove(Board board) {
        return null;
    }

    private int minimax(Board board, int currentDepth) {
        if (currentDepth == DEPTH_LIMIT) {
            return BoardEvaluation.evaluate(board);
        }
        LegalMoves currentLegalMoves = (currentDepth % 2 == 0) ? legalMovesWhite : legalMovesBlack;

        int maxEvaluation = Integer.MIN_VALUE;
        for (Move move : currentLegalMoves.getLegalMoves()) {
            maxEvaluation = Math.max(minimax(board, currentDepth + 1), maxEvaluation);
        }
        return maxEvaluation;
    }
}