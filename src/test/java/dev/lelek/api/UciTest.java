package dev.lelek.api;

import dev.lelek.chess.board.model.Board;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UciTest {
    @Test
    void positionTest() {
        Uci uci = new Uci();

        uci.handleCommand("position startpos");
        Assertions.assertEquals(Board.initializeDefaultBoard(), uci.getBoard());

        uci.handleCommand("position startpos moves e2e4 e7e5 g1f3 b8c6");
        Assertions.assertEquals(Board.initializeFromFen("r1bqkbnr/pppp1ppp/2n5/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R w KQkq - 2 3"), uci.getBoard());

        uci.handleCommand("position fen rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        Assertions.assertEquals(Board.initializeFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"), uci.getBoard());

        uci.handleCommand("position fen rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 moves e2e4 e7e5 g1f3 b8c6");
        Assertions.assertEquals(Board.initializeFromFen("r1bqkbnr/pppp1ppp/2n5/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R w KQkq - 2 3"), uci.getBoard());
    }

    @Test
    void asynchronousTest() {
        Uci uci = new Uci();
        Assertions.assertEquals(3, uci.handleCommand("uci").split("\n").length);
        Assertions.assertNull(uci.handleCommand("position startpos"));
        Assertions.assertEquals(Board.initializeDefaultBoard(), uci.getBoard());
        Runnable runnable = () -> {
            uci.handleCommand("go");
        };
        new Thread(runnable).start();
        Assertions.assertEquals("readyok", uci.handleCommand("isready"));
    }
}
