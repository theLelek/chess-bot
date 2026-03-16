package api;

import chess.Move;

import static org.junit.jupiter.api.Assertions.*;

class ApiTest {

    @org.junit.jupiter.api.Test
    void initializeMove() {
        String testInput = "1 2 2 4";
        Move actal = Api.initializeMove(testInput);
        assertEquals(new Move(1, 2, 2, 4), actal);
    }
}