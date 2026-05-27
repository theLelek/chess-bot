package chess.board.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    void initializeFromFen_onlyWhitePawns() {
        String fen = "8/8/8/8/8/8/PPPPPPPP/8";
        Position position = Position.initializeFromFen(fen);
        assertEquals(65280, position.getBitboard(BoardPiece.WHITE_PAWN));
        assertEquals(65280, position.getBitboard(PieceBitboard.WHITE_PIECES));
        assertEquals(65280, position.getBitboard(PieceBitboard.ALL_PIECES));
    }

    @Test
    void initializeFromFen_defaultPosition() {
        Position position = Position.initializeFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
//        position.printBitBoard(position.getBitboards()[PieceBitboard.BLACK_QUEEN.getIndex()]);
//        position.printBitBoard(1152921504606846976L);
        assertEquals(65280, position.getBitboard(BoardPiece.WHITE_PAWN));
        assertEquals(129, position.getBitboard(BoardPiece.WHITE_ROOK));
        assertEquals(66, position.getBitboard(BoardPiece.WHITE_KNIGHT));
        assertEquals(36, position.getBitboard(BoardPiece.WHITE_BISHOP));
        assertEquals(8, position.getBitboard(BoardPiece.WHITE_QUEEN));
        assertEquals(16, position.getBitboard(BoardPiece.WHITE_KING));

        assertEquals(71776119061217280L, position.getBitboard(BoardPiece.BLACK_PAWN));
        assertEquals(-9151314442816847872L, position.getBitboard(BoardPiece.BLACK_ROOK));
        assertEquals(4755801206503243776L, position.getBitboard(BoardPiece.BLACK_KNIGHT));
        assertEquals(2594073385365405696L, position.getBitboard(BoardPiece.BLACK_BISHOP));
        assertEquals(576460752303423488L, position.getBitboard(BoardPiece.BLACK_QUEEN));
        assertEquals(1152921504606846976L, position.getBitboard(BoardPiece.BLACK_KING));
    }
}