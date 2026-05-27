package chess.board.model;

import chess.board.BoardPiece;
import chess.board.OccupancyBitboard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BitBoardStateTest {

    @Test
    void initializeFromFen_onlyWhitePawns() {
        String fen = "8/8/8/8/8/8/PPPPPPPP/8";
        BitBoardState bitBoardState = BitBoardState.initializeFromFen(fen);
        assertEquals(65280, bitBoardState.getBitboard(BoardPiece.WHITE_PAWN));
        assertEquals(65280, bitBoardState.getBitboard(OccupancyBitboard.WHITE_PIECES));
        assertEquals(65280, bitBoardState.getBitboard(OccupancyBitboard.ALL_PIECES));
    }

    @Test
    void initializeFromFen_defaultPosition() {
        BitBoardState bitBoardState = BitBoardState.initializeFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
//        bitBoardState.printBitBoard(bitBoardState.getBitboards()[OccupancyBitboard.BLACK_QUEEN.getIndex()]);
//        bitBoardState.printBitBoard(1152921504606846976L);
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
    }
}