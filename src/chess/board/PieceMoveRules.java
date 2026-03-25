package chess.board;


public enum PieceMoveRules {

    QUEEN(new int[][]{{-1, -1}, {0, -1}, {1, -1}, {-1,  0}, {1,  0}, {-1,  1}, {0,  1}, {1,  1}}, true),
    BISHOP(new int[][]{{1,  1}, {-1, 1}, {1, -1}, {-1, -1}}, true),
    ROOK(new int[][]{{-1, 0}, {0, -1}, {0,  1}, {1,  0}}, true),
    KNIGHT(new int[][]{{1, -2}, {-1, -2}, {2, -1}, {-2, -1}, {2,  1}, {-2,  1}, {1,  2}, {-1,  2}}, false),
    KING(new int[][]{{-1, -1}, {0, -1}, {1, -1}, {-1,  0}, {1,  0}, {-1,  1}, {0,  1}, { 1,  1}}, false),
    PAWN(null, false); // TODO not sure what do do yet with pawn

    private final int[][] directions;
    private final boolean canMoveInfinitely;

    PieceMoveRules(int[][] directions, boolean canMoveInfinitely) {
        this.directions = directions;
        this.canMoveInfinitely = canMoveInfinitely;
    }


    public int[][] getDirections() {
        return directions;
    }

    public boolean canMoveInfinitely() {
        return canMoveInfinitely;
    }
}
