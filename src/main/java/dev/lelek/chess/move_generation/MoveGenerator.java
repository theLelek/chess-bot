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

        BoardPosition kingPosition = board.isWhiteToMove()
                ? board.getBlackKingPosition()
                : board.getWhiteKingPosition();

        if (isInCheck(previousMove, pseudoLegalMoves, kingPosition)) {
            return null;
        }

        if (depth == 0) {
            return new Result(BoardEvaluation.evaluate(board, color), null);
        }

        Move bestMove = null;
        int bestScore = Integer.MIN_VALUE;
        boolean foundLegalMove = false;

        for (Move move : pseudoLegalMoves) {
            unmakeMoveInfos.push(new UnmakeMoveInfo(board, move));
            board.makeMove(move);

            Result result = negmax(board, move, depth - 1, unmakeMoveInfos);

            if (result != null && -result.score() >= bestScore) {
                bestScore = -result.score();
                bestMove = move;
                foundLegalMove = true;
            }

            board.unmakeMove(move, unmakeMoveInfos.pop());
        }

        if (!foundLegalMove) {
            if (isPositionAttacked(pseudoLegalMoves, List.of(kingPosition))) {
                return new Result(Integer.MIN_VALUE, null);
            }
            return new Result(0, null);
        }

        return new Result(bestScore, bestMove);
    }

    private static boolean isInCheck(Move previousMove, List<Move> pseudoLegalMoves, BoardPosition kingPosition) {
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
}

record Result(int score, Move move) {}