package chess.board;

import chess.board.model.Board;
import chess.board.model.BoardPiece;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class BoardTest {

    @Test
    void initializeFromFen() {
        String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w Kq - 2 3";

        Board board = Board.initializeFromFen(fen);
        BoardPiece[][] b = board.getBoardPieces();

        // Black pieces
        assertEquals(BoardPiece.BLACK_ROOK, b[0][0]);
        assertEquals(BoardPiece.BLACK_KING, b[0][4]);

        // Black pawns
        assertEquals(BoardPiece.BLACK_PAWN, b[1][3]);

        // Empty square
        assertNull(b[3][3]);

        // White pawns
        assertEquals(BoardPiece.WHITE_PAWN, b[6][4]);

        // White pieces
        assertEquals(BoardPiece.WHITE_ROOK, b[7][0]);
        assertEquals(BoardPiece.WHITE_KING, b[7][4]);

        assertEquals(true, board.isWhiteToMove());

        assertTrue(board.getCastlingRightsWhite().canCastleKingSide());
        assertTrue(board.getCastlingRightsBlack().canCastleQueenSide());
        assertFalse(board.getCastlingRightsBlack().canCastleKingSide());
        assertFalse(board.getCastlingRightsWhite().canCastleQueenSide());
        assertEquals(2, board.getHalfmoveClock());
        assertEquals(3, board.getFullmoveNumber());
    }

    @Test
    void initializeDefaultBoard() {
        Board board = Board.initializeDefaultBoard();
        BoardPiece[][] b = board.getBoardPieces();

        // Black back rank
        assertEquals(BoardPiece.BLACK_ROOK, b[0][0]);
        assertEquals(BoardPiece.BLACK_KNIGHT, b[0][1]);
        assertEquals(BoardPiece.BLACK_BISHOP, b[0][2]);
        assertEquals(BoardPiece.BLACK_QUEEN, b[0][3]);
        assertEquals(BoardPiece.BLACK_KING, b[0][4]);
        assertEquals(BoardPiece.BLACK_BISHOP, b[0][5]);
        assertEquals(BoardPiece.BLACK_KNIGHT, b[0][6]);
        assertEquals(BoardPiece.BLACK_ROOK, b[0][7]);

        // Black pawns
        for (int i = 0; i < 8; i++) {
            assertEquals(BoardPiece.BLACK_PAWN, b[1][i]);
        }

        // Empty squares
        for (int r = 2; r <= 5; r++) {
            for (int c = 0; c < 8; c++) {
                assertNull(b[r][c]);
            }
        }

        // White pawns
        for (int i = 0; i < 8; i++) {
            assertEquals(BoardPiece.WHITE_PAWN, b[6][i]);
        }

        // White back rank
        assertEquals(BoardPiece.WHITE_ROOK, b[7][0]);
        assertEquals(BoardPiece.WHITE_KNIGHT, b[7][1]);
        assertEquals(BoardPiece.WHITE_BISHOP, b[7][2]);
        assertEquals(BoardPiece.WHITE_QUEEN, b[7][3]);
        assertEquals(BoardPiece.WHITE_KING, b[7][4]);
        assertEquals(BoardPiece.WHITE_BISHOP, b[7][5]);
        assertEquals(BoardPiece.WHITE_KNIGHT, b[7][6]);
        assertEquals(BoardPiece.WHITE_ROOK, b[7][7]);

        assertTrue(board.getCastlingRightsWhite().canCastleKingSide());
        assertTrue(board.getCastlingRightsBlack().canCastleQueenSide());
        assertTrue(board.getCastlingRightsBlack().canCastleKingSide());
        assertTrue(board.getCastlingRightsWhite().canCastleQueenSide());
        assertEquals(0, board.getHalfmoveClock());
        assertEquals(1, board.getFullmoveNumber());
    }
}