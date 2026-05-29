package chess.board.model;

import chess.BoardPosition;
import chess.Move.CastlingMove;
import chess.Move.Move;
import chess.board.BoardPiece;
import chess.board.OccupancyBitboard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void initializeFromFen_defaultPosition() {
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

        BitBoardState bitBoardState = board.getBitBoardState();
        assertEquals(65280, bitBoardState.getBitboard(BoardPiece.WHITE_PAWN));
        assertEquals(129, bitBoardState.getBitboard(BoardPiece.WHITE_ROOK));
        assertEquals(66, bitBoardState.getBitboard(BoardPiece.WHITE_KNIGHT));
        assertEquals(36, bitBoardState.getBitboard(BoardPiece.WHITE_BISHOP));
        assertEquals(8, bitBoardState.getBitboard(BoardPiece.WHITE_QUEEN));
        assertEquals(16, bitBoardState.getBitboard(BoardPiece.WHITE_KING));

        assertEquals(71776119061217280L, bitBoardState.getBitboard(BoardPiece.BLACK_PAWN));
        assertEquals(-9151314442816847872L, bitBoardState.getBitboard(BoardPiece.BLACK_ROOK));
        assertEquals(4755801206503243776L, bitBoardState.getBitboard(BoardPiece.BLACK_KNIGHT));
        assertEquals(2594073385365405696L, bitBoardState.getBitboard(BoardPiece.BLACK_BISHOP));
        assertEquals(576460752303423488L, bitBoardState.getBitboard(BoardPiece.BLACK_QUEEN));
        assertEquals(1152921504606846976L, bitBoardState.getBitboard(BoardPiece.BLACK_KING));

        assertTrue(board.getCastlingRightsWhite().canCastleKingSide());
        assertTrue(board.getCastlingRightsWhite().canCastleQueenSide());
        assertTrue(board.getCastlingRightsBlack().canCastleQueenSide());
        assertTrue(board.getCastlingRightsBlack().canCastleKingSide());

        assertEquals(0, board.getHalfmoveClock());
        assertEquals(1, board.getFullmoveNumber());
        assertNull(board.getEnPassantTargetSquare());
    }

    @Test
    void initializeFromFen_OnlyWhitePawns() {
        Board board = Board.initializeFromFen("8/8/8/8/8/8/PPPPPPPP/8 w KQkq - 0 1");
        BitBoardState bitBoardState = board.getBitBoardState();
        assertEquals(65280, bitBoardState.getBitboard(BoardPiece.WHITE_PAWN));
        assertEquals(65280, bitBoardState.getBitboard(OccupancyBitboard.WHITE_PIECES));
        assertEquals(65280, bitBoardState.getBitboard(OccupancyBitboard.ALL_PIECES));

        BoardPiece[][] expectedPieceList = new BoardPiece[][]{
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN},
                {null, null, null, null, null, null, null, null},
        };
        assertArrayEquals(board2dToPieceList(expectedPieceList), board.getPieceList());

        assertTrue(board.getCastlingRightsWhite().canCastleKingSide());
        assertTrue(board.getCastlingRightsWhite().canCastleQueenSide());
        assertTrue(board.getCastlingRightsBlack().canCastleQueenSide());
        assertTrue(board.getCastlingRightsBlack().canCastleKingSide());

        assertEquals(0, board.getHalfmoveClock());
        assertEquals(1, board.getFullmoveNumber());
        assertNull(board.getEnPassantTargetSquare());
    }

    @Test
    void initializeFromFen2() {
        Board board = Board.initializeFromFen("rnbqkbnr/ppp1pppp/8/3pP1B1/1P1P4/N2Q4/P1P2PPP/R3K2R w KQkq d6 0 4");
        BoardPiece[][] expectedPieceList = new BoardPiece[][]{
                {BoardPiece.BLACK_ROOK, BoardPiece.BLACK_KNIGHT, BoardPiece.BLACK_BISHOP, BoardPiece.BLACK_QUEEN, BoardPiece.BLACK_KING, BoardPiece.BLACK_BISHOP, BoardPiece.BLACK_KNIGHT, BoardPiece.BLACK_ROOK},
                {BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, null, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN},
                {null, null, null, null, null, null, null, null},
                {null, null, null, BoardPiece.BLACK_PAWN, BoardPiece.WHITE_PAWN, null, BoardPiece.WHITE_BISHOP, null},
                {null, BoardPiece.WHITE_PAWN, null, BoardPiece.WHITE_PAWN, null, null, null, null},
                {null, null, null, BoardPiece.WHITE_QUEEN, null, null, null, null},
                {BoardPiece.WHITE_PAWN, null, BoardPiece.WHITE_PAWN, null, null, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN},
                {BoardPiece.WHITE_ROOK, null, null, null, BoardPiece.WHITE_KING, null, null, BoardPiece.WHITE_ROOK}
        };
        assertArrayEquals(board2dToPieceList(expectedPieceList), board.getPieceList());
    }

    @Test
    void move_italienLine() {
        Board board = Board.initializeDefaultBoard();
        Move move1 = new Move("e2", "e4");
        board.move(move1);
        assertEquals(Board.initializeFromFen("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1"), board);

        Move move2 = new Move("e7", "e5");
        board.move(move2);
        assertEquals(Board.initializeFromFen("rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w KQkq e6 0 2"), board);

        Move move3 = new Move("g1", "f3");
        board.move(move3);
        assertEquals(Board.initializeFromFen("rnbqkbnr/pppp1ppp/8/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2"), board);

        Move move4 = new Move("b8", "c6");
        board.move(move4);
        assertEquals(Board.initializeFromFen("r1bqkbnr/pppp1ppp/2n5/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R w KQkq - 2 3"), board);

        Move move5 = new Move("f1", "c4");
        assertEquals(Board.initializeFromFen("r1bqkbnr/pppp1ppp/2n5/4p3/2B1P3/5N2/PPPP1PPP/RNBQK2R b KQkq - 3 3"), board);
    }

    private static BoardPiece[] board2dToPieceList(BoardPiece[][] board){
        ArrayList<BoardPiece> result = new ArrayList<>();
        for (int i = board.length - 1; i >= 0; i--) {
            result.addAll(Arrays.asList(board[i]));
        }
        return result.toArray(BoardPiece[]::new);
    }
}