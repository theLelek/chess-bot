package dev.lelek;

import dev.lelek.chess.board.model.Fen;
import dev.lelek.chess.Move.Move;
import dev.lelek.chess.board.model.Board;
import dev.lelek.chess.search.MoveGenerator;

public class Test {
    public static void main(String[] args) {
        Runtime rt = Runtime.getRuntime();
        Board board = Fen.fromFen("7k/3R4/1R6/8/8/8/8/7K w - - 1 1");
        Move move = MoveGenerator.generateMove(board, 1000L);

        long used = rt.totalMemory() - rt.freeMemory();
        long max = rt.maxMemory();


        System.out.printf("Used: %.1f MB%n", used / 1024.0 / 1024.0);
        System.out.printf("Max : %.1f MB%n", max / 1024.0 / 1024.0);


    }
}
