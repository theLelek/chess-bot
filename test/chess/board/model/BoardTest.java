package chess.board.model;

import chess.BoardPosition;
import chess.Move.CastlingMove;
import chess.Move.Move;
import chess.board.BoardPiece;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void move_italienLine() {
        Board board = Board.initializeDefaultBoard();

        BoardPiece[][] expectedPieceList = new BoardPiece[][]{
                {BoardPiece.BLACK_ROOK, BoardPiece.BLACK_KNIGHT, BoardPiece.BLACK_BISHOP, BoardPiece.BLACK_QUEEN, BoardPiece.BLACK_KING, BoardPiece.BLACK_BISHOP, BoardPiece.BLACK_KNIGHT, BoardPiece.BLACK_ROOK},
                {BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN},
                {BoardPiece.WHITE_ROOK, BoardPiece.WHITE_KNIGHT, BoardPiece.WHITE_BISHOP, BoardPiece.WHITE_QUEEN, BoardPiece.WHITE_KING, BoardPiece.WHITE_BISHOP, BoardPiece.WHITE_KNIGHT, BoardPiece.WHITE_ROOK}
        };
        assertArrayEquals(board2dToPieceList(expectedPieceList), board.getPieceList());

        assertTrue(board.getCastlingRightsWhite().canCastleKingSide());
        assertTrue(board.getCastlingRightsWhite().canCastleQueenSide());
        assertTrue(board.getCastlingRightsBlack().canCastleQueenSide());
        assertTrue(board.getCastlingRightsBlack().canCastleKingSide());

        assertEquals(0, board.getHalfmoveClock());
        assertEquals(1, board.getFullmoveNumber());
        assertNull(board.getEnPassantTargetSquare());

        Move move1 = new Move("e2", "e4");
        board.move(move1);
        BoardPiece[][] expectedPieceListAfterMove1 = new BoardPiece[][]{
                {BoardPiece.BLACK_ROOK, BoardPiece.BLACK_KNIGHT, BoardPiece.BLACK_BISHOP, BoardPiece.BLACK_QUEEN, BoardPiece.BLACK_KING, BoardPiece.BLACK_BISHOP, BoardPiece.BLACK_KNIGHT, BoardPiece.BLACK_ROOK},
                {BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, BoardPiece.WHITE_PAWN, null, null, null},
                {null, null, null, null, null, null, null, null},
                {BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, null, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN},
                {BoardPiece.WHITE_ROOK, BoardPiece.WHITE_KNIGHT, BoardPiece.WHITE_BISHOP, BoardPiece.WHITE_QUEEN, BoardPiece.WHITE_KING, BoardPiece.WHITE_BISHOP, BoardPiece.WHITE_KNIGHT, BoardPiece.WHITE_ROOK}
        };

        assertArrayEquals(board2dToPieceList(expectedPieceListAfterMove1), board.getPieceList());

        assertTrue(board.getCastlingRightsWhite().canCastleKingSide());
        assertTrue(board.getCastlingRightsWhite().canCastleQueenSide());
        assertTrue(board.getCastlingRightsBlack().canCastleQueenSide());
        assertTrue(board.getCastlingRightsBlack().canCastleKingSide());

        assertEquals(0, board.getHalfmoveClock());
        assertEquals(1, board.getFullmoveNumber());
        assertEquals(new BoardPosition("e3"), board.getEnPassantTargetSquare());
        assertEquals(new BoardPosition("e4"), board.getEnPassantPiecePosition());

        Move move2 = new Move("e7", "e5");
        board.move(move2);
        BoardPiece[][] expectedPieceListAfterMove2 = new BoardPiece[][]{
                {BoardPiece.BLACK_ROOK, BoardPiece.BLACK_KNIGHT, BoardPiece.BLACK_BISHOP, BoardPiece.BLACK_QUEEN, BoardPiece.BLACK_KING, BoardPiece.BLACK_BISHOP, BoardPiece.BLACK_KNIGHT, BoardPiece.BLACK_ROOK},
                {BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, null, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, BoardPiece.BLACK_PAWN, null, null, null},
                {null, null, null, null, BoardPiece.WHITE_PAWN, null, null, null},
                {null, null, null, null, null, null, null, null},
                {BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, null, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN},
                {BoardPiece.WHITE_ROOK, BoardPiece.WHITE_KNIGHT, BoardPiece.WHITE_BISHOP, BoardPiece.WHITE_QUEEN, BoardPiece.WHITE_KING, BoardPiece.WHITE_BISHOP, BoardPiece.WHITE_KNIGHT, BoardPiece.WHITE_ROOK}
        };
        assertArrayEquals(board2dToPieceList(expectedPieceListAfterMove2), board.getPieceList());

        assertTrue(board.getCastlingRightsWhite().canCastleKingSide());
        assertTrue(board.getCastlingRightsWhite().canCastleQueenSide());
        assertTrue(board.getCastlingRightsBlack().canCastleQueenSide());
        assertTrue(board.getCastlingRightsBlack().canCastleKingSide());

        assertEquals(0, board.getHalfmoveClock());
        assertEquals(2, board.getFullmoveNumber());
        assertEquals(new BoardPosition("e6"), board.getEnPassantTargetSquare());
        assertEquals(new BoardPosition("e5"), board.getEnPassantPiecePosition());

        Move move3 = new Move("g1", "f3");
        board.move(move3);
        BoardPiece[][] expectedPieceListAfterMove3 = new BoardPiece[][]{
                {BoardPiece.BLACK_ROOK, BoardPiece.BLACK_KNIGHT, BoardPiece.BLACK_BISHOP, BoardPiece.BLACK_QUEEN, BoardPiece.BLACK_KING, BoardPiece.BLACK_BISHOP, BoardPiece.BLACK_KNIGHT, BoardPiece.BLACK_ROOK},
                {BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, null, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, BoardPiece.BLACK_PAWN, null, null, null},
                {null, null, null, null, BoardPiece.WHITE_PAWN, null, null, null},
                {null, null, null, null, null, BoardPiece.WHITE_KNIGHT, null, null},
                {BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, null, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN},
                {BoardPiece.WHITE_ROOK, BoardPiece.WHITE_KNIGHT, BoardPiece.WHITE_BISHOP, BoardPiece.WHITE_QUEEN, BoardPiece.WHITE_KING, BoardPiece.WHITE_BISHOP, null, BoardPiece.WHITE_ROOK}
        };
        assertArrayEquals(board2dToPieceList(expectedPieceListAfterMove3), board.getPieceList());

        assertTrue(board.getCastlingRightsWhite().canCastleKingSide());
        assertTrue(board.getCastlingRightsWhite().canCastleQueenSide());
        assertTrue(board.getCastlingRightsBlack().canCastleQueenSide());
        assertTrue(board.getCastlingRightsBlack().canCastleKingSide());

        assertEquals(1, board.getHalfmoveClock());
        assertEquals(2, board.getFullmoveNumber());
        assertNull(board.getEnPassantTargetSquare());

        Move move4 = new Move("b8", "c6");
        board.move(move4);
        BoardPiece[][] expectedPieceListAfterMove4 = new BoardPiece[][]{
                {BoardPiece.BLACK_ROOK, null, BoardPiece.BLACK_BISHOP, BoardPiece.BLACK_QUEEN, BoardPiece.BLACK_KING, BoardPiece.BLACK_BISHOP, BoardPiece.BLACK_KNIGHT, BoardPiece.BLACK_ROOK},
                {BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, null, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN},
                {null, null, BoardPiece.BLACK_KNIGHT, null, null, null, null, null},
                {null, null, null, null, BoardPiece.BLACK_PAWN, null, null, null},
                {null, null, null, null, BoardPiece.WHITE_PAWN, null, null, null},
                {null, null, null, null, null, BoardPiece.WHITE_KNIGHT, null, null},
                {BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, null, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN},
                {BoardPiece.WHITE_ROOK, BoardPiece.WHITE_KNIGHT, BoardPiece.WHITE_BISHOP, BoardPiece.WHITE_QUEEN, BoardPiece.WHITE_KING, BoardPiece.WHITE_BISHOP, null, BoardPiece.WHITE_ROOK}
        };
        assertArrayEquals(board2dToPieceList(expectedPieceListAfterMove4), board.getPieceList());

        assertTrue(board.getCastlingRightsWhite().canCastleKingSide());
        assertTrue(board.getCastlingRightsWhite().canCastleQueenSide());
        assertTrue(board.getCastlingRightsBlack().canCastleQueenSide());
        assertTrue(board.getCastlingRightsBlack().canCastleKingSide());

        assertEquals(2, board.getHalfmoveClock());
        assertEquals(3, board.getFullmoveNumber());
        assertNull(board.getEnPassantTargetSquare());

        Move move5 = new Move("f1", "c4");
        board.move(move5);
        BoardPiece[][] expectedPieceListAfterMove5 = new BoardPiece[][]{
                {BoardPiece.BLACK_ROOK, null, BoardPiece.BLACK_BISHOP, BoardPiece.BLACK_QUEEN, BoardPiece.BLACK_KING, BoardPiece.BLACK_BISHOP, BoardPiece.BLACK_KNIGHT, BoardPiece.BLACK_ROOK},
                {BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, null, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN},
                {null, null, BoardPiece.BLACK_KNIGHT, null, null, null, null, null},
                {null, null, null, null, BoardPiece.BLACK_PAWN, null, null, null},
                {null, null, BoardPiece.WHITE_BISHOP, null, BoardPiece.WHITE_PAWN, null, null, null},
                {null, null, null, null, null, BoardPiece.WHITE_KNIGHT, null, null},
                {BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, null, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN},
                {BoardPiece.WHITE_ROOK, BoardPiece.WHITE_KNIGHT, BoardPiece.WHITE_BISHOP, BoardPiece.WHITE_QUEEN, BoardPiece.WHITE_KING, null, null, BoardPiece.WHITE_ROOK}
        };

        assertArrayEquals(board2dToPieceList(expectedPieceListAfterMove5), board.getPieceList());

        assertTrue(board.getCastlingRightsWhite().canCastleKingSide());
        assertTrue(board.getCastlingRightsWhite().canCastleQueenSide());
        assertTrue(board.getCastlingRightsBlack().canCastleQueenSide());
        assertTrue(board.getCastlingRightsBlack().canCastleKingSide());

        assertEquals(3, board.getHalfmoveClock());
        assertEquals(3, board.getFullmoveNumber());
        assertNull(board.getEnPassantTargetSquare());

        Move move6 = new Move("f8", "c5");
        board.move(move6);
        BoardPiece[][] expectedPieceListAfterMove6 = new BoardPiece[][]{
                {BoardPiece.BLACK_ROOK, null, BoardPiece.BLACK_BISHOP, BoardPiece.BLACK_QUEEN, BoardPiece.BLACK_KING, null, BoardPiece.BLACK_KNIGHT, BoardPiece.BLACK_ROOK},
                {BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, null, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN},
                {null, null, BoardPiece.BLACK_KNIGHT, null, null, null, null, null},
                {null, null, BoardPiece.BLACK_BISHOP, null, BoardPiece.BLACK_PAWN, null, null, null},
                {null, null, BoardPiece.WHITE_BISHOP, null, BoardPiece.WHITE_PAWN, null, null, null},
                {null, null, null, null, null, BoardPiece.WHITE_KNIGHT, null, null},
                {BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, null, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN},
                {BoardPiece.WHITE_ROOK, BoardPiece.WHITE_KNIGHT, BoardPiece.WHITE_BISHOP, BoardPiece.WHITE_QUEEN, BoardPiece.WHITE_KING, null, null, BoardPiece.WHITE_ROOK}
        };
        assertArrayEquals(board2dToPieceList(expectedPieceListAfterMove6), board.getPieceList());

        assertTrue(board.getCastlingRightsWhite().canCastleKingSide());
        assertTrue(board.getCastlingRightsWhite().canCastleQueenSide());
        assertTrue(board.getCastlingRightsBlack().canCastleQueenSide());
        assertTrue(board.getCastlingRightsBlack().canCastleKingSide());

        assertEquals(4, board.getHalfmoveClock());
        assertEquals(4, board.getFullmoveNumber());
        assertNull(board.getEnPassantTargetSquare());

        Move move7 = new CastlingMove("e1", "g1");
        board.move(move7);
        BoardPiece[][] expectedPieceListAfterMove7 = new BoardPiece[][]{
                {BoardPiece.BLACK_ROOK, null, BoardPiece.BLACK_BISHOP, BoardPiece.BLACK_QUEEN, BoardPiece.BLACK_KING, null, BoardPiece.BLACK_KNIGHT, BoardPiece.BLACK_ROOK},
                {BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, null, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN},
                {null, null, BoardPiece.BLACK_KNIGHT, null, null, null, null, null},
                {null, null, BoardPiece.BLACK_BISHOP, null, BoardPiece.BLACK_PAWN, null, null, null},
                {null, null, BoardPiece.WHITE_BISHOP, null, BoardPiece.WHITE_PAWN, null, null, null},
                {null, null, null, null, null, BoardPiece.WHITE_KNIGHT, null, null},
                {BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, null, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN},
                {BoardPiece.WHITE_ROOK, BoardPiece.WHITE_KNIGHT, BoardPiece.WHITE_BISHOP, BoardPiece.WHITE_QUEEN, null, BoardPiece.WHITE_ROOK, BoardPiece.WHITE_KING, null}
        };
        assertArrayEquals(board2dToPieceList(expectedPieceListAfterMove7), board.getPieceList());

        assertFalse(board.getCastlingRightsWhite().canCastleKingSide());
        assertFalse(board.getCastlingRightsWhite().canCastleQueenSide());
        assertTrue(board.getCastlingRightsBlack().canCastleQueenSide());
        assertTrue(board.getCastlingRightsBlack().canCastleKingSide());

        assertEquals(5, board.getHalfmoveClock());
        assertEquals(4, board.getFullmoveNumber());
        assertNull(board.getEnPassantTargetSquare());

    }

    private static BoardPiece[] board2dToPieceList(BoardPiece[][] board){
        ArrayList<BoardPiece> result = new ArrayList<>();
        for (int i = board.length - 1; i >= 0; i--) {
            result.addAll(Arrays.asList(board[i]));
        }
        return result.toArray(BoardPiece[]::new);
    }
}