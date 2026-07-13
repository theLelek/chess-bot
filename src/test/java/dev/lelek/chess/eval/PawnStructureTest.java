package dev.lelek.chess.eval;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.lelek.chess.BoardPosition;
import dev.lelek.chess.board.model.Board;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class PawnStructureTest {

    @Test
    void getPassedPawnValue_White() {
        Board board1 = Board.fromFen("7k/8/8/8/1P6/8/8/7K w - - 0 1");
        BoardPosition position1 = new BoardPosition("b4");
        Assertions.assertNotEquals(0, PawnStructure.getPassedPawnValue(board1, position1));

        Board board2 = Board.fromFen("1p5k/8/8/8/1P6/8/8/7K w - - 0 1");
        BoardPosition position2 = new BoardPosition("b4");
        Assertions.assertEquals(0, PawnStructure.getPassedPawnValue(board2, position2));

        Board board3 = Board.fromFen("1p5k/8/8/8/1P6/8/8/7K w - - 0 1");
        BoardPosition position3 = new BoardPosition("b4");
        Assertions.assertEquals(0, PawnStructure.getPassedPawnValue(board3, position3));


        Board board4 = Board.fromFen("p6k/8/8/8/1P6/8/8/7K w - - 0 1");
        BoardPosition position4 = new BoardPosition("b4");
        Assertions.assertEquals(0, PawnStructure.getPassedPawnValue(board4, position4));

        Board board5 = Board.fromFen("1r5k/8/8/4p3/1P6/8/8/7K w - - 0 1");
        BoardPosition position5 = new BoardPosition("b4");
        Assertions.assertNotEquals(0, PawnStructure.getPassedPawnValue(board5, position5));

        Board board6 = Board.fromFen("7k/4r3/8/8/Pp6/8/8/7K w - - 0 1");
        BoardPosition position6 = new BoardPosition("a4");
        Assertions.assertNotEquals(0, PawnStructure.getPassedPawnValue(board6, position6));

        Board board7 = Board.fromFen("7k/1p2r3/8/8/P7/8/8/7K w - - 0 1");
        BoardPosition position7 = new BoardPosition("a4");
        Assertions.assertEquals(0, PawnStructure.getPassedPawnValue(board7, position7));
    }

    @Test
    void getPassedPawnValue_Black() {
        Board board1 = Board.fromFen("7k/1p2r3/8/8/P7/8/8/7K w - - 0 1");
        BoardPosition position1 = new BoardPosition("b7");
        Assertions.assertEquals(0, PawnStructure.getPassedPawnValue(board1, position1));

        Board board2 = Board.fromFen("7k/1p2r3/8/8/4P3/8/8/7K w - - 0 1");
        BoardPosition position2 = new BoardPosition("b7");
        Assertions.assertNotEquals(0, PawnStructure.getPassedPawnValue(board2, position2));

        Board board3 = Board.fromFen("7k/1p2r3/8/8/4P3/8/8/1Q5K w - - 0 1");
        BoardPosition position3 = new BoardPosition("b7");
        Assertions.assertNotEquals(0, PawnStructure.getPassedPawnValue(board3, position3));

        Board board4 = Board.fromFen("7k/p3r3/8/8/4P3/8/8/1Q5K w - - 0 1");
        BoardPosition position4 = new BoardPosition("a7");
        Assertions.assertNotEquals(0, PawnStructure.getPassedPawnValue(board4, position4));
    }
}