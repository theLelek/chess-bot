package chess.Move;

import chess.BoardPosition;

import java.util.Objects;

public class Move {

    private final BoardPosition from;
    private final BoardPosition to;

    public Move(BoardPosition from, BoardPosition to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return Objects.equals(to, move.to) && Objects.equals(from, move.from);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    @Override
    public String toString() {
        return "from: " + from.toString() + " to: " + to.toString();
    }

    public BoardPosition from() {
        return from;
    }

    public BoardPosition to() {
        return to;
    }


    public static class Builder {

        private int fromX;
        private int fromY;
        private int toX;
        private int toY;

        public Builder fromX(int fromX) {
            this.fromX = fromX;
            return this;
        }

        public Builder fromY(int fromY) {
            this.fromY = fromY;
            return this;
        }

        public Builder toX(int toX) {
            this.toX = toX;
            return this;
        }

        public Builder toY(int toY) {
            this.toY = toY;
            return this;
        }

        public Move build() {
            return new Move(new BoardPosition.Builder().x(fromX).y(fromY).build(),
                    new BoardPosition.Builder().x(toX).y(toY).build());
        }
    }
}