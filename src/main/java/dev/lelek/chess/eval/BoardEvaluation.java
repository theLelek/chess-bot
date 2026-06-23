package dev.lelek.chess.search;

import dev.lelek.chess.Color;
import dev.lelek.chess.BoardPiece;
import dev.lelek.chess.board.OccupancyBitboard;
import dev.lelek.chess.board.model.Board;

import java.util.Arrays;

class BoardEvaluation {

    private static final int PAWN_VALUE = 100;
    private static final int KNIGHT_VALUE = 350;
    private static final int BISHOP_VALUE = 350;
    private static final int ROOK_VALUE = 525;
    private static final int QUEEN_VALUE = 1000;
    private static final int KING_VALUE = 100_000;

    public static int evaluate(Board board, Color color) {
        int evaluation = 0;
        int neutralEvaluation = 0;

        long bb = board.getBitBoardState().getBitboard(OccupancyBitboard.ALL_PIECES);
        while (bb != 0) {
            long lsb = bb & -bb;
            bb &= bb - 1;
            BoardPiece piece = board.getPieceList()[Long.numberOfTrailingZeros(lsb)];

            neutralEvaluation += getValue(piece);
            evaluation = piece.getColor() == color ? evaluation + getValue(piece) : evaluation - getValue(piece);
        }

        GamePhase gamePhase = GamePhase.fromEvaluation(neutralEvaluation);

        return evaluation;
    }

    static int getValue(BoardPiece piece) {
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

enum GamePhase {

    OPENING(6500),
    MIDDLEGAME(3500),
    ENDGAME(Integer.MIN_VALUE);

    private final int minEvaluation;

    GamePhase(int minEvaluation) {
        this.minEvaluation = minEvaluation;
    }

    static GamePhase fromBoard(Board board) {
        int evaluation = 0;

        long bb = board.getBitBoardState().getBitboard(OccupancyBitboard.ALL_PIECES);
        while (bb != 0) {
            long lsb = bb & -bb;
            bb &= bb - 1;
            BoardPiece piece = board.getPieceList()[Long.numberOfTrailingZeros(lsb)];

            evaluation += BoardEvaluation.getValue(piece);
        }

        GamePhase gamePhase = GamePhase.fromEvaluation(evaluation);


    }

    static GamePhase fromEvaluation(int evaluation) {
        GamePhase[] phases = GamePhase.values();
        Arrays.sort(phases);

        for (GamePhase phase : GamePhase.values()) {
            if (evaluation > phase.minEvaluation) {
                return phase;
            }
        }

        throw new IllegalArgumentException("Invalid evaluation: " + evaluation);
    }

    public int getMinEvaluation() {
        return minEvaluation;
    }
}