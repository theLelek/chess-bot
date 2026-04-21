package chess.board;

import chess.Move;
import chess.board.model.Board;
import chess.BoardPosition;
import chess.board.model.BoardPiece;
import chess.board.model.CastlingRights;

import java.util.ArrayList;
import java.util.List;

public class PseudoLegalMoveFinder {
    public static List<Move> getPseudoLegalMoves(Board board, boolean isWhiteToMove) {
        List<Move> legalMoves = new ArrayList<>();
        for (BoardPosition boardPosition : board.getPiecesIndexes()) {
            BoardPiece currentPiece = board.getBoardPieces()[boardPosition.y()][boardPosition.x()];
            if (currentPiece.isWhite() != isWhiteToMove) {
                continue;
            }
            // TODO pawn moves on last row will be removed and put into pseudolegalPromotions

            // pawn
            if (currentPiece == BoardPiece.BLACK_PAWN || currentPiece == BoardPiece.WHITE_PAWN) {
                getPseudoLegalPawnMoves(board, boardPosition, legalMoves);
            // general
            } else {
                getPseudoLegalMoves(board, boardPosition, legalMoves);
            }
        }
        return legalMoves;
    }


    private static void getPseudoLegalMoves(Board board, BoardPosition position, List<Move> legalMoves) {
        BoardPiece currentPiece = board.getBoardPieces()[position.y()][position.x()];
        PieceMoveRules currentPieceMoveRules = currentPiece.getMoveRules();

        for (int[] direction : currentPieceMoveRules.getDirections()) {
            BoardPosition currentPosition = position.copy();
            boolean interrupted = false;
            do {
                try {
                    currentPosition = currentPosition.move(direction);
                } catch (IndexOutOfBoundsException e){
                    interrupted = true;
                    continue;
                }
                if(board.getBoardPiece(currentPosition) != null){
                    interrupted = true;
                    if(board.getBoardPiece(currentPosition).hasSameColor(currentPiece)){
                        continue;
                    }
                }
                legalMoves.add(new Move(position, currentPosition));
            } while (currentPieceMoveRules.canMoveInfinitely() && !interrupted);
        }
    }

    private static void getPseudoLegalPawnMoves(Board board, BoardPosition position, List<Move> legalMoves) {
        BoardPiece currentPiece = board.getBoardPiece(position);
        int direction = (currentPiece.isWhite()) ? -1 : 1;
        int startingY = (currentPiece.isWhite()) ? 6 : 1;
        int forwardY = position.y() + direction;
        boolean isPromoting = currentPiece.isWhite() && forwardY == 0 || currentPiece.isBlack() && forwardY == Board.SIZE - 1;

        if (forwardY < 0 || forwardY >= Board.SIZE) {
            return;
        }
        // forward:
        if (board.getBoardPieces()[forwardY][position.x()] == null) {
            legalMoves.add(new Move(position, new BoardPosition(position.x(), forwardY)));
        }

        // forward 2:
        if (position.y() == startingY
                && board.getBoardPieces()[forwardY][position.x()] == null
                && board.getBoardPieces()[forwardY + direction][position.x()] == null) {
            legalMoves.add(new Move(position, new BoardPosition(position.x(), forwardY + direction)));
        }

        // diagonal:
        if (position.x() - 1 >= 0) {
            BoardPosition diagonalPiecePosition = new BoardPosition(position.x() - 1, forwardY);
            BoardPiece diagonalPiece = board.getBoardPiece(diagonalPiecePosition);
            if (diagonalPiece != null && diagonalPiece.isWhite() != currentPiece.isWhite()) {
                legalMoves.add(new Move(position, diagonalPiecePosition));
            }
        }

        if (position.x() + 1 < Board.SIZE) {
            BoardPosition diagonalPiecePosition = new BoardPosition(position.x() + 1, forwardY);
            BoardPiece diagonalPiece = board.getBoardPiece(diagonalPiecePosition);
            if (diagonalPiece != null && diagonalPiece.isWhite() != currentPiece.isWhite()) {
                legalMoves.add(new Move(position, diagonalPiecePosition));
            }
        }
    }

    public static CastlingRights getPseudoLegalCastlingRights(Board board, boolean isWhiteToMove) {
        CastlingRights rights = isWhiteToMove ? board.getCastlingRightsWhite() : board.getCastlingRightsBlack();
        String rank = isWhiteToMove ? "1" : "8";

        boolean canCastleKingSide = rights.canCastleKingSide()
                && board.getBoardPiece(new BoardPosition("f" + rank)) == null
                && board.getBoardPiece(new BoardPosition("g" + rank)) == null;

        boolean canCastleQueenSide = rights.canCastleQueenSide()
                && board.getBoardPiece(new BoardPosition("d" + rank)) == null
                && board.getBoardPiece(new BoardPosition("c" + rank)) == null
                && board.getBoardPiece(new BoardPosition("b" + rank)) == null;

        return new CastlingRights(canCastleKingSide, canCastleQueenSide);
    }
}