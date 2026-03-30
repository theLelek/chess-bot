package chess.board;

import chess.BoardPosition;
import chess.Move;
import chess.board.model.Board;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;


public class PseudoLegalMoveFinderTest {
    @Test
    @DisplayName("test find legal moves with black bishop at f8")
    public void testFindLegalMoves1() {
        Board board = Board.initializeFromFen("r1bqkbnr/pppp1ppp/2n5/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R w KQkq - 0 1");
        List<Move> legalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board);
        Assertions.assertNotNull(legalMoves);
        List<Move> actual = getMovesFromStartingPosition(legalMoves, new BoardPosition(5, 0));
        String expected = "[from: f8 to: e7, from: f8 to: d6, from: f8 to: c5, from: f8 to: b4, from: f8 to: a3]";
        Assertions.assertEquals(expected, actual.toString());
    }

    @Test
    @DisplayName("Test find legal pawn move with white pawn on e5, can capture en passant and move forward")
    public void testFindLegalPawnMoves() {
        Board board = Board.initializeFromFen("rnbqkbnr/ppp1ppp1/7p/3pP3/8/8/PPPP1PPP/RNBQKBNR w KQkq d6 0 3");
        List<Move> legalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board);
        Assertions.assertNotNull(legalMoves);
        List<Move> actual = getMovesFromStartingPosition(legalMoves, new BoardPosition(4, 3));
        String expected = "[from: e5 to: d6, from: e5 to: e6]";
        Assertions.assertEquals(expected, actual.toString());
    }

    @Test
    @DisplayName("Test find legal pawn move with black pawn on h7, can capture and move two fields foreward")
    public void testFindLegalPawnMoves2() {
        Board board = Board.initializeFromFen("rnbqkbnr/p1pppppp/6P1/8/1p6/8/PPPPPP1P/RNBQKBNR w KQkq - 0 1");
        List<Move> legalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board);
        Assertions.assertNotNull(legalMoves);
        List<Move> actual = getMovesFromStartingPosition(legalMoves, new BoardPosition(7, 1));
        String expected = "[from: h7 to: h6, from: h7 to: h5, from: h7 to: g6]";
        Assertions.assertEquals(expected, actual.toString());
    }

    private static List<Move> getMovesFromStartingPosition(List<Move> legalMoves, BoardPosition boardPosition) {
        List<Move> moves = new ArrayList<>();
        for (Move move : legalMoves) {
            if (move.from().equals(boardPosition)) {
                moves.add(move);
            }
        }
        return moves;
    }
}