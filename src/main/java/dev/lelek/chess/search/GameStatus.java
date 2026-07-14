package dev.lelek.chess.search;

import dev.lelek.chess.board.model.Board;

import java.util.Stack;

public enum GameStatus {

    ONGOING,
    CHECKMATE,
    STALEMATE;

    public static GameStatus getGameStatus(Board board) {
        BoardResults result = MoveGenerator.negmax(board, PseudoLegalMoveFinder.getPseudoLegalMoves(board, board.isWhiteToMove()), 1, new Stack<>(), false, -1, MoveGenerator.WORST, MoveGenerator.BEST);
        if (result.isCheckmate()) {
            return GameStatus.CHECKMATE;
        } else if (result.isStalemate()) {
            return GameStatus.STALEMATE;
        } else {
            return GameStatus.ONGOING;
        }
    }
}
