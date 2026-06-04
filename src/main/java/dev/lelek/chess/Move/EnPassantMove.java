package dev.lelek.chess.Move;

import dev.lelek.chess.BoardPosition;

public class EnPassantMove extends Move {
    public EnPassantMove(BoardPosition from, BoardPosition to) {
        super(from, to);
    }

    public EnPassantMove(int fromX, int fromY, int toX, int toY) {
        super(new BoardPosition(fromX, fromY), new BoardPosition(toX, toY));
    }

    public EnPassantMove(String from, String to) {
        super(new BoardPosition(from), new BoardPosition(to));
    }
}
