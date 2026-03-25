package chess.board;

import chess.BoardPosition;
import chess.Move;
import chess.board.model.Board;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PseudoLegalMoveFinderTest {
    @Test
    @DisplayName("test find legal moves with black bishop at f8")
    public void testFindLegalMoves1() {
        Board board = Board.initializeFromFen("r1bqkbnr/pppp1ppp/2n5/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R w KQkq - 0 1");
        List<Move> legalMoves = PseudoLegalMoveFinder.getLegalMoves(board, new BoardPosition(5, 0));

        String expectedLegalMoves = "[from: f8 to: e7, from: f8 to: d6, from: f8 to: c5, from: f8 to: b4, from: f8 to: a3]";

        Assertions.assertEquals(expectedLegalMoves, legalMoves.toString());
    }


}