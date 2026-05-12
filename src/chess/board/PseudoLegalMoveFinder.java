package chess.board;

import chess.Color;
import chess.Move.EnPassantMove;
import chess.Move.Move;
import chess.Move.PromotionMove;
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
        getPseudoLegalCastlingMoves(board, isWhiteToMove, legalMoves);
        getPseudoLegalPromotionMoves(board, isWhiteToMove, legalMoves);
        getPseudoLegalEnPassantMoves(board, isWhiteToMove, legalMoves);
        return legalMoves;
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

    private static void getPseudoLegalPromotionMoves(Board board, boolean isWhiteToMove, List<Move> legalMoves) {
        Color color = (isWhiteToMove) ? Color.WHITE : Color.BLACK;
        int promotionRow = color.getBackRank();
        for (int i = legalMoves.size() - 1; i >= 0; i--) {
            BoardPiece piece = board.getBoardPiece(legalMoves.get(i).from());
            if (piece != BoardPiece.BLACK_PAWN && piece != BoardPiece.WHITE_PAWN) {
                continue;
            }
            if (legalMoves.get(i).to().y() == promotionRow) {
                legalMoves.add(new PromotionMove(legalMoves.get(i).from(), legalMoves.get(i).to(), null));
                legalMoves.remove(i);
            }
        }
    }

    private static void getPseudoLegalCastlingMoves(Board board, boolean isWhiteToMove, List<Move> legalMoves) {
        CastlingRights rights = isWhiteToMove ? board.getCastlingRightsWhite() : board.getCastlingRightsBlack();
        String rank = isWhiteToMove ? "1" : "8";
        Color color = (isWhiteToMove) ? Color.WHITE : Color.BLACK;

        if (rights.canCastleKingSide()
                && board.getBoardPiece(new BoardPosition("f" + rank)) == null
                && board.getBoardPiece(new BoardPosition("g" + rank)) == null) {
            legalMoves.add(color.getCastlingMoveKingSide());
        }

        if (rights.canCastleQueenSide()
                && board.getBoardPiece(new BoardPosition("d" + rank)) == null
                && board.getBoardPiece(new BoardPosition("c" + rank)) == null
                && board.getBoardPiece(new BoardPosition("b" + rank)) == null) {
            legalMoves.add(color.getCastlingMoveQueenSide());
        }
    }

    private static void getPseudoLegalEnPassantMoves(Board board, boolean isWhiteToMove, List<Move> legalMoves) {
        Color color = (isWhiteToMove) ? Color.WHITE : Color.BLACK;
        if (board.getEnPassantTargetSquare() == null) {
            return;
        }
        BoardPosition enPassantPosition = board.getEnPassantPiecePosition();

        BoardPosition pieceToMoveFrom1 = new BoardPosition(enPassantPosition.x() - 1, enPassantPosition.y());
        if (pieceToMoveFrom1.x() >= 0 && board.getBoardPiece(pieceToMoveFrom1) != null && board.getBoardPiece(pieceToMoveFrom1).isPawn() && board.getBoardPiece(pieceToMoveFrom1).isWhite() == isWhiteToMove) {
            legalMoves.add(new EnPassantMove(pieceToMoveFrom1, new BoardPosition(board.getEnPassantTargetSquare().x(), board.getEnPassantTargetSquare().y())));
        }

        BoardPosition pieceToMoveFrom2 = new BoardPosition(enPassantPosition.x() + 1, enPassantPosition.y());
        if (pieceToMoveFrom2.x() < Board.SIZE && board.getBoardPiece(pieceToMoveFrom2) != null && board.getBoardPiece(pieceToMoveFrom2).isPawn() && board.getBoardPiece(pieceToMoveFrom2).isWhite() == isWhiteToMove) {
            legalMoves.add(new EnPassantMove(pieceToMoveFrom2, new BoardPosition(board.getEnPassantTargetSquare().x(), board.getEnPassantTargetSquare().y())));
        }
    }
}