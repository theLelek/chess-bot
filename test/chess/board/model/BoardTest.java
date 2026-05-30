package chess.board.model;

import chess.Move.CastlingMove;
import chess.Move.EnPassantMove;
import chess.Move.Move;
import chess.Move.PromotionMove;
import chess.board.BoardPiece;
import chess.board.OccupancyBitboard;
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
        assertTrue(board.isWhiteToMove());
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
                {BoardPiece.WHITE_KNIGHT, null, null, BoardPiece.WHITE_QUEEN, null, null, null, null},
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
        board.move(move5);
        assertEquals(Board.initializeFromFen("r1bqkbnr/pppp1ppp/2n5/4p3/2B1P3/5N2/PPPP1PPP/RNBQK2R b KQkq - 3 3"), board);

        Move move6 = new Move("f8", "c5");
        board.move(move6);
        assertEquals(Board.initializeFromFen("r1bqk1nr/pppp1ppp/2n5/2b1p3/2B1P3/5N2/PPPP1PPP/RNBQK2R w KQkq - 4 4"), board);

        Move move7 = new CastlingMove("e1", "g1");
        board.move(move7);
        assertEquals(Board.initializeFromFen("r1bqk1nr/pppp1ppp/2n5/2b1p3/2B1P3/5N2/PPPP1PPP/RNBQ1RK1 b kq - 5 4"), board);

        Move move8 = new Move("g8", "f6");
        board.move(move8);
        assertEquals(Board.initializeFromFen("r1bqk2r/pppp1ppp/2n2n2/2b1p3/2B1P3/5N2/PPPP1PPP/RNBQ1RK1 w kq - 6 5"), board);

        Move move9 = new Move("b1", "c3");
        board.move(move9);
        assertEquals(Board.initializeFromFen("r1bqk2r/pppp1ppp/2n2n2/2b1p3/2B1P3/2N2N2/PPPP1PPP/R1BQ1RK1 b kq - 7 5"), board);
    }

    @Test
    void move_castle() {
        // white king side castle
        Board board1 = Board.initializeFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/R3KBNR w KQkq - 0 1");
        Move move1 = new CastlingMove("e1", "c1");
        board1.move(move1);
        assertEquals(Board.initializeFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/2KR1BNR b kq - 1 1"), board1);

        // white queen side castle
        Board board2 = Board.initializeFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQK2R w KQkq - 0 1");
        Move move2 = new CastlingMove("e1", "g1");
        board2.move(move2);
        assertEquals(Board.initializeFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQ1RK1 b kq - 1 1"), board2);

        // black king side castle
        Board board3 = Board.initializeFromFen("rnbqk2r/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1");
        Move move3 = new CastlingMove("e8", "g8");
        board3.move(move3);
        assertEquals(Board.initializeFromFen("rnbq1rk1/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQ - 1 2"), board3);

        // black queen side castle
        Board board4 = Board.initializeFromFen("r3kbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1");
        Move move4 = new CastlingMove("e8", "c8");
        board4.move(move4);
        assertEquals(Board.initializeFromFen("2kr1bnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQ - 1 2"), board4);
    }

    @Test
    void move_promotion() {
        // white pawn promoting
        Board board1 = Board.initializeFromFen("8/P7/8/8/8/8/8/k6K w - - 0 1");
        Move move1 = new PromotionMove("a7", "a8", BoardPiece.WHITE_ROOK);
        board1.move(move1);
        assertEquals(Board.initializeFromFen("R7/8/8/8/8/8/8/k6K b - - 0 1"), board1);

        // white pawn promoting while capturing black rook
        Board board2 = Board.initializeFromFen("4r3/5P2/8/8/8/8/8/k6K w - - 0 1");
        Move move2 = new PromotionMove("f7", "e8", BoardPiece.WHITE_QUEEN);
        board2.move(move2);
        assertEquals(Board.initializeFromFen("4Q3/8/8/8/8/8/8/k6K b - - 0 1"), board2);


        // black pawn promoting
        Board board3 = Board.initializeFromFen("3r4/8/8/3k4/8/6K1/3p4/R7 b - - 0 1");
        Move move3 = new PromotionMove("d2", "d1", BoardPiece.BLACK_KNIGHT);
        board3.move(move3);
        assertEquals(Board.initializeFromFen("3r4/8/8/3k4/8/6K1/8/R2n4 w - - 0 2"), board3);

        // black pawn promoting while capturing white bishop
        Board board4 = Board.initializeFromFen("3r4/8/8/3k4/8/8/3p4/RKBn4 b - - 0 2");
        Move move4 = new PromotionMove("d2", "c1", BoardPiece.BLACK_BISHOP);
        board4.move(move4);
        assertEquals(Board.initializeFromFen("3r4/8/8/3k4/8/8/8/RKbn4 w - - 0 3"), board4);
    }

    @Test
    void move_enPassant() {
        // basic en passant white to move
        Board board1 = Board.initializeFromFen("8/7k/8/3pP3/8/8/8/RK6 w - d6 0 4");
        Move move1 = new EnPassantMove("e5", "d6");
        board1.move(move1);
        assertEquals(Board.initializeFromFen("8/7k/3P4/8/8/8/8/RK6 b - - 0 4"), board1);

        // basic en passant black to move

        Board board2 = Board.initializeFromFen("8/7k/8/8/3Pp3/8/8/RK6 b - d3 0 4");
        Move move2 = new EnPassantMove("e4", "d3");
        board2.move(move2);
        assertEquals(Board.initializeFromFen("8/7k/8/8/8/3p4/8/RK6 w - - 0 5"), board2);
    }


    private static BoardPiece[] board2dToPieceList(BoardPiece[][] board){
        ArrayList<BoardPiece> result = new ArrayList<>();
        for (int i = board.length - 1; i >= 0; i--) {
            result.addAll(Arrays.asList(board[i]));
        }
        return result.toArray(BoardPiece[]::new);
    }
}