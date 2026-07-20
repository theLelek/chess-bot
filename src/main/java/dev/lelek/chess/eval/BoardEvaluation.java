package dev.lelek.chess.eval;

import dev.lelek.chess.BoardPosition;
import dev.lelek.chess.BoardPiece;
import dev.lelek.chess.Move.Move;
import dev.lelek.chess.board.model.Fen;
import dev.lelek.chess.board.OccupancyBitboard;
import dev.lelek.chess.board.model.Board;
import jdk.jshell.Snippet;

import java.awt.datatransfer.ClipboardOwner;
import java.util.List;

public class BoardEvaluation {

    static final int DEFAULT_BOARD_VALUE = computePiecesValue(Fen.initializeDefaultBoard());

    public static final int PAWN_VALUE = 100;
    public static final int KNIGHT_VALUE = 350;
    public static final int BISHOP_VALUE = 350;
    public static final int ROOK_VALUE = 525;
    public static final int QUEEN_VALUE = 1000;
    public static final int KING_VALUE = 0;

    public static int evaluate(Board board, List<Move> pseudoLegalMoves) {
        int value = evaluatePieces(board);
        int sign = board.isWhiteToMove() ? 1 : -1;


        value += sign * pseudoLegalMoves.size();
        return value;
    }

    private static int evaluatePieces(Board board) {
        int value = 0;

        PieceSquareTableHandler pieceSquareTableHandler = PieceSquareTableHandler.fromBoard(board);
        long bb = board.getBitBoardState().getBitboard(OccupancyBitboard.ALL_PIECES);
        while (bb != 0) {
            long lsb = bb & -bb;
            bb &= bb - 1;
            int bitBoardSquare = Long.numberOfTrailingZeros(lsb);

            BoardPosition position = new BoardPosition(bitBoardSquare);
            BoardPiece piece = board.getPieceAt(position);

            int pieceValue = getPieceValue(piece);
            pieceValue += pieceSquareTableHandler.getEvaluation(piece, position);
            pieceValue += PawnStructure.getPassedPawnValue(board, position);

            if (piece.isBlack()) pieceValue = -pieceValue;
            value += pieceValue;
        }
        return value;
    }

    private static int kingSafety(Board board) {
        int evaluation = 0;

        BoardPosition whiteKing = board.getWhiteKingPosition();
        BoardPosition aboveWhiteKing = getPosition(whiteKing.getX(), whiteKing.getY() - 1);
        BoardPosition aboveLeftWhiteKing = getPosition(whiteKing.getX() - 1, whiteKing.getY() - 1);
        BoardPosition aboveRightWhiteKing = getPosition(whiteKing.getX() + 1, whiteKing.getY() - 1);

        BoardPosition blackKing = board.getBlackKingPosition();
        BoardPosition aboveBlackKing = getPosition(whiteKing.getX(), whiteKing.getY() + 1);
        BoardPosition aboveLeftBlackKing = getPosition(whiteKing.getX() - 1, whiteKing.getY() + 1);
        BoardPosition aboveRightBlackKing = getPosition(whiteKing.getX() + 1, whiteKing.getY() + 1);

        evaluation += evaluateKingShieldPawns(new KingShieldPans(aboveLeftWhiteKing, aboveWhiteKing, aboveRightWhiteKing));
        evaluation -= evaluateKingShieldPawns(new KingShieldPans(aboveLeftBlackKing, aboveBlackKing, aboveRightBlackKing));
        return evaluation;
    }

    private static int evaluateKingShieldPawns(KingShieldPans kingShieldPans) {
        int evaluation = 0;
        if (kingShieldPans.middlePawn() == null) {
            evaluation -= PAWN_VALUE / 2;
        }
        if (kingShieldPans.leftPawn() == null) {
            evaluation -= PAWN_VALUE / 4;
        }
        if (kingShieldPans.rightPawn() == null) {
            evaluation -= PAWN_VALUE / 4;
        }
        return evaluation;
    }

    private static BoardPosition getPosition(int x, int y) {
        if (x >= Board.SIZE || x < 0 || y >= Board.SIZE || y < 0) {
            return null;
        }
        return new BoardPosition(x, y);
    }

    static int computePiecesValue(Board board) {
        int value = 0;
        long bb = board.getBitBoardState().getBitboard(OccupancyBitboard.ALL_PIECES);
        while (bb != 0) {
            long lsb = bb & -bb;
            bb &= bb - 1;
            int bitBoardSquare = Long.numberOfTrailingZeros(lsb);
            BoardPiece piece = board.getPieceList()[bitBoardSquare];

            value += getPieceValue(piece);
        }
        return value;
    }

    private static int getPieceValue(BoardPiece piece) {
        return switch (piece) {
            case WHITE_PAWN, BLACK_PAWN -> PAWN_VALUE;
            case WHITE_KNIGHT, BLACK_KNIGHT -> KNIGHT_VALUE;
            case WHITE_BISHOP, BLACK_BISHOP -> BISHOP_VALUE;
            case WHITE_ROOK, BLACK_ROOK -> ROOK_VALUE;
            case WHITE_QUEEN, BLACK_QUEEN -> QUEEN_VALUE;
            case WHITE_KING, BLACK_KING -> KING_VALUE;
        };
    }
}

record KingShieldPans(BoardPosition leftPawn, BoardPosition middlePawn, BoardPosition rightPawn) {}