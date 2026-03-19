package chess;

import java.util.Objects;

public record Move(BoardPosition from, BoardPosition to) {

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

    public static class Builder {

        private BoardPosition from;
        private BoardPosition to;

        public Builder from(BoardPosition from) {
            this.from = from;
            return this;
        }

        public Builder to(BoardPosition to) {
            this.to = to;
            return this;
        }

        public Move build() {
            return new Move(from, to);
        }
    }
}