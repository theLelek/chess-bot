package chess.Move;

import chess.BoardPosition;
import chess.board.model.BoardPiece;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PromotionMoveTest {

    @Test
    void testToString() {
        String expected = "from: e7 to: e8 Promotion to: WHITE_QUEEN";
        String actual = new PromotionMove(new BoardPosition("e7"), new BoardPosition("e8"), BoardPiece.WHITE_QUEEN).toString();
        Assertions.assertEquals(expected, actual);
    }
}