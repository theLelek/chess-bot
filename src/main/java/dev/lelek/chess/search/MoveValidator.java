package dev.lelek.chess.search;

import dev.lelek.chess.BoardPosition;
import dev.lelek.chess.Move.CastlingMove;
import dev.lelek.chess.Move.Move;
import dev.lelek.chess.board.UnmakeMoveInfo;
import dev.lelek.chess.board.model.Board;

import java.util.List;

public class MoveValidator {
    public static boolean isMoveLegal(Board board, Move move) {
        List<Move> pseudoLegalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, board.isWhiteToMove());
        if (! pseudoLegalMoves.contains(move)) return false;

        UnmakeMoveInfo unmakeMoveInfo = UnmakeMoveInfo.from(board, move);
        board.makeMove(move);
        List<Move> pseudoLegalMoves2 = PseudoLegalMoveFinder.getPseudoLegalMoves(board, board.isWhiteToMove());
        boolean result = ! wasPreviousMoveIllegal(board, move, pseudoLegalMoves2);
        board.unmakeMove(move, unmakeMoveInfo);
        return result;
    }

    static boolean wasPreviousMoveIllegal(Board board, Move previousMove, List<Move> pseudoLegalMoves) {
        BoardPosition kingPosition = board.isWhiteToMove() ? board.getBlackKingPosition() : board.getWhiteKingPosition();
        if (previousMove instanceof CastlingMove castlingMove) {
            BoardPosition positionToCheck2 = new BoardPosition(castlingMove.isKingSideCastling() ? 5 : 3, castlingMove.from().y());
            BoardPosition positionToCheck3 = previousMove.from();
            return Utils.isPositionAttacked(pseudoLegalMoves, kingPosition, positionToCheck2, positionToCheck3);
        } else {
            return Utils.isPositionAttacked(pseudoLegalMoves, kingPosition);
        }
    }
}
