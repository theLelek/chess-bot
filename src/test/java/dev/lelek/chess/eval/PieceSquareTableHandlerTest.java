package dev.lelek.chess.eval;

import dev.lelek.chess.Color;
import dev.lelek.chess.Move.Move;
import dev.lelek.chess.board.model.Board;
import dev.lelek.chess.search.MoveGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class PieceSquareTableHandlerTest {

    @Test
    void main() {
        Board board = Board.fromFen("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1");
        Move move = MoveGenerator.generateMove(board, 4);
        System.out.println(move);
    }

    @Test
    void fromBoard_defaultBoard() {
        Board board = Board.initializeDefaultBoard();
        Assertions.assertEquals(1, PieceSquareTableHandler.fromBoard(board).getGamePhase());
    }

    @Test
    void fromBoard_emptyBoard() {
        Board board = Board.fromFen("8/8/8/8/8/8/8/8 w - - 0 1");
        Assertions.assertEquals(0, PieceSquareTableHandler.fromBoard(board).getGamePhase());
    }

    @Test
    void fromBoard_endGame() {
        Board board = Board.fromFen("3bkn2/7n/8/8/8/8/8/R3K3 w Q - 0 1");
        Assertions.assertTrue(0.3 > PieceSquareTableHandler.fromBoard(board).getGamePhase());

    }
}