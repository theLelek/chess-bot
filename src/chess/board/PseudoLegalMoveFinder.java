package chess.board;

public class PseudoLegalMoveFinder {
    // format: y x
    private static final int[][] KING_MOVES = {
            {-1, -1}, {-1, 0}, {-1, 1},
            { 0, -1},          { 0, 1},
            { 1, -1}, { 1, 0}, { 1, 1}
    };
    private static final int[][] QUEEN_MOVES = {
            {-1, -1}, {-1, 0}, {-1, 1},
            { 0, -1},          { 0, 1},
            { 1, -1}, { 1, 0}, { 1, 1}
    };
    private static final int[][] ROOK_MOVES = {
                    {-1, 0},
            {0, -1},       {0, 1},
                    {1, 0},
    };
    private static final int[][] KNIGHT_MOVES = {{-2, 1}, {-2, -1}, {-1, 2}, {-1, -2}, {1, 2}, {1, -2}, {2, 1}, {2, -1}};
    private static final int[][] BISHOP_MOVES = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

    public static void addPseudoLegalMoves(Board board) {
        for (BoardIndex boardIndex : board.getPiecesIndexes()) {
            BoardPiece currentPiece = board.getBoardPieces()[boardIndex.y()][boardIndex.x()];
            switch(currentPiece.getFortsythEdwardsNotation()) {
            }
        }
    }
}
