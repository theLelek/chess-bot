package dev.lelek;

import dev.lelek.chess.Move.Move;
import dev.lelek.chess.board.model.Board;
import dev.lelek.chess.search.MoveGenerator;

public class Test {
    public static void main(String[] args) {
        Board board = Board.initializeDefaultBoard();
        System.out.println("asdf");
        Move bestMove = MoveGenerator.generateMove(board, 1000L);
        System.out.println(bestMove);
        


    }
}
