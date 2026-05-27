package chess.board.model;

import chess.BoardPosition;
import chess.Move.Move;
import chess.board.BoardPiece;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @DisplayName("Italian line")
    @Test
    void move() {
        Board board = Board.initializeDefaultBoard();
        Move move1 = new Move(new BoardPosition("e2"), new BoardPosition("e4"));
        board.move(move1);

        assertNull(board.getBoardPiece(move1.from()));
        assertEquals(BoardPiece.WHITE_PAWN, board.getBoardPiece(move1.to()));
        assertEquals(32, amountOfPiecesOnBoard(board));
        assertTrue(board.getCastlingRightsWhite().canCastleKingSide() && board.getCastlingRightsWhite().canCastleQueenSide());
        assertTrue(board.getCastlingRightsBlack().canCastleKingSide() && board.getCastlingRightsBlack().canCastleQueenSide());
        assertEquals(new BoardPosition("e3"), board.getEnPassantTargetSquare());
        assertEquals(0, board.getHalfmoveClock());
        assertEquals(1, board.getFullmoveNumber());
        assertTrue(board.isBlackToMove());

        Move move2 = new Move(new BoardPosition("e7"), new BoardPosition("e5"));
        board.move(move2);

        assertNull(board.getBoardPiece(move2.from()));
        assertEquals(BoardPiece.BLACK_PAWN, board.getBoardPiece(move2.to()));
        assertEquals(32, amountOfPiecesOnBoard(board));
        assertTrue(board.getCastlingRightsBlack().canCastleKingSide() && board.getCastlingRightsBlack().canCastleQueenSide());

        assertEquals(new BoardPosition("e6"), board.getEnPassantTargetSquare());
        assertEquals(0, board.getHalfmoveClock());
        assertEquals(2, board.getFullmoveNumber());
        assertTrue(board.isWhiteToMove());
    }

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

    private static int amountOfPiecesOnBoard(Board board) {
        int amount = 0;
        for (BoardPiece[] row : board.getBoardPieces()) {
            for (BoardPiece piece : row) {
                if (piece != null) amount++;
            }
        }
        return amount;
    }
}