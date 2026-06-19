package dev.lelek.chess.board;

import dev.lelek.chess.BoardPiece;
import dev.lelek.chess.BoardPosition;
import dev.lelek.chess.Move.Move;
import dev.lelek.chess.board.model.Board;
import dev.lelek.chess.board.model.CastlingRights;

import java.util.Objects;

public final class UnmakeMoveInfo {

    private final BoardPiece capturedPiece;
    private final CastlingRights castlingRightsWhite;
    private final CastlingRights castlingRightsBlack;
    private final BoardPosition enPassantTargetSquare;
    private final int halfMoveClock;

    public UnmakeMoveInfo(BoardPiece capturedPiece, CastlingRights castlingRightsWhite, CastlingRights castlingRightsBlack, BoardPosition enPassantTargetSquare, int halfMoveClock) {
        this.capturedPiece = capturedPiece;
        this.castlingRightsWhite = castlingRightsWhite;
        this.castlingRightsBlack = castlingRightsBlack;
        this.enPassantTargetSquare = enPassantTargetSquare;
        this.halfMoveClock = halfMoveClock;
    }

    public UnmakeMoveInfo(Board board, Move move) { // todo maybe change to static factory method so class can be replaced with record
        capturedPiece = board.getPieceList()[move.to().getBitBoardSquare()];
        castlingRightsWhite = CastlingRights.copyOf(board.getCastlingRightsWhite());
        castlingRightsBlack = CastlingRights.copyOf(board.getCastlingRightsBlack());
        enPassantTargetSquare = board.getEnPassantTargetSquare() != null ? BoardPosition.copyOf(board.getEnPassantTargetSquare()) : null; // todo copying can probably be avoided
        halfMoveClock = board.getHalfmoveClock();
    }

    public BoardPiece capturedPiece() {
        return capturedPiece;
    }

    public CastlingRights castlingRightsWhite() {
        return castlingRightsWhite;
    }

    public CastlingRights castlingRightsBlack() {
        return castlingRightsBlack;
    }

    public BoardPosition enPassantTargetSquare() {
        return enPassantTargetSquare;
    }

    public int halfMoveClock() {
        return halfMoveClock;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (UnmakeMoveInfo) obj;
        return Objects.equals(this.capturedPiece, that.capturedPiece) &&
                Objects.equals(this.castlingRightsWhite, that.castlingRightsWhite) &&
                Objects.equals(this.castlingRightsBlack, that.castlingRightsBlack) &&
                Objects.equals(this.enPassantTargetSquare, that.enPassantTargetSquare) &&
                this.halfMoveClock == that.halfMoveClock;
    }

    @Override
    public int hashCode() {
        return Objects.hash(capturedPiece, castlingRightsWhite, castlingRightsBlack, enPassantTargetSquare, halfMoveClock);
    }

    @Override
    public String toString() {
        return "UnmakeMoveInfo[" +
                "capturedPiece=" + capturedPiece + ", " +
                "castlingRightsWhite=" + castlingRightsWhite + ", " +
                "castlingRightsBlack=" + castlingRightsBlack + ", " +
                "enPassantTargetSquare=" + enPassantTargetSquare + ", " +
                "halfMoveClock=" + halfMoveClock + ']';
    }
}
