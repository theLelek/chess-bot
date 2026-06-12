package dev.lelek.chess.move_generation;

import dev.lelek.chess.BoardPosition;
import dev.lelek.chess.Color;
import dev.lelek.chess.Move.CastlingMove;
import dev.lelek.chess.Move.Move;
import dev.lelek.chess.board.UnmakeMoveInfo;
import dev.lelek.chess.board.model.Board;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class MoveGenerator {

    private static final Random random = new Random();
    static Move bestMove = null;

    public static Move generateMove(Board board, int seconds) {
        LocalTime time = LocalTime.now();
        for (int i = 1; ChronoUnit.MILLIS.between(time, LocalTime.now()) < seconds; i++) { // iterative deepening
            findBestMove(board, new Stack<>(), null, i, i);
        }
        return bestMove;
    }


    private static int findBestMove(Board board, Stack<UnmakeMoveInfo> unmakeMoveInfos, Move previousMove, int depth, final int startingDepth) {
        boolean isWhiteToMove = board.isWhiteToMove();
        Color color = (isWhiteToMove) ? Color.WHITE : Color.BLACK;
        List<Move> pseudoLegalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, isWhiteToMove);
        BoardPosition kingPosition = isWhiteToMove ? board.getBlackKingPosition() : board.getWhiteKingPosition();

        if (previousMove instanceof CastlingMove castlingMove) {
            BoardPosition positionToCheck = new BoardPosition(castlingMove.isKingSideCastling() ? 5 : 3, castlingMove.from().y());
            if (isPositionTargeted(pseudoLegalMoves, positionToCheck, kingPosition, previousMove.from())) {
                return Integer.MIN_VALUE / 2;
            }
        } else if (isPositionTargeted(pseudoLegalMoves, kingPosition)) {
            return Integer.MIN_VALUE / 2;
        }

        if (depth == 0) return BoardEvaluation.evaluate(board, color);

        int best = 0;
        for (Move move : pseudoLegalMoves) {
            unmakeMoveInfos.push(new UnmakeMoveInfo(board, move));
            board.makeMove(move);
            int foo = findBestMove(board, unmakeMoveInfos, move, depth - 1, startingDepth);
            foo += random.nextInt(21) - 10;
            if (foo >= best) {
                best = foo;
                if (depth == startingDepth) {
                    bestMove = move;
                }
            }
            board.unmakeMove(move, unmakeMoveInfos.pop());
        }
        return -best;
    }

    private static boolean isPositionTargeted(List<Move> moves, BoardPosition... positions) {
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