package chess.board;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


class BoardTest {

    @Test
    void initializeBoardByFen() {
        String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";

        Board board = Board.initializeBoardByFen(fen);
        BoardPiece[][] b = board.getBoard();

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
    }

    @Test
    void initializeDefaultBoard() {
        Board board = Board.initializeDefaultBoard();
        BoardPiece[][] b = board.getBoard();

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
    }
}