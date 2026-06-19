package dev.lelek.chess.board;

import dev.lelek.chess.BoardPosition;
import dev.lelek.chess.Move.CastlingMove;
import dev.lelek.chess.Move.Move;
import dev.lelek.chess.board.model.Board;
import dev.lelek.chess.search.PseudoLegalMoveFinder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class PerftTest {
    // all perft results can be seen on https://www.chessprogramming.org/Perft_Results
    @Test
    void perftPosition1() {
        // currently takes 3.8-4.1 seconds for depth 5 on the default position
        Board board = Board.initializeDefaultBoard();
        Assertions.assertEquals(4_865_609,  perft(5, board, new Stack<>(), null));
    }

    @Test
    void perftPosition2() {
        /*
        the fen of position 2 uses a short version which is currently not supported,
        thus the - at the end had to be manually changed to 0 1
         */
        Board board = Board.initializeFromFen("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
        Assertions.assertEquals(97_862, perft(3, board, new Stack<>(), null));
    }

    @Test
    void perftPosition3() {
        Board board = Board.initializeFromFen("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1 ");
        Assertions.assertEquals(43238, perft(4, board, new Stack<>(), null));
    }

    @Test
    void perftPosition4() {
        Board board = Board.initializeFromFen("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1");
        Assertions.assertEquals(9467, perft(3, board, new Stack<>(), null));
    }

    @Test
    void perftPosition5() {
        Board board = Board.initializeFromFen("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8");
        Assertions.assertEquals(2103487, perft(4, board, new Stack<>(), null));
    }

    @Test
    void perftPosition6() {
        Board board = Board.initializeFromFen("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10 ");
        Assertions.assertEquals(3894594, perft(4, board, new Stack<>(), null));
    }

    private static int perft(int depth, Board board, Stack<UnmakeMoveInfo> unmakeMoveInfos, Move previousMove) {
        boolean isWhiteToMove = board.isWhiteToMove();
        List<Move> pseudoLegalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, isWhiteToMove);
        BoardPosition kingPosition = isWhiteToMove ? board.getBlackKingPosition() : board.getWhiteKingPosition();

        if (isInCheck(previousMove, pseudoLegalMoves, kingPosition)) {
            return 0;
        }

        if (depth == 0) return 1;

        int count = 0;
        for (Move move : pseudoLegalMoves) {
            unmakeMoveInfos.push(new UnmakeMoveInfo(board, move));
            board.makeMove(move);
            int foo = perft(depth - 1, board, unmakeMoveInfos, move);
            count += foo;
            board.unmakeMove(move, unmakeMoveInfos.pop());
        }
        return count;
    }

    private static boolean isInCheck(Move previousMove, List<Move> pseudoLegalMoves, BoardPosition kingPosition) {
        List<BoardPosition> positionsToCheck = new ArrayList<>();
        positionsToCheck.add(kingPosition);

        if (previousMove instanceof CastlingMove castlingMove) {
            BoardPosition positionToCheck = new BoardPosition(castlingMove.isKingSideCastling() ? 5 : 3, castlingMove.from().y());
            positionsToCheck.add(positionToCheck);
            positionsToCheck.add(previousMove.from());
        }
        return isPositionTargeted(pseudoLegalMoves, positionsToCheck);
    }

    private static boolean isPositionTargeted(List<Move> moves, List<BoardPosition> positions) {
        for (Move move : moves) {
            for (BoardPosition position : positions) {
                if (move.to().equals(position)) {
                    return true;
                }
            }
        }
        return false;
    }
}