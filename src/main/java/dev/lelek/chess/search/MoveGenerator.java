package dev.lelek.chess.search;

import dev.lelek.chess.BoardPosition;
import dev.lelek.chess.Color;
import dev.lelek.chess.Move.Move;
import dev.lelek.chess.board.UnmakeMoveInfo;
import dev.lelek.chess.board.model.Board;
import dev.lelek.chess.eval.BoardEvaluation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class MoveGenerator {

    private static final Logger log = LoggerFactory.getLogger(MoveGenerator.class);

    private static final Random random = new Random();

    static final int BEST = Integer.MAX_VALUE / 2;
    static final int WORST = Integer.MIN_VALUE / 2;


    public static Move generateMove(Board board, long timeMillis) {
        Move bestMove = negmax(board, PseudoLegalMoveFinder.getPseudoLegalMoves(board, board.isWhiteToMove()), 1, new Stack<>(), false, -1).move();;

        long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeMillis);

        log.info("search started");
        int i;
        for (i = 2; ; i++) {
            BoardResults foo = negmax(board, null, i, new Stack<>(), true, deadline);
            if (foo == null) {
                break; // timeMillis have passed
            }
            bestMove = foo.move();
        }
        log.info("search competed depth reached: {}", i);
        return bestMove;
    }

    public static Move generateMove(Board board, int maxDepth) {
        Move bestMove = null;

        for (int i = 1; i <= maxDepth; i++) {
            bestMove = negmax(board, PseudoLegalMoveFinder.getPseudoLegalMoves(board, board.isWhiteToMove()), i, new Stack<>(), false, -1).move();
        }
        return bestMove;
    }

    static BoardResults negmax(Board board, List<Move> pseudoLegalMoves, int depth, Stack<UnmakeMoveInfo> unmakeMoveInfos, boolean hasTimeLimit, long deadline) { // todo write more tests
        if (hasTimeLimit && System.nanoTime() - deadline >= 0) {
            return null;
        }

        Color color = board.isWhiteToMove() ? Color.WHITE : Color.BLACK;

        if (board.getHalfmoveClock() == 100) { // 50 move rule
            return new BoardResults(0, null);
        }
        if (depth == 0) {
            int sign = color == Color.WHITE ? 1 : -1;
            return new BoardResults(sign * (random.nextInt(3) - 1 + BoardEvaluation.evaluate(board)), null);
        }

        Move bestMove = null;
        int bestScore = WORST;
        boolean foundLegalMove = false;

        for (Move move : pseudoLegalMoves) {
            unmakeMoveInfos.push(UnmakeMoveInfo.from(board, move));
            board.makeMove(move);
            List<Move> currentPseudoLegalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, board.isWhiteToMove());

            if (LegalMoveFinder.wasPreviousMoveIllegal(board, move, currentPseudoLegalMoves)) {
                board.unmakeMove(move, unmakeMoveInfos.pop());
                continue;
            }
            foundLegalMove = true;

            BoardResults boardResults = negmax(board, PseudoLegalMoveFinder.getPseudoLegalMoves(board, board.isWhiteToMove()), depth - 1, unmakeMoveInfos, hasTimeLimit, deadline);
            if (hasTimeLimit && boardResults == null) break; // the time limit has been reached

            int score = -boardResults.score();
            if (bestMove == null || score > bestScore) {
                bestScore = score;
                bestMove = move;
            }

            board.unmakeMove(move, unmakeMoveInfos.pop());
        }
        if (hasTimeLimit && System.nanoTime() - deadline >= 0) {
            return null;
        }
        return getBoardResult(board, foundLegalMove, bestScore, bestMove);
    }

    private static BoardResults getBoardResult(Board board, boolean foundLegalMove, int bestScore, Move bestMove) {
        if (! foundLegalMove) {
            List<Move> pseudoLegalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, ! board.isWhiteToMove());
            BoardPosition kingPosition = ! board.isWhiteToMove() ? board.getBlackKingPosition() : board.getWhiteKingPosition();

            if (Utils.isPositionAttacked(pseudoLegalMoves, kingPosition)) {
                return new BoardResults(WORST, null); // checkmate
            }
            return new BoardResults(0, null); // stalemate
        }
        return new BoardResults(bestScore, bestMove);
    }
}

record BoardResults(int score, Move move) {}