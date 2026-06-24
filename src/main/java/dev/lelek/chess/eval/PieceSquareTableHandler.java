package dev.lelek.chess.eval;

import dev.lelek.chess.BoardPiece;
import dev.lelek.chess.BoardPosition;
import dev.lelek.chess.board.model.Board;

class PieceSquareTableHandler {

    private final double gamePhase;
    private final double openingPercent;
    private final double middlePercent;
    private final double endPercent;

    public PieceSquareTableHandler(double gamePhase, double openingPercent, double middlePercent, double endPercent) {
        this.gamePhase = gamePhase;
        this.openingPercent = openingPercent;
        this.middlePercent = middlePercent;
        this.endPercent = endPercent;
    }


    public int getEvaluation(BoardPiece piece, BoardPosition position) {
        int[][][] tables = PieceSquareTables.fromPiece(piece);
        double evaluation = 0;
        evaluation += tables[0][position.y()][position.x()] * openingPercent;
        evaluation += tables[1][position.y()][position.x()] * middlePercent;
        evaluation += tables[2][position.y()][position.x()] * endPercent;
        return (int) evaluation;
    }

    public static PieceSquareTableHandler fromBoard(Board board) {
        double score = (double) BoardEvaluation.computeBoardValue(board) / BoardEvaluation.DEFAULT_BOARD_VALUE;
        // score = 1 -> opening
        // score = 0.75 -> half opening and half middle game

        double openingPercent;
        double middlePercent;
        double endPercent;

        if (score >= 0.5) {
            openingPercent = (score - 0.5) / 0.5;
            middlePercent = 1.0 - openingPercent;
            endPercent = 0.0;
        } else {
            // Middlegame <-> Endgame transition
            endPercent = (0.5 - score) / 0.5;
            middlePercent = 1.0 - endPercent;
            openingPercent = 0.0;
        }
        return new PieceSquareTableHandler(score, openingPercent, middlePercent, endPercent);
    }

    public double getGamePhase() {
        return gamePhase;
    }
}