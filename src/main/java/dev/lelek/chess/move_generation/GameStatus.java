package dev.lelek.chess.move_generation;

import dev.lelek.chess.board.model.Board;

import java.util.Stack;

public enum GameStatus {

    ONGOING,
    CHECKMATE,
    STALEMATE;

    public static GameStatus getGameStatus(Board board) {
        BoardResults result = MoveGenerator.negmax(board, null, 1, new Stack<>());
        if (result.move() == null && result.score() == MoveGenerator.WORST) {
            return GameStatus.CHECKMATE;
        } else if (result.move() == null && result.score() == 0) {
            return GameStatus.STALEMATE;
        } else {
            return GameStatus.ONGOING;
        }
    }
}
