package chess.board.model;

public enum PieceColor {
    WHITE, BLACK;

    public static PieceColor initializeFromFen(String whiteToMove) {
        if (whiteToMove.equals("w")) {
            return PieceColor.WHITE;
        } else if (whiteToMove.equals("b")) {
            return PieceColor.BLACK;
        }
        return null;
    }
}
