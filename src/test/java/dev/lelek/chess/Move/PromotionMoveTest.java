package dev.lelek.chess.Move;

import dev.lelek.chess.BoardPosition;
import dev.lelek.chess.Move.PromotionMove;
import dev.lelek.chess.board.BoardPiece;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PromotionMoveTest {

    @Test
    void testToString() {
        String expected = "from: e7 to: e8 Promotion to: WHITE_QUEEN";
        String actual = new PromotionMove(new BoardPosition("e7"), new BoardPosition("e8"), BoardPiece.WHITE_QUEEN).toString();
        Assertions.assertEquals(expected, actual);
    }
}