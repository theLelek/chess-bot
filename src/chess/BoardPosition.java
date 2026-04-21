package chess;

import chess.board.model.Board;

import java.util.Objects;

public class BoardPosition {

    private final int x;
    private final int y;

    public BoardPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public BoardPosition(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        str = str.toLowerCase();
        if (str.length() != 2) {
            throw new IllegalArgumentException();
        }
        this.x = str.charAt(0) - 97;
        this.y = Math.abs(str.charAt(1) - 48 - Board.SIZE);
        if(this.x < 0 || this.x >= Board.SIZE || this.y < 0 || this.y >= Board.SIZE) {
            throw new IllegalArgumentException();
        }
    }

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

    @Override
    public String toString() {
        return (char) (x + 97) + String.valueOf(Math.abs(y - Board.SIZE));
    }

    public BoardPosition copy() {
        return new BoardPosition(x, y);
    }

    public BoardPosition move(int[] direction) throws IndexOutOfBoundsException {
        if (x + direction[0] < 0 || y + direction[1] < 0 || x + direction[0] >= Board.SIZE || y + direction[1] >= Board.SIZE) {
            throw new IndexOutOfBoundsException("Move index out of bounds");
        }
        return new BoardPosition(x + direction[0], y + direction[1]);
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
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
            return new BoardPosition(x, y);
        }
    }
}
