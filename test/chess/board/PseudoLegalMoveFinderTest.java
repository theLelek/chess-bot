package chess.board;

import chess.BoardPosition;
import chess.Move;
import chess.board.model.Board;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PseudoLegalMoveFinderTest {
    @Test
    public void findLegalMoves() {
        Board board = Board.initializeFromFen("r1bqkbnr/pppp1ppp/2n5/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R w KQkq - 0 1");
        List<Move> legalMoves = PseudoLegalMoveFinder.getLegalMoves(board, new BoardPosition(5, 0));

        System.out.println(legalMoves.toString());
    }
}