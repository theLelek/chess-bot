package dev.lelek.chess.search;

import dev.lelek.chess.board.model.Board;

import java.util.Stack;

public enum GameStatus {

    ONGOING,
    CHECKMATE,
    STALEMATE;

    public static GameStatus getGameStatus(Board board) {
        BoardResults result = MoveGenerator.negmax(board, PseudoLegalMoveFinder.getPseudoLegalMoves(board, board.isWhiteToMove()), 1, new Stack<>(), false, -1, MoveGenerator.ALPHA_START, MoveGenerator.BETA_START);
        if (result.hasLost()) {
            return GameStatus.CHECKMATE;
        } else if (result.hasDrawn()) {
            return GameStatus.STALEMATE;
        } else {
            return GameStatus.ONGOING;
        }
    }
}
