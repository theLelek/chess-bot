package dev.lelek.chess.search;

import dev.lelek.chess.Move.Move;
import dev.lelek.chess.board.model.Board;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveGeneratorTest {
    @Test
    void generateMove_captureQueen() {
        Board board = Board.fromFen("k7/8/8/8/3q4/2Q5/8/K7 w - - 0 1");
        Move bestMove = MoveGenerator.generateMove(board, 3);
        Assertions.assertEquals(new Move("c3", "d4"), bestMove);
    }

    @Test
    void generateMove_captureQueenWhileInCheck() {
        Board board = Board.fromFen("K6k/8/8/8/8/8/8/3q3Q b - - 0 1");
        Move bestMove = MoveGenerator.generateMove(board, 1);
        Assertions.assertEquals(new Move("d1", "h1"), bestMove);
    }

    @Test
    void generateMove_checkmate() {
        Board board = Board.fromFen("1k6/1Q5R/8/8/8/8/8/K7 b - - 3 2");
        Move bestMove = MoveGenerator.generateMove(board, 2);
        Assertions.assertNull(bestMove);
    }

    @Test
    void generateMove_mateIn1ByQueen() {
        Board board = Board.fromFen("1k6/8/8/8/4Q3/8/8/K6B w - - 3 2");
        Move bestMove = MoveGenerator.generateMove(board, 2);
        Assertions.assertEquals(new Move("e4", "b7"), bestMove);
    }

    @Test
    void generateMove_mateIn1ByRook() {
        Board board = Board.fromFen("1k6/7R/8/8/8/8/8/K4R2 w - - 3 2");
        Move bestMove = MoveGenerator.generateMove(board, 2);
        Assertions.assertEquals(new Move("f1", "f8"), bestMove);
    }

    @Test
    void generateMove_mateIn2() {
        Board board = Board.fromFen("3qr2k/pbpp2pp/1p5N/3Q2b1/2P1P3/P7/1PP2PPP/R4RK1 w - - 1 2");
        Move bestMove = MoveGenerator.generateMove(board, 4);
        Assertions.assertEquals(new Move("d5", "g8"), bestMove);
    }

    @Test
    void generateMove_GameEndsInMate() {
        Board board = Board.fromFen("8/8/8/8/1q6/8/2k5/K7 w - - 0 1");
        Move bestMove = MoveGenerator.generateMove(board, 4);
        Assertions.assertEquals(new Move("a1", "a2"), bestMove);
    }

    @Test
    void generateMove_littleTime_doesntReturnNull() {
        Board board = Board.initializeDefaultBoard();
        Move bestMove = MoveGenerator.generateMove(board, 1L);
        Assertions.assertNotNull(bestMove);
    }

    @Test
    void generateMove_findShortestMate() {
        Board board = Board.fromFen("7k/3R4/1R6/8/8/8/8/7K w - - 1 1");
        Move bestMove = MoveGenerator.generateMove(board, 5);
        Assertions.assertEquals(new Move("b6", "b8"), bestMove);
    }

    @Test
    void generateMove_esapeMate() {
        Board board = Board.fromFen("1r4kr/3R1ppp/4p3/p4n2/5P2/2N4P/PPP3P1/3R3K b - - 2 25");
        Move bestMove = MoveGenerator.generateMove(board, 1000L);
        Assertions.assertNotEquals(new Move("f5", "e3"), bestMove);
        System.out.println(bestMove);
    }

    @Test
    void generateMove_testTime() {
        // depth 6 = 4.4 sec
        // depth 5 = 0.384 sec
        Board board = Board.initializeDefaultBoard();
        Move bestMove = MoveGenerator.generateMove(board, 5);
        System.out.println(bestMove);
    }
}