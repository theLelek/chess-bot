package api;

import chess.BoardPosition;
import chess.Move;
import org.junit.jupiter.api.Disabled;
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

    // TODO write toString()
//    @Test
//    @DisplayName("test Move::toString() with a3 and b4")
//    void testToString() {
//        Move actual = new Move(new BoardPosition(3, 1), new BoardPosition(4, 2));
//
//        String expected = "from: a3 to: b4";
//        assertEquals(expected, actual.toString());
//    }
}