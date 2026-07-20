package dev.lelek.chess.search;

import dev.lelek.chess.BoardPosition;
import dev.lelek.chess.Color;
import dev.lelek.chess.board.model.Fen;
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
        nodeCount = 0;
        Move bestMove = negmax(board, PseudoLegalMoveFinder.getPseudoLegalMoves(board, board.isWhiteToMove()), 1, new Stack<>(), false, -1, ALPHA_START, BETA_START).move();

        long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeMillis);

        log.info("search started");
        int i;

        List<Move> pseudoLegalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, board.isWhiteToMove());
        MoveOrdering.order(board, pseudoLegalMoves);
        nodeCount = 0;
        for (i = 2; ; i++) {
            BoardResults boardResults = negmax(board, pseudoLegalMoves, i, new Stack<>(), true, deadline, ALPHA_START, BETA_START);
            if (boardResults == null) {
                break; // timeMillis have passed
            }
            bestMove = boardResults.move();
            log.info("depth {} complete, best move: {}, node count: {}", i, bestMove, nodeCount);
            nodeCount = 0;
        }
        log.info("search completed, depth reached: {}, node count: {}", i - 1, nodeCount);
        nodeCount = 0;
        return bestMove;
    }

    public static Move generateMove(Board board, int maxDepth) {
        nodeCount = 0;
        Move bestMove = null;

        List<Move> pseudoLegalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, board.isWhiteToMove());
        MoveOrdering.order(board, pseudoLegalMoves);
        int i;
        nodeCount = 0;
        for (i = 1; i <= maxDepth; i++) {
            BoardResults boardResults = negmax(board, pseudoLegalMoves, i, new Stack<>(), false, -1, ALPHA_START, BETA_START);
            bestMove = boardResults.move();
            log.info("depth {} complete, best move: {}, node count: {}", i, bestMove, nodeCount);
            nodeCount = 0;
        }
        log.info("search completed, depth reached: {}, node count: {}", i - 1, nodeCount);
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

        TranspositionTable tt = TranspositionTable.getInstance();
        TranspositionTableEntry entry = tt.getEntry(board.getZobristHash());
        if (entry != null && entry.zobristHash() == board.getZobristHash() && entry.searchedDepth() >= depth) {
            nodeCount++;
            return new BoardResults(entry.score(), entry.move(), false, false);
        }
        if (entry != null && entry.zobristHash() != board.getZobristHash()) {
            log.error("hash collision in tt, fen: {}", Fen.toFen(board));
        }
        if (depth == 0) { // todo could stop at illegal position
            int sign = color == Color.WHITE ? 1 : -1;
            nodeCount++;
            return new BoardResults(sign * (random.nextInt(3) - 1 + BoardEvaluation.evaluate(board)), null, false, false);
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