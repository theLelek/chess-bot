package dev.lelek.chess.move_generation;

import dev.lelek.chess.Move.Move;
import dev.lelek.chess.board.model.Board;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveGeneratorTest {


    @Test
    void generateMove() {
        Board board = Board.initializeFromFen("K6k/8/8/8/8/8/8/3q3Q b - - 0 1");
        Move bestMove = MoveGenerator.generateMove(board, 1);
        assertEquals(new Move("d1", "h1"), bestMove);
    }
}