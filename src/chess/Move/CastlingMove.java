package chess.Move;

import chess.BoardPosition;

public class CastlingMove extends Move {
    public CastlingMove(BoardPosition from, BoardPosition to) {
        super(from, to);
    }

    public CastlingMove(int fromX, int fromY, int toX, int toY) {
        super(new BoardPosition(fromX, fromY), new BoardPosition(toX, toY));
    }
}
