package chess.board.model;

import chess.BoardPosition;
import chess.Move.Move;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.management.modelmbean.ModelMBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @DisplayName("Italian line")
    @Test
    void move() {
        Board board = Board.initializeDefaultBoard();
        Move move1 = new Move(new BoardPosition("e2"), new BoardPosition("e4"));
        board.move(move1);

        assertEquals(BoardPiece.WHITE_PAWN, board.getBoardPiece(move1.to()));
        assertNull(board.getBoardPiece(move1.from()));
        assertEquals(32, amountOfPiecesOnBoard(board));
        assertEquals(32, board.getPiecesIndexes().size());
        assertFalse(board.getPiecesIndexes().contains(new BoardPosition("e2")));
        assertTrue(board.getPiecesIndexes().contains(new BoardPosition("e4")));
        assertTrue(board.getCastlingRightsWhite().canCastleKingSide() && board.getCastlingRightsWhite().canCastleQueenSide());
        assertTrue(board.getCastlingRightsBlack().canCastleKingSide() && board.getCastlingRightsBlack().canCastleQueenSide());
        assertEquals(new BoardPosition("e4"), board.getPossibleEnPassant());
        assertEquals(0, board.getHalfmoveClock());
        assertEquals(1, board.getFullmoveNumber());
        assertTrue(board.isBlackToMove());

        Move move2 = new Move(new BoardPosition("e7"), new BoardPosition("e5"));
        board.move(move2);

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