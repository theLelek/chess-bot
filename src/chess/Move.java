package chess;

import java.util.Objects;

public record Move(int fromX, int fromY, int toX, int toY) {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return toX == move.toX && toY == move.toY && fromX == move.fromX && fromY == move.fromY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromX, fromY, toX, toY);
    }

    @Override
    public String toString() {
        return "Move{" +
                "fromX=" + fromX +
                ", fromY=" + fromY +
                ", toX=" + toX +
                ", toY=" + toY +
                '}';
    }
}