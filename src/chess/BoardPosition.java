package chess;

import java.util.Objects;

public record BoardPosition(int x, int y) {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BoardPosition that = (BoardPosition) o;
        return y == that.y && x == that.x;
    }

    @Override
    public int hashCode() {
        return Objects.hash(y, x);
    }

    public static class Builder {

        private int y;
        private int x;

        public Builder y(int y) {
            this.y = y;
            return this;
        }

        public Builder x(int x) {
            this.x = x;
            return this;
        }

        public BoardPosition build() {
            return new BoardPosition(y, x);
        }
    }
}
