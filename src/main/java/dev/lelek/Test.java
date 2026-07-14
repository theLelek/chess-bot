package dev.lelek;

import dev.lelek.chess.Move.Move;
import dev.lelek.chess.board.model.Board;
import dev.lelek.chess.search.MoveGenerator;

public class Test {
    public static void main(String[] args) {
        Board board = Board.fromFen("7k/3R4/1R6/8/8/8/8/7K w - - 1 1");
        Move move = MoveGenerator.generateMove(board, 6);
        System.out.println(move);


    }
}
