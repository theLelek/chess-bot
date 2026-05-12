package chess.board.model;

import chess.BoardPosition;
import chess.Move.Move;
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
        assertEquals(32, board.getPiecesIndexes().size());
        assertFalse(board.getPiecesIndexes().contains(new BoardPosition("e2")));
        assertTrue(board.getPiecesIndexes().contains(new BoardPosition("e4")));
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
        assertEquals(32, board.getPiecesIndexes().size());
        assertFalse(board.getPiecesIndexes().contains(new BoardPosition("e7")));
        assertTrue(board.getPiecesIndexes().contains(new BoardPosition("e5")));
        assertTrue(board.getCastlingRightsWhite().canCastleKingSide() && board.getCastlingRightsWhite().canCastleQueenSide());
        assertTrue(board.getCastlingRightsBlack().canCastleKingSide() && board.getCastlingRightsBlack().canCastleQueenSide());

        assertEquals(new BoardPosition("e6"), board.getEnPassantTargetSquare());
        assertEquals(0, board.getHalfmoveClock());
        assertEquals(2, board.getFullmoveNumber());
        assertTrue(board.isWhiteToMove());
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