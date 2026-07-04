package dev.lelek.chess.eval;

import dev.lelek.chess.board.model.Board;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class PieceSquareTableHandlerTest {

    @Test
    void fromBoard_defaultBoard() {
        Board board = Board.initializeDefaultBoard();
        Assertions.assertEquals(1, PieceSquareTableHandler.fromBoard(board).getGamePhase());
    }

    @Test
    void fromBoard_emptyBoard() {
        Board board = Board.initializeFromFen("8/8/8/8/8/8/8/8 w - - 0 1");
        Assertions.assertEquals(0, PieceSquareTableHandler.fromBoard(board).getGamePhase());
    }

    @Test
    void fromBoard_endGame() {
        Board board = Board.initializeFromFen("3bkn2/7n/8/8/8/8/8/R3K3 w Q - 0 1");
        Assertions.assertTrue(0.3 > PieceSquareTableHandler.fromBoard(board).getGamePhase());

    }
}