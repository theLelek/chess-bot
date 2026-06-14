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
        return bestMove;
    }

    private static Result negmax(Board board, Move previousMove, int depth, Stack<UnmakeMoveInfo> unmakeMoveInfos) {
        Color color = board.isWhiteToMove() ? Color.WHITE : Color.BLACK;

        List<Move> pseudoLegalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, board.isWhiteToMove());
        BoardPosition kingPosition = board.isWhiteToMove() ? board.getBlackKingPosition() : board.getWhiteKingPosition();

        if (wasPreviousMoveILlegal(previousMove, pseudoLegalMoves, kingPosition)) {
            return null;
        }

//        if (isPositionAttacked(pseudoLegalMoves, kingPosition)) {
////            return new Result(BEST, null); // would lead to king capture
//            return null;
//        }

        if (depth == 0) {
            return new Result(BoardEvaluation.evaluate(board, color), null);
        }

        Move bestMove = null;
        int bestScore = WORST;
        boolean foundLegalMove = false;

        for (Move move : pseudoLegalMoves) {
            unmakeMoveInfos.push(new UnmakeMoveInfo(board, move));
            board.makeMove(move);

            // result will be null if move was illegal
            Result result = negmax(board, move, depth - 1, unmakeMoveInfos);

            if (result != null && -result.score() > bestScore) {
                bestScore = -result.score();
                bestMove = move;
                foundLegalMove = true;
            }

            board.unmakeMove(move, unmakeMoveInfos.pop());
        }

        if (! foundLegalMove) {
            List<Move> pseudoLegalMoves2 = PseudoLegalMoveFinder.getPseudoLegalMoves(board, ! board.isWhiteToMove());
            BoardPosition kingPosition2 = ! board.isWhiteToMove() ? board.getBlackKingPosition() : board.getWhiteKingPosition();

            if (isPositionAttacked(pseudoLegalMoves2, List.of(kingPosition2))) {
                return new Result(WORST, null); // checkmate
            }
            return new Result(0, null); // stalemate
        }
        return new Result(bestScore, bestMove);
    }

    private static boolean wasPreviousMoveILlegal(Move previousMove, List<Move> pseudoLegalMoves, BoardPosition kingPosition) {
        List<BoardPosition> positionsToCheck = new ArrayList<>();
        positionsToCheck.add(kingPosition);

        if (previousMove instanceof CastlingMove castlingMove) {
            BoardPosition positionToCheck = new BoardPosition(castlingMove.isKingSideCastling() ? 5 : 3, castlingMove.from().y());
            positionsToCheck.add(positionToCheck);
            positionsToCheck.add(previousMove.from());
        }
        return isPositionAttacked(pseudoLegalMoves, positionsToCheck);
    }

    private static boolean isPositionAttacked(List<Move> moves, List<BoardPosition> positions) {
        for (Move move : moves) {
            for (BoardPosition position : positions) {
                if (move.to().equals(position)) {
                    return true;
                }
            }
        }
        return false;
    }


    private static boolean isPositionAttacked(List<Move> moves, BoardPosition position) {
        for (Move move : moves) {
            if (move.to().equals(position)) {
                return true;
            }
        }
        return false;
    }
}

record Result(int score, Move move) {}