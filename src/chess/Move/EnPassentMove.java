package chess.Move;

import chess.BoardPosition;

public class EnPassentMove extends Move {
    public EnPassentMove(BoardPosition from, BoardPosition to) {
        super(from, to);
    }

    public EnPassentMove(int fromX, int fromY, int toX, int toY) {
        super(new BoardPosition(fromX, fromY), new BoardPosition(toX, toY));
    }

    public EnPassentMove(String from, String to) {
        super(new BoardPosition(from), new BoardPosition(to));
    }
}
