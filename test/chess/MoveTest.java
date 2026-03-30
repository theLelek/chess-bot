package chess;

import chess.board.model.Board;
import org.junit.jupiter.api.BeforeEach;
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

    @Test
    void moveBuilder() {
        Move actual = new Move.Builder()
                .fromX(0)
                .fromY(1)
                .toX(2)
                .toY(3).build();
        assertEquals(new BoardPosition(0, 1), actual.from());
        assertEquals(new BoardPosition(2, 3), actual.to());
    }
}