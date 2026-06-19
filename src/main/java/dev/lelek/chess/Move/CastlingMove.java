package dev.lelek.chess.Move;

import dev.lelek.chess.BoardPosition;

public class CastlingMove extends Move {
    public CastlingMove(BoardPosition from, BoardPosition to) {
        super(from, to);
    }

    public CastlingMove(int fromX, int fromY, int toX, int toY) {
        super(new BoardPosition(fromX, fromY), new BoardPosition(toX, toY));
    }

    public CastlingMove(String from, String to) {
        super(new BoardPosition(from), new BoardPosition(to));
    }

    public boolean isKingSideCastling() {
        return to().x() == 6;
    }

    public boolean isQueenSideCastling() {
        return to().x() == 2;
    }
}
