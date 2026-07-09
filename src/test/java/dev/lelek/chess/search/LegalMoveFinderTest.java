package dev.lelek.chess.search;

import dev.lelek.chess.Move.CastlingMove;
import dev.lelek.chess.Move.Move;
import dev.lelek.chess.board.model.Board;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LegalMoveFinderTest {


    @Test
    void isMoveLegal_defaultBoard() {
        Board board = Board.initializeDefaultBoard();

        Move move1 = new Move("e2", "e4");
        Assertions.assertTrue(LegalMoveFinder.isMoveLegal(board, move1));

        Move move2 = new Move("a1", "b1");
        Assertions.assertFalse(LegalMoveFinder.isMoveLegal(board, move2));
    }

    @Test
    void isMoveLegal_customBoard() {
        Board board = Board.fromFen("rn1qkbnr/pppppppp/8/1b6/8/8/PPPP1PPP/rN2K2R w Kkq - 0 1");

        Move move1 = new Move("b1", "c3");
        Assertions.assertFalse(LegalMoveFinder.isMoveLegal(board, move1));

        Move move2 = new CastlingMove("e1", "g1");
        Assertions.assertFalse(LegalMoveFinder.isMoveLegal(board, move2));
    }
}