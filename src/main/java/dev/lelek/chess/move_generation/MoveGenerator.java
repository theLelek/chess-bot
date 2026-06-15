package dev.lelek.chess.move_generation;

import dev.lelek.chess.BoardPosition;
import dev.lelek.chess.Color;
import dev.lelek.chess.Move.CastlingMove;
import dev.lelek.chess.Move.Move;
import dev.lelek.chess.board.UnmakeMoveInfo;
import dev.lelek.chess.board.model.Board;

import java.util.*;

public class MoveGenerator {

    private static final Random random = new Random();

    private static final int BEST = Integer.MAX_VALUE / 2;
    private static final int WORST = Integer.MIN_VALUE / 2;

    public static Move generateMove(Board board, long timeMillis) {
        long endTime = System.nanoTime() + timeMillis * 1_000_000L;
        Move bestMove = null;

        for (int i = 1; System.nanoTime() < endTime; i++) {
            bestMove = negmax(board, null, i, new Stack<>()).move();
        }
        return bestMove;
    }

    public static Move generateMove(Board board, int maxDepth) {
        Move bestMove = null;

        for (int i = 1; i <= maxDepth; i++) {
            bestMove = negmax(board, null, i, new Stack<>()).move();
        }
        // if result.move() = null and result.score = WORST -> checkmate
        // if result.move() = null and result.score = 0 -> stalemate
        return bestMove;
    }

    private static GameResult negmax(Board board, Move previousMove, int depth, Stack<UnmakeMoveInfo> unmakeMoveInfos) {
        Color color = board.isWhiteToMove() ? Color.WHITE : Color.BLACK;

        List<Move> pseudoLegalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, board.isWhiteToMove());
        BoardPosition kingPosition = board.isWhiteToMove() ? board.getBlackKingPosition() : board.getWhiteKingPosition();

        if (wasPreviousMoveIllegal(previousMove, pseudoLegalMoves, kingPosition)) {
            return null;
        }

        if (board.getHalfmoveClock() == 50) { // 50 move rule
            return new GameResult(0, null);
        }

        if (depth == 0) {
            return new GameResult(BoardEvaluation.evaluate(board, color), null);
        }

        Move bestMove = null;
        int bestScore = WORST;
        boolean foundLegalMove = false;

        for (Move move : pseudoLegalMoves) {
            unmakeMoveInfos.push(new UnmakeMoveInfo(board, move));
            board.makeMove(move);

            // gameResult will be null if move was illegal
            GameResult gameResult = negmax(board, move, depth - 1, unmakeMoveInfos);

            if (gameResult != null && -gameResult.score() > bestScore) {
                bestScore = -gameResult.score();
                bestMove = move;
                foundLegalMove = true;
            }

            board.unmakeMove(move, unmakeMoveInfos.pop());
        }
        return getGameResult(board, foundLegalMove, bestScore, bestMove);
    }

    private static GameResult getGameResult(Board board, boolean foundLegalMove, int bestScore, Move bestMove) {
        if (! foundLegalMove) {
            List<Move> pseudoLegalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, !board.isWhiteToMove());
            BoardPosition kingPosition = !board.isWhiteToMove() ? board.getBlackKingPosition() : board.getWhiteKingPosition();

            if (isPositionAttacked(pseudoLegalMoves, kingPosition)) {
                return new GameResult(WORST, null); // checkmate
            }
            return new GameResult(0, null); // stalemate
        }
        return new GameResult(bestScore, bestMove);
    }

    private static boolean wasPreviousMoveIllegal(Move previousMove, List<Move> pseudoLegalMoves, BoardPosition positionToCheck1) {
        if (previousMove instanceof CastlingMove castlingMove) {
            BoardPosition positionToCheck2 = new BoardPosition(castlingMove.isKingSideCastling() ? 5 : 3, castlingMove.from().y());
            BoardPosition positionToCheck3 = previousMove.from();
            return isPositionAttacked(pseudoLegalMoves, positionToCheck1, positionToCheck2, positionToCheck3);
        } else {
            return isPositionAttacked(pseudoLegalMoves, positionToCheck1);
        }
    }

    private static boolean isPositionAttacked(List<Move> moves, BoardPosition... positions) {
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

record GameResult(int score, Move move) {}