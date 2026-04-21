package chess;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardPositionTest {

    @Test
    void getFromString() {
        BoardPosition actual = new BoardPosition("a8");
        BoardPosition expected = new BoardPosition(0,0);
        assertEquals(expected, actual);
    }

    @Test
    void getFromString_h1_isLastPosition() {
        BoardPosition actual = new BoardPosition("h1");
        BoardPosition expected = new BoardPosition(7, 7);
        assertEquals(expected, actual);
    }

    @Test
    void getFromString_a1_isBottomLeft() {
        BoardPosition actual = new BoardPosition("a1");
        BoardPosition expected = new BoardPosition(0, 7);
        assertEquals(expected, actual);
    }

    @Test
    void getFromString_h8_isTopRight() {
        BoardPosition actual = new BoardPosition("h8");
        BoardPosition expected = new BoardPosition(7, 0);
        assertEquals(expected, actual);
    }

    @Test
    void getFromString_centerSquare() {
        BoardPosition actual = new BoardPosition("d5");
        BoardPosition expected = new BoardPosition(3, 3);
        assertEquals(expected, actual);
    }

    @Test
    void getFromString_isCaseInsensitive() {
        BoardPosition lower = new BoardPosition("e4");
        BoardPosition upper = new BoardPosition("E4");
        assertEquals(lower, upper);
    }

    @Test
    void equality_sameCoordinates_areEqual() {
        BoardPosition first = new BoardPosition(3, 5);
        BoardPosition second = new BoardPosition(3, 5);
        assertEquals(first, second);
    }

    @Test
    void equality_differentCoordinates_areNotEqual() {
        BoardPosition first = new BoardPosition(3, 5);
        BoardPosition second = new BoardPosition(5, 3);
        assertNotEquals(first, second);
    }

    @Test
    void equality_stringAndCoordinateConstructors_match() {
        for (char col = 'a'; col <= 'h'; col++) {
            for (char row = '1'; row <= '8'; row++) {
                BoardPosition fromString = new BoardPosition("" + col + row);
                BoardPosition fromCoords = new BoardPosition(col - 'a', '8' - row);
                assertEquals(fromCoords, fromString,
                        "Mismatch at " + col + row);
            }
        }
    }

    @Test
    void getFromString_invalidInput_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new BoardPosition("z9"));
    }

    @Test
    void getFromString_nullInput_throwsException() {
        assertThrows(NullPointerException.class, () -> new BoardPosition((String) null));
    }

    @Test
    void getFromString_emptyString_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new BoardPosition(""));
    }
}