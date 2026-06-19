package dev.lelek.chess.Move;

import dev.lelek.chess.BoardPosition;
import dev.lelek.chess.Move.Move;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveTest
{
    @Test
    void moveConstructor() {
        BoardPosition from = new BoardPosition(0, 0);
        BoardPosition to = new BoardPosition(0, 1);
        Move move = new Move(from, to);
        assertEquals(new BoardPosition(0, 0), move.from());
        assertEquals(new BoardPosition(0, 1), move.to());
    }
}