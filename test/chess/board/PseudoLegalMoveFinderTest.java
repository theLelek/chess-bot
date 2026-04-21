package chess.board;

import chess.BoardPosition;
import chess.Move;
import chess.board.model.Board;
import chess.board.model.CastlingRights;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;


public class PseudoLegalMoveFinderTest {
    @Test
    @DisplayName("test find legal moves with black bishop at f8")
    public void testFindLegalMoves1() {
        Board board = Board.initializeFromFen("r1bqkbnr/pppp1ppp/2n5/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R w KQkq - 0 1");
        List<Move> legalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, false);
        List<Move> actual = getMovesFromStartingPosition(legalMoves, new BoardPosition(5, 0));
        String expected = "[from: f8 to: e7, from: f8 to: d6, from: f8 to: c5, from: f8 to: b4, from: f8 to: a3]";
        Assertions.assertEquals(expected, actual.toString());
    }

    @Test
    @DisplayName("Test find legal pawn move with black pawn on h7, can capture and move two fields foreward")
    public void testFindLegalPawnMoves2() {
        Board board = Board.initializeFromFen("rnbqkbnr/p1pppppp/6P1/8/1p6/8/PPPPPP1P/RNBQKBNR w KQkq - 0 1");
        List<Move> legalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, false);
        List<Move> actual = getMovesFromStartingPosition(legalMoves, new BoardPosition(7, 1));
        String expected = "[from: h7 to: h6, from: h7 to: h5, from: h7 to: g6]";
        Assertions.assertEquals(expected, actual.toString());
    }

    @Test
    @DisplayName("Test white knight on b1 in starting position can only move to a3 and c3")
    public void testFindLegalKnightMoves1() {
        Board board = Board.initializeFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        List<Move> legalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, true);
        List<Move> actual = getMovesFromStartingPosition(legalMoves, new BoardPosition(1, 7));
        String expected = "[from: b1 to: c3, from: b1 to: a3]";
        Assertions.assertEquals(expected, actual.toString());
    }

    @Test
    @DisplayName("Test white pawn on e3 blocked by black pawn on e4 has no legal moves")
    public void testFindLegalPawnMovesBlocked() {
        Board board = Board.initializeFromFen("4k3/8/8/8/4p3/4P3/8/4K3 w - - 0 1");
        List<Move> legalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, true);
        List<Move> actual = getMovesFromStartingPosition(legalMoves, new BoardPosition(4, 5));
        Assertions.assertTrue(actual.isEmpty(), "Expected blocked pawn to have no legal moves");
    }

    @Test
    @DisplayName("Test black bishop on f8 has no moves when blocked by own pawns")
    public void testFindLegalBishopMovesBlocked() {
        Board board = Board.initializeFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        List<Move> legalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, false);
        List<Move> actual = getMovesFromStartingPosition(legalMoves, new BoardPosition(5, 0));
        Assertions.assertTrue(actual.isEmpty(), "Expected f8 bishop to have no legal moves in starting position");
    }

    @Test
    @DisplayName("Test white pawn on e2 in starting position can move to e3 or e4")
    public void testFindLegalPawnMovesDoubleStep() {
        Board board = Board.initializeFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        List<Move> legalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, true);
        List<Move> actual = getMovesFromStartingPosition(legalMoves, new BoardPosition(4, 6));
        String expected = "[from: e2 to: e3, from: e2 to: e4]";
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

    @Test
    void getPseudoLegalCastlingRights() {
        Board board = Board.initializeDefaultBoard();
        CastlingRights castlingRights = PseudoLegalMoveFinder.getPseudoLegalCastlingRights(board, true);
        Assertions.assertFalse(castlingRights.canCastleKingSide());
        Assertions.assertFalse(castlingRights.canCastleQueenSide());
    }

    @Test
    void white_canCastleBothSides_whenAllSquaresClear() {
        Board board = Board.initializeFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w KQkq - 0 1");
        CastlingRights result = PseudoLegalMoveFinder.getPseudoLegalCastlingRights(board, true);
        Assertions.assertTrue(result.canCastleKingSide());
        Assertions.assertTrue(result.canCastleQueenSide());
    }

    @Test
    void white_cannotCastleEitherSide_whenSquaresBlocked() {
        Board board = Board.initializeDefaultBoard();
        CastlingRights result = PseudoLegalMoveFinder.getPseudoLegalCastlingRights(board, true);
        Assertions.assertFalse(result.canCastleKingSide());
        Assertions.assertFalse(result.canCastleQueenSide());
    }

    @Test
    void black_canCastleBothSides_whenAllSquaresClear() {
        Board board = Board.initializeFromFen("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1");
        CastlingRights result = PseudoLegalMoveFinder.getPseudoLegalCastlingRights(board, false);
        Assertions.assertTrue(result.canCastleKingSide());
        Assertions.assertTrue(result.canCastleQueenSide());
    }

    @Test
    void getPromotionMoves_returnsEmpty_whenNoPawnsNearPromotion() {
        Board board = Board.initializeDefaultBoard();
        List<Move> pseudoLegalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, true);
        List<Move> promotionMoves = PseudoLegalMoveFinder.getPromotionMoves(board, pseudoLegalMoves, true);
        Assertions.assertTrue(promotionMoves.isEmpty());
    }

    @Test
    void getPromotionMoves_returnsPromotionMove_whenWhitePawnOnSeventhRank() {
        Board board = Board.initializeFromFen("8/4P3/8/8/8/8/8/4K2k w - - 0 1");
        List<Move> pseudoLegalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, true);
        List<Move> promotionMoves = PseudoLegalMoveFinder.getPromotionMoves(board, pseudoLegalMoves, true);
        Assertions.assertFalse(promotionMoves.isEmpty());
    }

    @Test
    void getPromotionMoves_returnsPromotionMove_whenBlackPawnOnSecondRank() {
        Board board = Board.initializeFromFen("4K2k/8/8/8/8/8/4p3/8 b - - 0 1");
        List<Move> pseudoLegalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, false);
        List<Move> promotionMoves = PseudoLegalMoveFinder.getPromotionMoves(board, pseudoLegalMoves, false);
        Assertions.assertFalse(promotionMoves.isEmpty());
    }

    @Test
    void getPromotionMoves_returnsMultiplePromotionMoves_whenMultiplePawnsOnSeventhRank() {
        Board board = Board.initializeFromFen("8/2P1P3/8/8/8/8/8/4K2k w - - 0 1");
        List<Move> pseudoLegalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, true);
        List<Move> promotionMoves = PseudoLegalMoveFinder.getPromotionMoves(board, pseudoLegalMoves, true);
        Assertions.assertEquals(2, promotionMoves.size());
    }
}