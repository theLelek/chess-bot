package dev.lelek.chess.eval;

import dev.lelek.chess.board.model.Board;

public class PieceSquareTableHandler {

    private final double gamePhase;

    public static PieceSquareTableHandler fromBoard(Board board) {
         double score = (double) BoardEvaluation.computeBoardValue(board) / BoardEvaluation.DEFAULT_BOARD_VALUE;
         return new PieceSquareTableHandler(score);
    }

    private PieceSquareTableHandler(double gamePhase) {
        this.gamePhase = gamePhase;
    }

    public double getGamePhase() {
        return gamePhase;
    }
}
