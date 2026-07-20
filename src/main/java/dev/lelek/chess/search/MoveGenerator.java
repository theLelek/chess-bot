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

    static final int ALPHA_START = Integer.MIN_VALUE / 2;
    static final int BETA_START = Integer.MAX_VALUE / 2;
    static final int BEST = BETA_START / 2;
    static final int WORST = ALPHA_START / 2;

    private static final int FIFTY_MOVE_RULE_HALFMOVES = 100;

    private static long nodeCount = 0;

    public static Move generateMove(Board board, long timeMillis) {
        return generateMove(board, -1, System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeMillis), true);
    }

    public static Move generateMove(Board board, int maxDepth) {
        return generateMove(board, maxDepth, -1, false);
    }

    private static Move generateMove(Board board, int maxDepth, long deadline, boolean useTimeLimit) {
        nodeCount = 0;

        List<Move> moves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, board.isWhiteToMove());
        MoveOrdering.order(board, moves);

        Move bestMove = useTimeLimit ? negmax(board, moves, 1, new Stack<>(), false, -1, ALPHA_START, BETA_START).move() : null;
        int startDepth = useTimeLimit ? 2 : 1;

        log.info("search started");
        int i;
        for (i = startDepth; useTimeLimit || i <= maxDepth; i++) {
            BoardResults result = negmax(board, moves, i, new Stack<>(), useTimeLimit, deadline, ALPHA_START, BETA_START);
            if (result == null) break;

            bestMove = result.move();
            log.info("depth {} complete, best move: {}, node count: {}", i, bestMove, nodeCount);
            nodeCount = 0;
        }

        log.info("search completed, depth reached: {}, partially searched nodes: {}", i - 1, nodeCount);
        nodeCount = 0;
        return bestMove;
    }

    static BoardResults negmax(Board board, List<Move> pseudoLegalMoves, int depth, Stack<UnmakeMoveInfo> unmakeMoveInfos, boolean hasTimeLimit, long deadline, int alpha, int beta) { // todo write more tests
        if (hasTimeLimit && System.nanoTime() - deadline >= 0) {
            return null;
        }

        Color color = board.isWhiteToMove() ? Color.WHITE : Color.BLACK;

        if (board.getHalfmoveClock() == FIFTY_MOVE_RULE_HALFMOVES) {
            nodeCount++;
            return new BoardResults(0, null, false, true);
        }
        if (depth == 0) { // todo could stop at illegal position
            int sign = color == Color.WHITE ? 1 : -1;
            nodeCount++;
            return new BoardResults(sign * (random.nextInt(3) - 1 + BoardEvaluation.evaluate(board)), null, false, false);
        }

        TranspositionTable tt = TranspositionTable.getInstance();
        TranspositionTableEntry entry = tt.getEntry(board.getZobristHash());
        if (entry != null && entry.zobristHash() == board.getZobristHash() && entry.searchedDepth() >= depth) {
            return new BoardResults(entry.score(), entry.move(), false, false);
        }

        Move bestMove = null;
        int bestScore = WORST;
        boolean foundLegalMove = false;

        for (Move move : pseudoLegalMoves) {
            unmakeMoveInfos.push(UnmakeMoveInfo.from(board, move));
            board.makeMove(move);
            List<Move> currentPseudoLegalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, board.isWhiteToMove());
            MoveOrdering.order(board, currentPseudoLegalMoves);

            if (LegalMoveFinder.wasPreviousMoveIllegal(board, move, currentPseudoLegalMoves)) {
                board.unmakeMove(move, unmakeMoveInfos.pop());
                continue;
            }
            foundLegalMove = true;

            BoardResults boardResults = negmax(board, currentPseudoLegalMoves, depth - 1, unmakeMoveInfos, hasTimeLimit, deadline, -beta, -alpha);
            if (hasTimeLimit && boardResults == null) {
                board.unmakeMove(move, unmakeMoveInfos.pop());
                return null; // the time limit has been reached
            }

            int score = -boardResults.score();
            if (bestMove == null || score > bestScore) {
                bestScore = score;
                bestMove = move;
            }

            if (score >= beta) {
                board.unmakeMove(move, unmakeMoveInfos.pop());
                return new BoardResults(bestScore, bestMove, false, false);
            }
            alpha = Math.max(alpha, score);

            board.unmakeMove(move, unmakeMoveInfos.pop());
        }
        if (hasTimeLimit && System.nanoTime() - deadline >= 0) {
            return null;
        }
        if (! foundLegalMove) {
            nodeCount++;
            return getBoardResult(board, depth);
        }
        BoardResults boardResults = new BoardResults(bestScore, bestMove, false, false);

        tt.setEntry(board.getZobristHash(), new TranspositionTableEntry(board.getZobristHash(), depth, bestMove, bestScore));
        return boardResults;
    }

    private static BoardResults getBoardResult(Board board, int depth) {
        List<Move> opponentPseudoLegalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, ! board.isWhiteToMove());
        BoardPosition kingPosition = ! board.isWhiteToMove() ? board.getBlackKingPosition() : board.getWhiteKingPosition();

        if (Utils.isPositionAttacked(opponentPseudoLegalMoves, kingPosition)) { // checks if king is in check
            return new BoardResults(WORST - depth, null, true, false); // checkmate
        }
        return new BoardResults(0, null, false, true); // stalemate
    }
}