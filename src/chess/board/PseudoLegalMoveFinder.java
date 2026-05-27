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
        for (int i = 0; i < BoardPiece.values().length; i++) {
            // iterating through bitboards of pieces
            BoardPiece currentPiece = BoardPiece.values()[i];
            if (currentPiece.isWhite() != isWhiteToMove) { // skips bitboards that only exist for performance optimizations
                continue;
            }
            addPseudoLegalMovesOfBitboard(board, currentPiece, legalMoves);
        }
        addPseudoLegalCastlingMoves(board, isWhiteToMove, legalMoves);
        addPseudoLegalPromotionMoves(board, isWhiteToMove, legalMoves);
        addPseudoLegalEnPassantMoves(board, isWhiteToMove, legalMoves);

        return legalMoves;
    }

    private static void addPseudoLegalMovesOfBitboard(Board board, BoardPiece boardPiece, List<Move> legalMoves) {
        long bitboard = board.getPosition().getBitboard(boardPiece.getBitboardIndex());
        while (bitboard != 0) {
            long leastSignificantBit = bitboard & -bitboard; // todo why / how does this work
            int bitBoardSquare = Long.numberOfTrailingZeros(leastSignificantBit);
            BoardPosition boardPosition = new BoardPosition(bitBoardSquare);

            // pawn
            if (boardPiece == BoardPiece.BLACK_PAWN || boardPiece == BoardPiece.WHITE_PAWN) {
                addPseudoLegalPawnMoves(board, boardPiece, boardPosition, legalMoves);
            } else {
                // general
                addPseudoLegalMovesOfGeneralPiece(board, boardPiece, boardPosition, legalMoves);
            }
            bitboard &= bitboard - 1; // remove extracted bit
        }
    }

    private static void addPseudoLegalMovesOfGeneralPiece(Board board, BoardPiece boardPiece, BoardPosition position, List<Move> legalMoves) { // TODO refactor
        PieceMoveRules currentPieceMoveRules = boardPiece.getMoveRules();

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
                    if (board.getBoardPiece(currentPosition).hasSameColor(boardPiece)) {
                        continue;
                    }
                }
                legalMoves.add(new Move(position, currentPosition));
            } while (currentPieceMoveRules.canMoveInfinitely() && !interrupted);
        }
    }

    private static void addPseudoLegalPawnMoves(Board board, BoardPiece boardPiece, BoardPosition boardPosition, List<Move> legalMoves) {
        Position position = board.getPosition();
        Color color = (boardPiece.isWhite()) ? Color.WHITE : Color.BLACK;
        int direction = color.getMovingDirection();
        int startingY  = color.getPawnStartingRow();
        int forwardY = boardPosition.y() + direction;
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
        Position position = board.getPosition();
        Color color = (isWhiteToMove) ? Color.WHITE : Color.BLACK;
        int promotionRow = color.getBackRank();
        for (int i = legalMoves.size() - 1; i >= 0; i--) {
            if (! position.getBit(color.getPawnsBitboard(), legalMoves.get(i).from())) {
                continue;
            }
            if (legalMoves.get(i).to().y() == promotionRow) {
                legalMoves.add(new PromotionMove(legalMoves.get(i).from(), legalMoves.get(i).to(), null));
                legalMoves.remove(i);
            }
        }
    }

    private static void addPseudoLegalCastlingMoves(Board board, boolean isWhiteToMove, List<Move> legalMoves) {
        Position position = board.getPosition();
        Color color = (isWhiteToMove) ? Color.WHITE : Color.BLACK;
        CastlingRights rights = isWhiteToMove ? board.getCastlingRightsWhite() : board.getCastlingRightsBlack();
        String rank = isWhiteToMove ? "1" : "8";

        if (rights.canCastleKingSide()
                && ! position.getBit(new BoardPosition("f" + rank))
                && ! position.getBit(new BoardPosition("g" + rank))) {
            legalMoves.add(color.getCastlingMoveKingSide());
        }

        if (rights.canCastleQueenSide()
                && ! position.getBit(new BoardPosition("d" + rank))
                && ! position.getBit(new BoardPosition("c" + rank))
                && ! position.getBit(new BoardPosition("b" + rank))) {
            legalMoves.add(color.getCastlingMoveQueenSide());
        }
    }

    private static void addPseudoLegalEnPassantMoves(Board board, boolean isWhiteToMove, List<Move> legalMoves) {
        Position position = board.getPosition();
        Color color = (isWhiteToMove) ? Color.WHITE : Color.BLACK;
        if (board.getEnPassantTargetSquare() == null) {
            return;
        }

        BoardPosition enPassantPosition = board.getEnPassantPiecePosition();
        PieceBitboard pawnsBitBoard = color.getPawnsBitboard();

        BoardPosition pieceToMoveFrom1 = new BoardPosition(enPassantPosition.x() - 1, enPassantPosition.y());
        if (pieceToMoveFrom1.x() >= 0 && position.getBit(pawnsBitBoard, pieceToMoveFrom1)) {
            legalMoves.add(new EnPassantMove(pieceToMoveFrom1, new BoardPosition(board.getEnPassantTargetSquare().x(), board.getEnPassantTargetSquare().y())));
        }

        BoardPosition pieceToMoveFrom2 = new BoardPosition(enPassantPosition.x() + 1, enPassantPosition.y());
        if (pieceToMoveFrom2.x() < Board.SIZE && position.getBit(pawnsBitBoard, pieceToMoveFrom2)) {
            legalMoves.add(new EnPassantMove(pieceToMoveFrom2, new BoardPosition(board.getEnPassantTargetSquare().x(), board.getEnPassantTargetSquare().y())));
        }
    }
}