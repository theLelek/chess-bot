package chess.board;

import chess.Color;
import chess.Move.EnPassantMove;
import chess.Move.Move;
import chess.Move.PromotionMove;
import chess.board.model.*;
import chess.BoardPosition;

import java.util.ArrayList;
import java.util.List;

public class PseudoLegalMoveFinder {
    public static List<Move> getPseudoLegalMoves(Board board, boolean isWhiteToMove) {
        List<Move> legalMoves = new ArrayList<>();
        Position position = board.getPosition();
        for (int i = 0; i < position.getBitboards().length; i++) {
            // iterating through bitboards of pieces
            PieceBitboard pieceBitboard = PieceBitboard.fromIndex(i);
            BoardPiece currentPiece = BoardPiece.fromPieceBitBoard(pieceBitboard);
            if (currentPiece == null || currentPiece.isWhite() != isWhiteToMove) { // skips bitboards that only exist for performance optimizations
                continue;
            }
            addPseudoLegalMovesOfBitboard(board, pieceBitboard, legalMoves);
        }
        addPseudoLegalCastlingMoves(board, isWhiteToMove, legalMoves);
        addPseudoLegalPromotionMoves(board, isWhiteToMove, legalMoves);
        addPseudoLegalEnPassantMoves(board, isWhiteToMove, legalMoves);

        return legalMoves;
    }

    private static void addPseudoLegalMovesOfBitboard(Board board, PieceBitboard pieceBitboard, List<Move> legalMoves) {
        long bitboard = board.getPosition().getBitboard(pieceBitboard);
        while (bitboard != 0) {
            long leastSignificantBit = bitboard & -bitboard; // todo why / how does this work
            int bitBoardSquare = Long.numberOfTrailingZeros(leastSignificantBit);
            BoardPosition boardPosition = new BoardPosition(bitBoardSquare);

            // pawn
            if (pieceBitboard == PieceBitboard.BLACK_PAWNS || pieceBitboard == PieceBitboard.WHITE_PAWNS) {
                addPseudoLegalPawnMoves(board, pieceBitboard, boardPosition, legalMoves);
            } else {
                // general
                addPseudoLegalMovesOfGeneralPiece(board, boardPosition, legalMoves);
            }
            bitboard &= bitboard - 1; // remove extracted bit
        }
    }

    private static void addPseudoLegalMovesOfGeneralPiece(Board board, BoardPosition position, List<Move> legalMoves) { // TODO refactor
        BoardPiece currentPiece = board.getBoardPieces()[position.y()][position.x()];
        PieceMoveRules currentPieceMoveRules = currentPiece.getMoveRules();

        for (int[] direction : currentPieceMoveRules.getDirections()) {
            BoardPosition currentPosition = position.copy();
            boolean interrupted = false;
            do {
                try {
                    currentPosition = currentPosition.move(direction);
                } catch (IndexOutOfBoundsException e) {
                    interrupted = true;
                    continue;
                }
                if (board.getBoardPiece(currentPosition) != null) {
                    interrupted = true;
                    if (board.getBoardPiece(currentPosition).hasSameColor(currentPiece)) {
                        continue;
                    }
                }
                legalMoves.add(new Move(position, currentPosition));
            } while (currentPieceMoveRules.canMoveInfinitely() && !interrupted);
        }
    }

    private static void addPseudoLegalPawnMoves(Board board, PieceBitboard pieceBitboard, BoardPosition boardPosition, List<Move> legalMoves) {
        Position position = board.getPosition();
        BoardPiece boardPiece = BoardPiece.fromPieceBitBoard(pieceBitboard);
        Color color = (boardPiece.isWhite()) ? Color.WHITE : Color.BLACK;
        int direction = color.getMovingDirection();
        int startingY  = color.getPawnStartingRow();
        int forwardY = boardPosition.y() + direction;
        PieceBitboard ownPieceBitBoard = color.getOwnPieceBitboard();
        PieceBitboard opponentPieceBitBoard = color.getOpponentPieceBitboard();

        if (forwardY < 0 || forwardY >= Board.SIZE) {
            return;
        }

        // forward 1
        BoardPosition forward1 = new BoardPosition(boardPosition.x(), forwardY);
        if (! position.getBit(forward1)) {
            legalMoves.add(new Move(boardPosition, forward1));
        }

        // forward 2
        if (boardPosition.y() == startingY
                && ! position.getBit(new BoardPosition(boardPosition.x(), forwardY))
                && ! position.getBit(new BoardPosition(boardPosition.x(), forwardY + direction))) {
            legalMoves.add(new Move(boardPosition, new BoardPosition(boardPosition.x(), forwardY + direction)));
        }

        // diagonal
        if (boardPosition.x() - 1 >= 0) {
            BoardPosition diagonalPiecePosition = new BoardPosition(boardPosition.x() - 1, forwardY);
            if (position.getBit(diagonalPiecePosition) && position.getBit(opponentPieceBitBoard, diagonalPiecePosition)) {
                legalMoves.add(new Move(boardPosition, diagonalPiecePosition));
            }
        }

        if (boardPosition.x() + 1 < Board.SIZE) {
            BoardPosition diagonalPiecePosition = new BoardPosition(boardPosition.x() + 1, forwardY);
            if (position.getBit(diagonalPiecePosition) && position.getBit(opponentPieceBitBoard, diagonalPiecePosition)) {
                legalMoves.add(new Move(boardPosition, diagonalPiecePosition));
            }
        }
    }

    private static void addPseudoLegalPromotionMoves(Board board, boolean isWhiteToMove, List<Move> legalMoves) {
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

    private static void addPseudoLegalCastlingMoves(Board board, boolean isWhiteToMove, List<Move> legalMoves) {
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

    private static void addPseudoLegalEnPassantMoves(Board board, boolean isWhiteToMove, List<Move> legalMoves) {
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