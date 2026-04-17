package chess.board;

import chess.LegalMoves;
import chess.Move;
import chess.board.model.Board;
import chess.BoardPosition;
import chess.board.model.BoardPiece;

import java.util.ArrayList;
import java.util.List;

public class PseudoLegalMoveFinder {
    public static LegalMoves getPseudoLegalMoves(Board board, boolean isWhiteToMove) {
        LegalMoves legalMoves = new LegalMoves();
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

    private static void getPseudoLegalMoves(Board board, BoardPosition position, LegalMoves legalMoves) {
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
                legalMoves.getLegalMoves().add(new Move(position, currentPosition));
            } while (currentPieceMoveRules.canMoveInfinitely() && !interrupted);
        }
    }

    private static void getPseudoLegalPawnMoves(Board board, BoardPosition position, LegalMoves legalMoves) {
        BoardPiece currentPiece = board.getBoardPiece(position);
        int direction = (currentPiece.isWhite()) ? -1 : +1;
        int startingY = (currentPiece.isWhite()) ? 6 : 1;
        int forwardY = position.y() + direction;

        if (forwardY < 0 || forwardY >= Board.SIZE) {
            return;
        }

        // forward:
        if (board.getBoardPieces()[forwardY][position.x()] == null) {
            legalMoves.getLegalMoves().add(new Move(position.copy(), new BoardPosition(position.x(), forwardY)));
        }

        // forward 2:
        if (position.y() == startingY
                && board.getBoardPieces()[forwardY][position.x()] == null
                && board.getBoardPieces()[forwardY + direction][position.x()] == null) {
            legalMoves.getLegalMoves().add(new Move(position.copy(), new BoardPosition(position.x(), forwardY + direction)));
        }

        // diagonal:
        if (position.x() - 1 >= 0) {
            BoardPosition diagonalPiecePosition = new BoardPosition(position.x() - 1, forwardY);
            BoardPiece diagonalPiece = board.getBoardPiece(diagonalPiecePosition);
            if (diagonalPiece != null && diagonalPiece.isWhite() != currentPiece.isWhite()) {
                legalMoves.getLegalMoves().add(new Move(position.copy(), diagonalPiecePosition));
            }
        }

        if (position.x() + 1 < Board.SIZE) {
            BoardPosition diagonalPiecePosition = new BoardPosition(position.x() + 1, forwardY);
            BoardPiece diagonalPiece = board.getBoardPiece(diagonalPiecePosition);
            if (diagonalPiece != null && diagonalPiece.isWhite() != currentPiece.isWhite()) {
                legalMoves.getLegalMoves().add(new Move(position.copy(), diagonalPiecePosition));
            }
        }
    }
}
