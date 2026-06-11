package dev.lelek.chess.Move;

import dev.lelek.chess.BoardPosition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CastlingMoveTest {

    @Test
    void isKingSideCastling() {
        CastlingMove move = new CastlingMove(new BoardPosition("e1"), new BoardPosition("g1"));
        Assertions.assertTrue(move.isKingSideCastling());
        Assertions.assertFalse(move.isQueenSideCastling());
    }

    @Test
    void isQueenSideCastling() {
        CastlingMove move = new CastlingMove(new BoardPosition("e1"), new BoardPosition("c1"));
        Assertions.assertFalse(move.isKingSideCastling());
        Assertions.assertTrue(move.isQueenSideCastling());
    }
}