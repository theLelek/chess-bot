package dev.lelek.chess.eval;

import dev.lelek.chess.board.model.Board;
import dev.lelek.chess.board.model.Fen;
import dev.lelek.chess.search.PseudoLegalMoveFinder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardEvaluationTest {

    @Test
    void kingSafetyTest() {
        Board board1 = Fen.fromFen("r1bqk2r/pppp1ppp/2n2n2/2b1p3/2B1P3/5N2/PPPP1PPP/RNBQ1RK1 w kq - 6 5");
        int evaluation1 = BoardEvaluation.evaluate(board1, PseudoLegalMoveFinder.getPseudoLegalMoves(board1, board1.isWhiteToMove()));

        Board board2 = Fen.fromFen("r1bqk2r/pppp1ppp/2n2n2/2b1p3/2B1P3/5NP1/PPPP1P1P/RNBQ1RK1 b kq - 0 5");
        int evaluation2 = BoardEvaluation.evaluate(board2, PseudoLegalMoveFinder.getPseudoLegalMoves(board2, board2.isWhiteToMove()));

        Board board3 = Fen.fromFen("r1bqk2r/pppp1ppp/2n2n2/2b1p3/2B1P3/5N1P/PPPP1PP1/RNBQ1RK1 b kq - 0 5");
        int evaluation3 = BoardEvaluation.evaluate(board3, PseudoLegalMoveFinder.getPseudoLegalMoves(board3, board3.isWhiteToMove()));

        Assertions.assertTrue(evaluation1 > evaluation2);
        Assertions.assertTrue(evaluation1 > evaluation3);
    }
}