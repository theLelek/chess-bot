package chess.board;


public enum PieceMoveRules {

    QUEEN(new int[][]{{-1, -1}, {0, -1}, {1, -1}, {-1,  0}, {1,  0}, {-1,  1}, {0,  1}, {1,  1}}, false, true),
    BISHOP(new int[][]{{1,  1}, {-1, 1}, {1, -1}, {-1, -1}}, false, true),
    ROOK(new int[][]{{-1, 0}, {0, -1}, {0,  1}, {1,  0}}, false, true),
    KNIGHT(new int[][]{{1, -2}, {-1, -2}, {2, -1}, {-2, -1}, {2,  1}, {-2,  1}, {1,  2}, {-1,  2}}, true, false),
    KING(new int[][]{{-1, -1}, {0, -1}, {1, -1}, {-1,  0}, {1,  0}, {-1,  1}, {0,  1}, { 1,  1}}, false, false),
    PAWN(null, false, false); // TODO not sure what do do yet with pawn

    private final int[][] directions;
    private final boolean canJump;
    private final boolean canMoveInfinitely;

    PieceMoveRules(int[][] directions, boolean canJump, boolean canMoveInfinitely) {
        this.directions = directions;
        this.canJump = canJump;
        this.canMoveInfinitely = canMoveInfinitely;
    }


    public int[][] getDirections() {
        return directions;
    }

    public boolean canJump() {
        return canJump;
    }

    public boolean canMoveInfinitely() {
        return canMoveInfinitely;
    }
}
