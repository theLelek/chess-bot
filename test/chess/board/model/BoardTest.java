package chess.board.model;

import chess.BoardPosition;
import chess.Move.Move;
import chess.board.BoardPiece;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void initializeDefaultBoard() {
        Board board = Board.initializeDefaultBoard();

        assertTrue(board.getCastlingRightsWhite().canCastleKingSide());
        assertTrue(board.getCastlingRightsBlack().canCastleQueenSide());
        assertTrue(board.getCastlingRightsBlack().canCastleKingSide());
        assertTrue(board.getCastlingRightsWhite().canCastleQueenSide());
        assertEquals(0, board.getHalfmoveClock());
        assertEquals(1, board.getFullmoveNumber());
    }

    @Test
    void initializeFromFen() {
        String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w Kq - 2 3";

        Board board = Board.initializeFromFen(fen);

        assertEquals(true, board.isWhiteToMove());

        assertTrue(board.getCastlingRightsWhite().canCastleKingSide());
        assertTrue(board.getCastlingRightsBlack().canCastleQueenSide());
        assertFalse(board.getCastlingRightsBlack().canCastleKingSide());
        assertFalse(board.getCastlingRightsWhite().canCastleQueenSide());
        assertEquals(2, board.getHalfmoveClock());
        assertEquals(3, board.getFullmoveNumber());
    }

    @DisplayName("Italian line")
    @Test
    void move() {
        Board board = Board.initializeDefaultBoard();
        Move move1 = new Move(new BoardPosition("e2"), new BoardPosition("e4"));
        board.move(move1);

        assertTrue(board.getCastlingRightsWhite().canCastleKingSide() && board.getCastlingRightsWhite().canCastleQueenSide());
        assertTrue(board.getCastlingRightsBlack().canCastleKingSide() && board.getCastlingRightsBlack().canCastleQueenSide());
        assertEquals(new BoardPosition("e3"), board.getEnPassantTargetSquare());
        assertEquals(0, board.getHalfmoveClock());
        assertEquals(1, board.getFullmoveNumber());
        assertTrue(board.isBlackToMove());

        Move move2 = new Move(new BoardPosition("e7"), new BoardPosition("e5"));
        board.move(move2);

        assertTrue(board.getCastlingRightsBlack().canCastleKingSide() && board.getCastlingRightsBlack().canCastleQueenSide());

        assertEquals(new BoardPosition("e6"), board.getEnPassantTargetSquare());
        assertEquals(0, board.getHalfmoveClock());
        assertEquals(2, board.getFullmoveNumber());
        assertTrue(board.isWhiteToMove());
    }
}