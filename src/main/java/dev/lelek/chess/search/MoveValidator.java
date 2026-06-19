package dev.lelek.chess.search;

import dev.lelek.chess.Move.Move;
import dev.lelek.chess.board.UnmakeMoveInfo;
import dev.lelek.chess.board.model.Board;

import java.util.List;

public class MoveValidator {
    public static boolean isMoveLegal(Board board, Move move) {
        List<Move> pseudoLegalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, board.isWhiteToMove());
        if (! pseudoLegalMoves.contains(move)) return false;

        UnmakeMoveInfo unmakeMoveInfo = new UnmakeMoveInfo(board, move);
        board.makeMove(move);
        List<Move> pseudoLegalMoves2 = PseudoLegalMoveFinder.getPseudoLegalMoves(board, board.isWhiteToMove());
        boolean result = ! MoveGenerator.wasPreviousMoveIllegal(board, move, pseudoLegalMoves2);
        board.unmakeMove(move, unmakeMoveInfo);
        return result;
    }
}
