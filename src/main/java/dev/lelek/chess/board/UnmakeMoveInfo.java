package dev.lelek.chess.board;

import dev.lelek.chess.BoardPiece;
import dev.lelek.chess.BoardPosition;
import dev.lelek.chess.Move.Move;
import dev.lelek.chess.board.model.Board;
import dev.lelek.chess.board.model.CastlingRights;

import java.util.Objects;

public record UnmakeMoveInfo(BoardPiece capturedPiece, CastlingRights castlingRightsWhite,
                             CastlingRights castlingRightsBlack, BoardPosition enPassantTargetSquare,
                             int halfMoveClock) {

    public static UnmakeMoveInfo from(Board board, Move move) {
        var capturedPiece = board.getPieceList()[move.to().getBitBoardSquare()];
        var castlingRightsWhite = CastlingRights.copyOf(board.getCastlingRightsWhite());
        var castlingRightsBlack = CastlingRights.copyOf(board.getCastlingRightsBlack());
        var enPassantTargetSquare = board.getEnPassantTargetSquare() != null ? BoardPosition.copyOf(board.getEnPassantTargetSquare()) : null;
        var halfMoveClock = board.getHalfmoveClock();
        return new UnmakeMoveInfo(capturedPiece, castlingRightsWhite, castlingRightsBlack, enPassantTargetSquare, halfMoveClock);
    }
}
