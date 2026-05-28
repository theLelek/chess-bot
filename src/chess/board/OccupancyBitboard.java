package chess.board;

import chess.Color;

public enum OccupancyBitboard implements BitboardIndexProvider {

    WHITE_PIECES(12),
    BLACK_PIECES(13),
    ALL_PIECES(14);

    private final int bitboardIndex;

    OccupancyBitboard(int bitboardIndex) {
        this.bitboardIndex = bitboardIndex;
    }

    public static OccupancyBitboard fromColor(Color color) {
        return (color == Color.WHITE) ? WHITE_PIECES : BLACK_PIECES;
    }

    public static OccupancyBitboard fromBitboardIndex(int index) {
        for (OccupancyBitboard occupancyBitboard : OccupancyBitboard.values()) {
            if (occupancyBitboard.getBitboardIndex() == index) {
                return occupancyBitboard;
            }
        }
        throw new IllegalArgumentException("Invalid bitboard index: " + index);
    }

    public int getBitboardIndex() {
        return bitboardIndex;
    }
}