package chess;

import chess.board.model.Board;

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

    @Override
    public String toString() {
        return (char)(x + 97) + String.valueOf(Math.abs(y - Board.SIZE));
    }

    public BoardPosition copy() {
        return new BoardPosition(x, y);
    }

    public static BoardPosition getFromString(String str) {
        if (str.length() != 2) {
            return null;
        }
        char x = str.charAt(0);
        int y = str.charAt(1) - 48;

        return new BoardPosition(x - 97, Math.abs(y - Board.SIZE));
    }

    public BoardPosition move(int[] direction) throws IndexOutOfBoundsException{
        if(x + direction[0] < 0 || y + direction[1] < 0 ||  x + direction[0] > 8 || y + direction[1] > 8){
            throw new IndexOutOfBoundsException("Move index out of bounds");
        }
        return new BoardPosition(x + direction[0], y + direction[1]);
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
