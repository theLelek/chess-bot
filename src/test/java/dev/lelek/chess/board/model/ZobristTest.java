package dev.lelek.chess.board.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ZobristTest {

    @Test
    void fromBoard() {
        Board board = Board.initializeDefaultBoard();
        System.out.println(Zobrist.fromBoard(board));
    }
}