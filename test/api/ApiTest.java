package api;

import chess.BoardPosition;
import chess.Move.Move;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApiTest {

    @org.junit.jupiter.api.Test
    void initializeMove() {
        String testInput = "1 2 2 4";
        Move actal = Api.initializeMove(testInput);
        assertEquals(new Move(new BoardPosition(1, 2), new BoardPosition(2, 4)), actal);
    }
    
    @Test
    @DisplayName("test Move::toString() with a3 and b4")
    void testToString() {
        Move actual = new Move(new BoardPosition(1, 3), new BoardPosition(2, 4));

        String expected = "from: b5 to: c4";
        assertEquals(expected, actual.toString());
    }
}