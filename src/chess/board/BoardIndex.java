package chess.board;

import java.util.Objects;

public record BoardIndex(int y, int x) { // TODO class name is confusing
    // TODO Maybe user builder because format is confusing because its y x not x y

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BoardIndex that = (BoardIndex) o;
        return y == that.y && x == that.x;
    }

    @Override
    public int hashCode() {
        return Objects.hash(y, x);
    }
}
