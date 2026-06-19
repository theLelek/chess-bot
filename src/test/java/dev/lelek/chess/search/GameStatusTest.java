package dev.lelek.chess.search;

import dev.lelek.chess.Move.Move;
import dev.lelek.chess.board.model.Board;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GameStatusTest {

    @Test
    void checkMate() {
        Board board = Board.initializeFromFen("1r6/8/8/8/8/8/8/Kq6 w - - 0 1");
        Move bestMove = MoveGenerator.generateMove(board, 1);
        Assertions.assertEquals(GameStatus.CHECKMATE, GameStatus.getGameStatus(board));
        Assertions.assertNull(bestMove);
    }

    @Test
    void stalemate() {
        Board board = Board.initializeFromFen("8/8/8/8/8/1q6/8/K7 w - - 0 1");
        Move bestMove = MoveGenerator.generateMove(board, 1);
        Assertions.assertEquals(GameStatus.STALEMATE, GameStatus.getGameStatus(board));
        Assertions.assertNull(bestMove);
    }

    @Test
    void ongoing() {
        Board board = Board.initializeFromFen("1r6/8/1q6/8/8/8/8/K7 w - - 0 1");
        Move bestMove = MoveGenerator.generateMove(board, 1);
        Assertions.assertEquals(GameStatus.ONGOING, GameStatus.getGameStatus(board));
        Assertions.assertEquals(new Move("a1", "a2"), bestMove);
    }
}