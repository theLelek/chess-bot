package chess.board;


public enum PieceMoveRules {
    ROOK({{-1, -1}, {-1, 0}, {-1, 1}, { 0, -1},{ 0, 1},{ 1, -1}, { 1, 0}, { 1, 1}}, false, true),
    BISHOP(), QUEEN(), KNIGHT(), PAWN(), KING();

    private int[][] directions;
    private boolean canJump;
    private boolean canMoveInfinitely;


    PieceMoveRules(int[][] directions, boolean canJump, boolean canMoveInfinitely) {
        this.directions = directions;
        this.canJump = canJump;
        this.canMoveInfinitely = canMoveInfinitely;
    }
}
