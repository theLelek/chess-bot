package chess.board;

import chess.BoardPosition;
import chess.board.model.CastlingRights;

public record UnmakeMoveInfo(BoardPiece capturedPiece, CastlingRights castlingRightsWhite, CastlingRights castlingRightsBlack, BoardPosition enPassantTargetSquare, int halfMoveClock) {

}
