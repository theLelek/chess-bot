package dev.lelek.chess.eval;

import dev.lelek.chess.BoardPiece;
import dev.lelek.chess.board.model.Board;

public class PieceSquareTables { // todo maybe convert to enum

    public static int[][][] fromPiece(BoardPiece piece) {
        switch (piece) {
            case WHITE_PAWN -> {return pawnsWhite;}
            case BLACK_PAWN -> {return pawnsBlack;}
            case WHITE_ROOK -> {return rooksWhite;}
            case BLACK_ROOK -> {return rooksBlack;}
            case WHITE_BISHOP -> {return bishopsWhite;}
            case BLACK_BISHOP -> {return bishopsBlack;}
            case WHITE_KNIGHT -> {return knightsWhite;}
            case BLACK_KNIGHT -> {return knightsBlack;}
            case WHITE_QUEEN -> {return queensWhite;}
            case BLACK_QUEEN -> {return queensBlack;}
            case WHITE_KING -> {return kingsWhite;}
            case BLACK_KING -> {return kingsBlack;}
            default -> throw new IllegalArgumentException("Unexpected value: " + piece);
        }
    }

    private static int[][][] mirror(int[][][] table) {
        int[][][] mirrored = new int[table.length][][];
        for (int i = 0; i < table.length; i++) {
            int[][] currentMirrored = new int[Board.SIZE][Board.SIZE];
            for (int j = 0; j < Board.SIZE; j++) {
                currentMirrored[j] = table[i][Board.SIZE - 1 - j].clone();
            }
            mirrored[i] = currentMirrored;
        }
        return mirrored;
    }

    private static final int[][] pawnWhite = {
            { 0,  0,   0,   0,   0,   0,  0,  0},
            {50, 50,  50,  50,  50,  50, 50, 50},
            {10, 10,  20,  30,  30,  20, 10, 10},
            { 5,  5,  10,  27,  27,  10,  5,  5},
            { 0,  0,   0,  25,  25,   0,  0,  0},
            { 5, -5, -10,   0,   0, -10, -5,  5},
            { 5, 10,  10, -25, -25,  10, 10,  5},
            { 0,  0,   0,   0,   0,   0,  0,  0}
    };

    private static final int[][] knightWhite = {
            {-50, -40, -30, -30, -30, -30, -40, -50},
            {-40, -20,   0,   0,   0,   0, -20, -40},
            {-30,   0,  10,  15,  15,  10,   0, -30},
            {-30,   5,  15,  20,  20,  15,   5, -30},
            {-30,   0,  15,  20,  20,  15,   0, -30},
            {-30,   5,  10,  15,  15,  10,   5, -30},
            {-40, -20,   0,   5,   5,   0, -20, -40},
            {-50, -40, -20, -30, -30, -20, -40, -50}
    };

    private static final int[][] bishopWhite = {
            {-20, -10, -10, -10, -10, -10, -10, -20},
            {-10,   0,   0,   0,   0,   0,   0, -10},
            {-10,   0,   5,  10,  10,   5,   0, -10},
            {-10,   5,   5,  10,  10,   5,   5, -10},
            {-10,   0,  10,  10,  10,  10,   0, -10},
            {-10,  10,  10,  10,  10,  10,  10, -10},
            {-10,   5,   0,   0,   0,   0,   5, -10},
            {-20, -10, -40, -10, -10, -40, -10, -20}
    };

    private static final int[][] rookWhite = {
            {  0,   0,   5,  10,  10,   5,   0,   0},
            { -5,   0,   0,   0,   0,   0,   0,  -5},
            { -5,   0,   0,   0,   0,   0,   0,  -5},
            { -5,   0,   0,   0,   0,   0,   0,  -5},
            { -5,   0,   0,   0,   0,   0,   0,  -5},
            { -5,   0,   0,   0,   0,   0,   0,  -5},
            {  5,  10,  10,  10,  10,  10,  10,   5},
            {  0,   0,   5,  10,  10,   5,   0,   0}
    };

    private static final int[][] queenOpeningWhite = {
            {-20, -10, -10,  -5,  -5, -10, -10, -20},
            {-10, -10, -10, -10, -10, -10, -10, -10},
            {-10, -10,   0,   0,   0,   0, -10, -10},
            { -5, -10,   0,   5,   5,   0, -10,  -5},
            { -5, -10,   0,   5,   5,   0, -10,  -5},
            {-10, -10,   0,   0,   0,   0, -10, -10},
            {-10, -10, -10, -10, -10, -10, -10, -10},
            {-20, -10, -10,  -5,  -5, -10, -10, -20}
    };

    private static final int[][] queenMiddleWhite = {
            {-20, -10, -10,  -5,  -5, -10, -10, -20},
            {-10,   0,   0,   0,   0,   0,   0, -10},
            {-10,   0,   5,   5,   5,   5,   0, -10},
            { -5,   0,   5,   5,   5,   5,   0,  -5},
            {  0,   0,   5,   5,   5,   5,   0,  -5},
            {-10,   5,   5,   5,   5,   5,   0, -10},
            {-10,   0,   5,   0,   0,   0,   0, -10},
            {-20, -10, -10,  -5,  -5, -10, -10, -20}
    };

    private static final int[][] queenEndWhite = {
            {-10,  -5,  -5,   0,   0,  -5,  -5, -10},
            { -5,   0,   5,   5,   5,   5,   0,  -5},
            { -5,   5,  10,  10,  10,  10,   5,  -5},
            {  0,   5,  10,  15,  15,  10,   5,   0},
            {  0,   5,  10,  15,  15,  10,   5,   0},
            { -5,   5,  10,  10,  10,  10,   5,  -5},
            { -5,   0,   5,   5,   5,   5,   0,  -5},
            {-10,  -5,  -5,   0,   0,  -5,  -5, -10}
    };

    private static final int[][] kingOpeningWhite = {
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-20, -30, -30, -40, -40, -30, -30, -20},
            {-10, -20, -20, -20, -20, -20, -20, -10},
            { 20,  20,   0,   0,   0,   0,  20,  20},
            { 20,  30,  10,   0,   0,  10,  30,  20}
    };

    private static final int[][] kingMiddleWhite = {
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-20, -30, -30, -40, -40, -30, -30, -20},
            {-10, -20, -20, -20, -20, -20, -20, -10},
            {  0, -10, -10, -10, -10, -10, -10,   0},
            { 10,  10,   0,   0,   0,   0,  10,  10},
            { 20,  20,  10,   0,   0,  10,  20,  20}
    };

    private static final int[][] kingEndWhite = {
            {-50, -40, -30, -20, -20, -30, -40, -50},
            {-30, -20, -10,   0,   0, -10, -20, -30},
            {-30, -10,  20,  30,  30,  20, -10, -30},
            {-30, -10,  30,  40,  40,  30, -10, -30},
            {-30, -10,  30,  40,  40,  30, -10, -30},
            {-30, -10,  20,  30,  30,  20, -10, -30},
            {-30, -30,   0,   0,   0,   0, -30, -30},
            {-50, -30, -30, -30, -30, -30, -30, -50}
    };


    static final int[][][] pawnsWhite = {pawnWhite, pawnWhite, pawnWhite};
    static final int[][][] pawnsBlack = mirror(pawnsWhite);

    static final int[][][] rooksWhite = {rookWhite, rookWhite, rookWhite};
    static final int[][][] rooksBlack = mirror(rooksWhite);

    static final int[][][] knightsWhite = {knightWhite, knightWhite, knightWhite};
    static final int[][][] knightsBlack = mirror(knightsWhite);

    static final int[][][] bishopsWhite = {bishopWhite, bishopWhite, bishopWhite};
    static final int[][][] bishopsBlack = mirror(bishopsWhite);


    static final int[][][] kingsWhite = {kingOpeningWhite, kingMiddleWhite, kingEndWhite};
    static final int[][][] kingsBlack = mirror(kingsWhite);

    static final int[][][] queensWhite = {queenOpeningWhite, queenMiddleWhite, queenEndWhite};
    static final int[][][] queensBlack = mirror(queensWhite);
}
