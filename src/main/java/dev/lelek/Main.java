package dev.lelek;

import dev.lelek.chess.board.model.Board;
import dev.lelek.chess.move_generation.MoveGenerator;

public class Main {
    public static void main(String[] args) {
        Board board = Board.initializeDefaultBoard();
        System.out.println(MoveGenerator.generateMove(board, 5));
    }
}