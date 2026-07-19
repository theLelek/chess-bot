package dev.lelek.chess.board.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.lelek.chess.Move.Move;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

class ZobristTest {

    @Test
    void fromBoard_WhenEnPassantCaptureIsAvailable() {
        Board board1 = Board.fromFen("rnbqkbnr/ppp1pppp/8/8/3pP3/8/PPPP1PPP/RNBQKBNR b KQkq - 0 1");
        Board board2 = Board.fromFen("rnbqkbnr/ppp1pppp/8/8/3pP3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1");
        Assertions.assertNotEquals(Zobrist.fromBoard(board1), Zobrist.fromBoard(board2));

        Board board3 = Board.fromFen("rnbqkbnr/ppppp1pp/8/8/4Pp2/8/PPPP1PPP/RNBQKBNR b KQkq - 0 1");
        Board board4 = Board.fromFen("rnbqkbnr/ppppp1pp/8/8/4Pp2/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1");
        Assertions.assertNotEquals(Zobrist.fromBoard(board3), Zobrist.fromBoard(board4));

    }

    @Test
    void fromBoard_WhenEnPassantCaptureIsNotAvailable() {
        Board board1 = Board.fromFen("r1bqkbnr/pppppppp/2n5/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq - 0 1");
        Board board2 = Board.fromFen("r1bqkbnr/pppppppp/2n5/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq - 0 1");
        Assertions.assertEquals(Zobrist.fromBoard(board1), Zobrist.fromBoard(board2));
    }

    @Test
    void fromBoard_defaultBoard() {
        Set<Long> hashes = new HashSet<>();
        Board board = Board.initializeDefaultBoard();
        long hash1 = Zobrist.fromBoard(board);
        hashes.add(hash1);

        board.makeMove(new Move("e2", "e4"));
        long hash2 = Zobrist.fromBoard(board);
        hashes.add(hash2);

        board.makeMove(new Move("e7", "e5"));
        long hash3 = Zobrist.fromBoard(board);
        hashes.add(hash3);

        Assertions.assertEquals(3, hashes.size());
    }
}