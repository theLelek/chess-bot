package dev.lelek.chess.search;

import dev.lelek.chess.BoardPosition;
import dev.lelek.chess.Color;
import dev.lelek.chess.Move.EnPassantMove;
import dev.lelek.chess.Move.Move;
import dev.lelek.chess.Move.PromotionMove;
import dev.lelek.chess.BoardPiece;
import dev.lelek.chess.board.OccupancyBitboard;
import dev.lelek.chess.board.model.BitBoardState;
import dev.lelek.chess.board.model.Board;
import dev.lelek.chess.board.model.CastlingRights;

import java.util.ArrayList;
import java.util.List;

public class PseudoLegalMoveFinder {

    private static final int[][] queenMoves = {{-1, -1}, {0, -1}, {1, -1}, {-1,  0}, {1,  0}, {-1,  1}, {0,  1}, {1,  1}};
    private static final int[][] rookMoves = {{-1, 0}, {0, -1}, {0,  1}, {1,  0}};
    private static final int[][] bishopMoves = {{1,  1}, {-1, 1}, {1, -1}, {-1, -1}};
    private static final int[][] knightMoves = {{1, -2}, {-1, -2}, {2, -1}, {-2, -1}, {2,  1}, {-2,  1}, {1,  2}, {-1,  2}};
    private static final int[][] kingMoves = {{-1, -1}, {0, -1}, {1, -1}, {-1,  0}, {1,  0}, {-1,  1}, {0,  1}, { 1,  1}};

    public static List<Move> getPseudoLegalMoves(Board board, boolean isWhiteToMove) { // todo remove boolena paramether
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
        long bitboard = board.getPosition().getBitboard(boardPiece);
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
        int[][] currentPieceMovesRules = getPieceMoves(boardPiece);

        for (int[] direction : currentPieceMovesRules) {
            BoardPosition currentPosition = position.copy();
            boolean interrupted = false;

            do {
                if (currentPosition.getX() + direction[0] < 0 || currentPosition.getX() + direction[0] >= Board.SIZE || currentPosition.getY() + direction[1] < 0 || currentPosition.getY() + direction[1] >= Board.SIZE) {
                    interrupted = true;
                    continue;
                }
                currentPosition = currentPosition.move(direction);
                if (board.getPieceAt(currentPosition) != null) {
                    interrupted = true;
                    if (board.getPieceAt(currentPosition).hasSameColor(boardPiece)) {
                        continue;
                    }
                }
                legalMoves.add(new Move(position, currentPosition));
            } while ((boardPiece.isBishop() || boardPiece.isRook() || boardPiece.isQueen()) && ! interrupted);
        }
    }

    private static void addPseudoLegalPawnMoves(Board board, BoardPiece boardPiece, BoardPosition boardPosition, List<Move> legalMoves) {
        BitBoardState bitBoardState = board.getPosition();
        Color color = (boardPiece.isWhite()) ? Color.WHITE : Color.BLACK;
        int direction = color.getMovingDirection();
        int startingY  = color.getPawnStartingRow();
        int forwardY = boardPosition.getY() + direction;
        OccupancyBitboard opponentPieceBitBoard = color.getOpponentPieceBitboard();

        if (forwardY < 0 || forwardY >= Board.SIZE) {
            return;
        }

        // forward 1
        BoardPosition forward1 = new BoardPosition(boardPosition.getX(), forwardY);
        if (! bitBoardState.getBit(OccupancyBitboard.ALL_PIECES, forward1)) {
            legalMoves.add(new Move(boardPosition, forward1));
        }

        // forward 2
        if (boardPosition.getY() == startingY
                && ! bitBoardState.getBit(OccupancyBitboard.ALL_PIECES, new BoardPosition(boardPosition.getX(), forwardY))
                && ! bitBoardState.getBit(OccupancyBitboard.ALL_PIECES, new BoardPosition(boardPosition.getX(), forwardY + direction))) {
            legalMoves.add(new Move(boardPosition, new BoardPosition(boardPosition.getX(), forwardY + direction)));
        }

        // diagonal
        if (boardPosition.getX() - 1 >= 0) {
            BoardPosition diagonalPiecePosition = new BoardPosition(boardPosition.getX() - 1, forwardY);
            if (bitBoardState.getBit(OccupancyBitboard.ALL_PIECES, diagonalPiecePosition) && bitBoardState.getBit(opponentPieceBitBoard, diagonalPiecePosition)) {
                legalMoves.add(new Move(boardPosition, diagonalPiecePosition));
            }
        }

        if (boardPosition.getX() + 1 < Board.SIZE) {
            BoardPosition diagonalPiecePosition = new BoardPosition(boardPosition.getX() + 1, forwardY);
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
            if (! bitBoardState.getBit(color.getPawn(), legalMoves.get(i).getFrom())) {
                continue;
            }
            if (legalMoves.get(i).getTo().getY() == promotionRow) {
                legalMoves.add(new PromotionMove(legalMoves.get(i).getFrom(), legalMoves.get(i).getTo(), color.getQueen()));
                legalMoves.add(new PromotionMove(legalMoves.get(i).getFrom(), legalMoves.get(i).getTo(), color.getRook()));
                legalMoves.add(new PromotionMove(legalMoves.get(i).getFrom(), legalMoves.get(i).getTo(), color.getBishop()));
                legalMoves.add(new PromotionMove(legalMoves.get(i).getFrom(), legalMoves.get(i).getTo(), color.getKnight()));
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


        if (enPassantPosition.getX() - 1 >= 0 && bitBoardState.getBit(pawnBoardPiece, new BoardPosition(enPassantPosition.getX() - 1, enPassantPosition.getY()))) {
            legalMoves.add(new EnPassantMove(new BoardPosition(enPassantPosition.getX() - 1, enPassantPosition.getY()), new BoardPosition(board.getEnPassantTargetSquare().getX(), board.getEnPassantTargetSquare().getY())));
        }

        if (enPassantPosition.getX() + 1 < Board.SIZE && bitBoardState.getBit(pawnBoardPiece, new BoardPosition(enPassantPosition.getX() + 1, enPassantPosition.getY()))) {
            legalMoves.add(new EnPassantMove(new BoardPosition(enPassantPosition.getX() + 1, enPassantPosition.getY()), new BoardPosition(board.getEnPassantTargetSquare().getX(), board.getEnPassantTargetSquare().getY())));
        }
    }

    private static int[][] getPieceMoves(BoardPiece boardPiece) {
        if (boardPiece.isKing()) {
            return kingMoves;
        } else if (boardPiece.isQueen()) {
            return queenMoves;
        } else if (boardPiece.isRook()) {
            return rookMoves;
        } else if (boardPiece.isBishop()) {
            return bishopMoves;
        } else if (boardPiece.isKnight()) {
            return knightMoves;
        }
        throw new IllegalArgumentException("Unknown piece type: " + boardPiece);
    }
}