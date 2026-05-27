package chess.board.model;

public enum PieceBitboard implements BitboardIndexProvider {

    WHITE_PAWNS(0),
    WHITE_KNIGHTS(1),
    WHITE_BISHOPS(2),
    WHITE_ROOKS(3),
    WHITE_QUEEN(4),
    WHITE_KING(5),

    BLACK_PAWNS(6),
    BLACK_KNIGHTS(7),
    BLACK_BISHOPS(8),
    BLACK_ROOKS(9),
    BLACK_QUEEN(10),
    BLACK_KING(11),

    WHITE_PIECES(12),
    BLACK_PIECES(13),
    ALL_PIECES(14);

    private final int bitboardIndex;

    PieceBitboard(int bitboardIndex) {
        this.bitboardIndex = bitboardIndex;
    }

    public static PieceBitboard fromFen(char fen) {
        return switch (fen) {
            case 'P' -> PieceBitboard.WHITE_PAWNS;
            case 'N' -> PieceBitboard.WHITE_KNIGHTS;
            case 'B' -> PieceBitboard.WHITE_BISHOPS;
            case 'R' -> PieceBitboard.WHITE_ROOKS;
            case 'Q' -> PieceBitboard.WHITE_QUEEN;
            case 'K' -> PieceBitboard.WHITE_KING;
            case 'p' -> PieceBitboard.BLACK_PAWNS;
            case 'n' -> PieceBitboard.BLACK_KNIGHTS;
            case 'b' -> PieceBitboard.BLACK_BISHOPS;
            case 'r' -> PieceBitboard.BLACK_ROOKS;
            case 'q' -> PieceBitboard.BLACK_QUEEN;
            case 'k' -> PieceBitboard.BLACK_KING;
            default -> throw new IllegalArgumentException(
                    "Invalid FEN piece: ' + fen");
        };
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