package dev.lelek.chess.move_generation;

import dev.lelek.chess.BoardPosition;
import dev.lelek.chess.Color;
import dev.lelek.chess.Move.EnPassantMove;
import dev.lelek.chess.Move.Move;
import dev.lelek.chess.Move.PromotionMove;
import dev.lelek.chess.board.BoardPiece;
import dev.lelek.chess.board.OccupancyBitboard;
import dev.lelek.chess.board.model.BitBoardState;
import dev.lelek.chess.board.model.Board;
import dev.lelek.chess.board.model.CastlingRights;

import java.util.ArrayList;
import java.util.List;

public class PseudoLegalMoveFinder {
    public static List<Move> getPseudoLegalMoves(Board board, boolean isWhiteToMove) {
        List<Move> legalMoves = new ArrayList<>();
        BitBoardState bitBoardState = board.getPosition();
        for (int i = 0; i < BoardPiece.values().length; i++) {
            // iterating through bitboards of pieces
            BoardPiece currentPiece = BoardPiece.values()[i];
            if (currentPiece.isWhite() != isWhiteToMove) {
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
                if (currentPosition.x() + direction[0] < 0 || currentPosition.x() + direction[0] >= Board.SIZE || currentPosition.y() + direction[1] < 0 || currentPosition.y() + direction[1] >= Board.SIZE) {
                    interrupted = true;
                    continue;
                }
                currentPosition = currentPosition.move(direction);
                if (board.getPieceList()[currentPosition.getBitBoardSquare()] != null) {
                    interrupted = true;
                    if (board.getPieceList()[currentPosition.getBitBoardSquare()].hasSameColor(boardPiece)) {
                        continue;
                    }
                }
                legalMoves.add(new Move(position, currentPosition));
            } while (currentPieceMoveRules.canMoveInfinitely() && ! interrupted);
        }
    }

    private static void addPseudoLegalPawnMoves(Board board, BoardPiece boardPiece, BoardPosition boardPosition, List<Move> legalMoves) {
        BitBoardState bitBoardState = board.getPosition();
        Color color = (boardPiece.isWhite()) ? Color.WHITE : Color.BLACK;
        int direction = color.getMovingDirection();
        int startingY  = color.getPawnStartingRow();
        int forwardY = boardPosition.y() + direction;
        OccupancyBitboard opponentPieceBitBoard = color.getOpponentPieceBitboard();

        if (forwardY < 0 || forwardY >= Board.SIZE) {
            return;
        }

        // forward 1
        BoardPosition forward1 = new BoardPosition(boardPosition.x(), forwardY);
        if (! bitBoardState.getBit(OccupancyBitboard.ALL_PIECES, forward1)) {
            legalMoves.add(new Move(boardPosition, forward1));
        }

        // forward 2
        if (boardPosition.y() == startingY
                && ! bitBoardState.getBit(OccupancyBitboard.ALL_PIECES, new BoardPosition(boardPosition.x(), forwardY))
                && ! bitBoardState.getBit(OccupancyBitboard.ALL_PIECES, new BoardPosition(boardPosition.x(), forwardY + direction))) {
            legalMoves.add(new Move(boardPosition, new BoardPosition(boardPosition.x(), forwardY + direction)));
        }

        // diagonal
        if (boardPosition.x() - 1 >= 0) {
            BoardPosition diagonalPiecePosition = new BoardPosition(boardPosition.x() - 1, forwardY);
            if (bitBoardState.getBit(OccupancyBitboard.ALL_PIECES, diagonalPiecePosition) && bitBoardState.getBit(opponentPieceBitBoard, diagonalPiecePosition)) {
                legalMoves.add(new Move(boardPosition, diagonalPiecePosition));
            }
        }

        if (boardPosition.x() + 1 < Board.SIZE) {
            BoardPosition diagonalPiecePosition = new BoardPosition(boardPosition.x() + 1, forwardY);
            if (bitBoardState.getBit(OccupancyBitboard.ALL_PIECES, diagonalPiecePosition) && bitBoardState.getBit(opponentPieceBitBoard, diagonalPiecePosition)) {
                legalMoves.add(new Move(boardPosition, diagonalPiecePosition));
            }
        }
    }

    private static void addPseudoLegalPromotionMoves(Board board, boolean isWhiteToMove, List<Move> legalMoves) {
        BitBoardState bitBoardState = board.getPosition();
        Color color = (isWhiteToMove) ? Color.WHITE : Color.BLACK;
        int promotionRow = color.getBackRank();
        for (int i = legalMoves.size() - 1; i >= 0; i--) {
            if (! bitBoardState.getBit(color.getPawn(), legalMoves.get(i).from())) {
                continue;
            }
            if (legalMoves.get(i).to().y() == promotionRow) {
                legalMoves.add(new PromotionMove(legalMoves.get(i).from(), legalMoves.get(i).to(), null));
                legalMoves.remove(i);
            }
        }
    }

    private static void addPseudoLegalCastlingMoves(Board board, boolean isWhiteToMove, List<Move> legalMoves) {
        BitBoardState bitBoardState = board.getPosition();
        Color color = (isWhiteToMove) ? Color.WHITE : Color.BLACK;
        CastlingRights rights = isWhiteToMove ? board.getCastlingRightsWhite() : board.getCastlingRightsBlack();
        String rank = isWhiteToMove ? "1" : "8";

        if (rights.canCastleKingSide()
                && ! bitBoardState.getBit(OccupancyBitboard.ALL_PIECES, new BoardPosition("f" + rank))
                && ! bitBoardState.getBit(OccupancyBitboard.ALL_PIECES, new BoardPosition("g" + rank))) {
            legalMoves.add(color.getCastlingMoveKingSide());
        }

        if (rights.canCastleQueenSide()
                && ! bitBoardState.getBit(OccupancyBitboard.ALL_PIECES, new BoardPosition("d" + rank))
                && ! bitBoardState.getBit(OccupancyBitboard.ALL_PIECES, new BoardPosition("c" + rank))
                && ! bitBoardState.getBit(OccupancyBitboard.ALL_PIECES, new BoardPosition("b" + rank))) {
            legalMoves.add(color.getCastlingMoveQueenSide());
        }
    }

    private static void addPseudoLegalEnPassantMoves(Board board, boolean isWhiteToMove, List<Move> legalMoves) {
        BitBoardState bitBoardState = board.getPosition();
        Color color = (isWhiteToMove) ? Color.WHITE : Color.BLACK;
        if (board.getEnPassantTargetSquare() == null) {
            return;
        }

        BoardPosition enPassantPosition = board.getEnPassantPiecePosition();
        BoardPiece pawnBoardPiece = color.getPawn();


        if (enPassantPosition.x() - 1 >= 0 && bitBoardState.getBit(pawnBoardPiece, new BoardPosition(enPassantPosition.x() - 1, enPassantPosition.y()))) {
            legalMoves.add(new EnPassantMove(new BoardPosition(enPassantPosition.x() - 1, enPassantPosition.y()), new BoardPosition(board.getEnPassantTargetSquare().x(), board.getEnPassantTargetSquare().y())));
        }

        if (enPassantPosition.x() + 1 < Board.SIZE && bitBoardState.getBit(pawnBoardPiece, new BoardPosition(enPassantPosition.x() + 1, enPassantPosition.y()))) {
            legalMoves.add(new EnPassantMove(new BoardPosition(enPassantPosition.x() + 1, enPassantPosition.y()), new BoardPosition(board.getEnPassantTargetSquare().x(), board.getEnPassantTargetSquare().y())));
        }
    }
}