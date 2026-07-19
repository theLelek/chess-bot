package dev.lelek.chess.search;

import dev.lelek.chess.BoardPiece;
import dev.lelek.chess.Move.Move;
import dev.lelek.chess.board.model.Board;

import java.util.List;

class MoveOrdering {
    public static void order(Board board, List<Move> moves) {
        int bestIdx = 0;
        int bestEvaluation = Integer.MIN_VALUE;

        for (int i = 0; i < moves.size(); i++) {
            int evaluation = evaluateMove(board, moves.get(i));
            if (evaluation > bestEvaluation) {
                bestEvaluation = evaluation;
                bestIdx = i;
            }
        }

        if (bestIdx != 0) {
            swap(moves, 0, bestIdx);
        }
    }

    private static int evaluateMove(Board board, Move move) { // higher = better
        BoardPiece pieceFrom = board.getPieceAt(move.getFrom());
        BoardPiece pieceTo = board.getPieceAt(move.getTo());
        if (pieceFrom == null || pieceTo == null) return 0;
        int evaluation = 0;
        evaluation -= pieceFrom.getEvaluation();
        evaluation += pieceTo.getEvaluation();
        return evaluation;
    }

    private static void swap(List<Move> moves, int i, int j) {
        Move temp = moves.get(i);
        moves.set(i, moves.get(j));
        moves.set(j, temp);
    }
}