package dev.lelek.chess.search;

import dev.lelek.chess.BoardPiece;
import dev.lelek.chess.Move.Move;
import dev.lelek.chess.Move.PromotionMove;
import dev.lelek.chess.board.model.Board;
import dev.lelek.chess.eval.BoardEvaluation;

import java.util.List;

class MoveOrdering {
    static void order(Board board, List<Move> moves) {
        int bestIdx = -1;
        int bestEvaluation = Integer.MIN_VALUE;

        Move pvMove = getPvMove(board);
        int pvMoveIndex = -1;


        for (int i = 0; i < moves.size(); i++) {
            if (moves.get(i).equals(pvMove)) {
                pvMoveIndex = i;
                continue;
            }
            int evaluation = evaluateMove(board, moves.get(i));
            if (evaluation > bestEvaluation) {
                bestEvaluation = evaluation;
                bestIdx = i;
            }
        }

        if (pvMoveIndex != -1) {
            swap(moves, 0, pvMoveIndex);

            if (bestIdx == 0) {
                bestIdx = pvMoveIndex;
            }
        }

        if (bestIdx != -1) {
            swap(moves, pvMoveIndex != -1 ? 1 : 0, bestIdx);
        }
    }

    private static Move getPvMove(Board board) {
        TranspositionTable tt = TranspositionTable.getInstance();
        TranspositionTableEntry entry = tt.getEntry(board.getZobristHash());
        return entry == null ? null : entry.move();
    }

    private static int evaluateMove(Board board, Move move) { // higher = better
        BoardPiece pieceFrom = board.getPieceAt(move.getFrom());
        BoardPiece pieceTo = board.getPieceAt(move.getTo());
        if (pieceFrom == null || pieceTo == null) return 0;
        int evaluation = 0;
        evaluation -= pieceFrom.getEvaluation();
        evaluation += pieceTo.getEvaluation();
        if (move instanceof PromotionMove) evaluation += BoardEvaluation.PAWN_VALUE / 2;
        return evaluation;
    }

    private static void swap(List<Move> moves, int i, int j) {
        Move temp = moves.get(i);
        moves.set(i, moves.get(j));
        moves.set(j, temp);
    }
}