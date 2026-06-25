package dev.lelek.chess.search;

import dev.lelek.chess.BoardPosition;
import dev.lelek.chess.Color;
import dev.lelek.chess.Move.Move;
import dev.lelek.chess.board.UnmakeMoveInfo;
import dev.lelek.chess.board.model.Board;
import dev.lelek.chess.eval.BoardEvaluation;

import java.util.*;

public class MoveGenerator {

    private static final Random random = new Random();

    static final int BEST = Integer.MAX_VALUE / 2;
    static final int WORST = Integer.MIN_VALUE / 2;

    private static volatile boolean timedOut = false;

    public static Move generateMove(Board board, long timeMillis) {
        timedOut = false;
        Move bestMove = null;

        new Thread(() -> {
            try {
                Thread.sleep(timeMillis);
                timedOut = true;
            } catch (InterruptedException ignored) {}
        }).start();

        for (int i = 1; ; i++) {
            BoardResults foo = negmax(board, null, i, new Stack<>());
            if (foo == null) {
                break;
            }
            bestMove = foo.move();
        }
        timedOut = false;
        return bestMove;
    }

    public static Move generateMove(Board board, int maxDepth) {
        timedOut = false;
        Move bestMove = null;

        for (int i = 1; i <= maxDepth; i++) {
            bestMove = negmax(board, null, i, new Stack<>()).move();
        }
        timedOut = false;
        return bestMove;
    }

    static BoardResults negmax(Board board, Move previousMove, int depth, Stack<UnmakeMoveInfo> unmakeMoveInfos) { // todo write more tests
        if (timedOut) {
            return null;
        }
        Color color = board.isWhiteToMove() ? Color.WHITE : Color.BLACK;

        List<Move> pseudoLegalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, board.isWhiteToMove());

        if (MoveValidator.wasPreviousMoveIllegal(board, previousMove, pseudoLegalMoves)) {
            return null;
        }

        if (board.getHalfmoveClock() == 50) { // 50 move rule
            return new BoardResults(0, null);
        }

        if (depth == 0) {
            return new BoardResults(random.nextInt(3) - 1 + BoardEvaluation.evaluate(board, color), null);
        }

        Move bestMove = null;
        int bestScore = WORST;
        boolean foundLegalMove = false;

        for (Move move : pseudoLegalMoves) {
            unmakeMoveInfos.push(UnmakeMoveInfo.from(board, move));
            board.makeMove(move);

            // boardResults will be null if move was illegal
            BoardResults boardResults = negmax(board, move, depth - 1, unmakeMoveInfos);

            if (boardResults != null && -boardResults.score() > bestScore) {
                bestScore = -boardResults.score();
                bestMove = move;
                foundLegalMove = true;
            }

            board.unmakeMove(move, unmakeMoveInfos.pop());
        }
        return getBoardResult(board, foundLegalMove, bestScore, bestMove);
    }

    private static BoardResults getBoardResult(Board board, boolean foundLegalMove, int bestScore, Move bestMove) {
        if (! foundLegalMove) {
            List<Move> pseudoLegalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, !board.isWhiteToMove());
            BoardPosition kingPosition = !board.isWhiteToMove() ? board.getBlackKingPosition() : board.getWhiteKingPosition();

            if (Utils.isPositionAttacked(pseudoLegalMoves, kingPosition)) {
                return new BoardResults(WORST, null); // checkmate
            }
            return new BoardResults(0, null); // stalemate
        }
        return new BoardResults(bestScore, bestMove);
    }
}

record BoardResults(int score, Move move) {}