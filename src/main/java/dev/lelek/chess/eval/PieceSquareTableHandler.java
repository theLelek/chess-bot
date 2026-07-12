package dev.lelek.chess.eval;

import dev.lelek.chess.BoardPiece;
import dev.lelek.chess.BoardPosition;
import dev.lelek.chess.Color;
import dev.lelek.chess.board.model.Board;

class PieceSquareTableHandler {

    private final double gamePhase;
    private final double openingPercent;
    private final double middlePercent;
    private final double endPercent;

    PieceSquareTableHandler(double gamePhase, double openingPercent, double middlePercent, double endPercent) {
        this.gamePhase = gamePhase;
        this.openingPercent = openingPercent;
        this.middlePercent = middlePercent;
        this.endPercent = endPercent;
    }

    int getEvaluation(BoardPiece piece, BoardPosition position) { // todo change so you dont only have 3 kind of evals
        int[][][] tables = PieceSquareTables.fromPiece(piece);
        double evaluation = 0;
        evaluation += tables[0][position.getY()][position.getX()] * openingPercent;
        evaluation += tables[1][position.getY()][position.getX()] * middlePercent;
        evaluation += tables[2][position.getY()][position.getX()] * endPercent;
        return (int) evaluation;
    }

    static PieceSquareTableHandler fromBoard(Board board) {
        double gamePhase = (double) BoardEvaluation.computeBoardValue(board) / BoardEvaluation.DEFAULT_BOARD_VALUE;
        // score = 1 -> opening
        // score = 0.75 -> half opening and half middle game

        double openingPercent;
        double middlePercent;
        double endPercent;

        if (gamePhase >= 0.5) {
            openingPercent = (gamePhase - 0.5) / 0.5;
            middlePercent = 1.0 - openingPercent;
            endPercent = 0.0;
        } else {
            endPercent = (0.5 - gamePhase) / 0.5;
            middlePercent = 1.0 - endPercent;
            openingPercent = 0.0;
        }
        return new PieceSquareTableHandler(gamePhase, openingPercent, middlePercent, endPercent);
    }

    double getGamePhase() {
        return gamePhase;
    }
}