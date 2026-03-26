package chess;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardPositionTest {

    @Test
    void getFromString() {
        BoardPosition actual = BoardPosition.getFromString("a8");
        BoardPosition expected = new BoardPosition(0,0);
        assertEquals(expected, actual);
    }
}