package dev.lelek.chess.move_generation;

import dev.lelek.chess.Move.Move;
import dev.lelek.chess.board.model.Board;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MoveGeneratorTest {

    @Test
    void generateMove_captureQueen() {
        Board board = Board.initializeFromFen("k7/8/8/8/3q4/2Q5/8/K7 w - - 0 1");
        Move bestMove = MoveGenerator.generateMove(board, 3);
        Assertions.assertEquals(new Move("c3", "d4"), bestMove);
    }

    @Test
    void generateMove_captureQueenWhileInCheck() {
        Board board = Board.initializeFromFen("K6k/8/8/8/8/8/8/3q3Q b - - 0 1");
        Move bestMove = MoveGenerator.generateMove(board, 1);
        Assertions.assertEquals(new Move("d1", "h1"), bestMove);
    }

    @Test
    void generateMove_checkmate() {
        Board board = Board.initializeFromFen("1k6/1Q5R/8/8/8/8/8/K7 b - - 3 2");
        Move bestMove = MoveGenerator.generateMove(board, 2);
        Assertions.assertNull(bestMove);
    }

    @Test
    void generateMove_findCheckmate() {
        Board board = Board.initializeFromFen("1k6/7R/8/8/4Q3/8/8/K7 w - - 3 2");
        Move bestMove = MoveGenerator.generateMove(board, 2);
        Assertions.assertEquals(new Move("e4", "b7"), bestMove);
    }
}