package chess.board.model;

public enum PieceBitboard implements BitboardIndexProvider {

    WHITE_PIECES(12),
    BLACK_PIECES(13),
    ALL_PIECES(14);

    private final int bitboardIndex;

    PieceBitboard(int bitboardIndex) {
        this.bitboardIndex = bitboardIndex;
    }

    public static PieceBitboard fromIndex(int index) {
        for (PieceBitboard pieceBitboard : PieceBitboard.values()) {
            if (pieceBitboard.getBitboardIndex() == index) {
                return pieceBitboard;
            }
        }
        return null;
    }

    public int getBitboardIndex() {
        return bitboardIndex;
    }
}