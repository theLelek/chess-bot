package chess.board.model;

public enum PieceBitboard {

    WHITE_PAWNS(0),
    WHITE_KNIGHTS(1),
    WHITE_BISHOPS(2),
    WHITE_ROOKS(3),
    WHITE_QUEENS(4),
    WHITE_KING(5),

    BLACK_PAWNS(6),
    BLACK_KNIGHTS(7),
    BLACK_BISHOPS(8),
    BLACK_ROOKS(9),
    BLACK_QUEENS(10),
    BLACK_KING(11),

    WHITE_PIECES(12),
    BLACK_PIECES(13),
    ALL_PIECES(14);


    private final int index;

    PieceBitboard(int index) {
        this.index = index;
    }

    public static PieceBitboard fromFen(char fen) {
        switch (fen) {
            case 'P':
                return PieceBitboard.WHITE_PAWNS;
            case 'N':
                return PieceBitboard.WHITE_KNIGHTS;
            case 'B':
                return PieceBitboard.WHITE_BISHOPS;
            case 'R':
                return PieceBitboard.WHITE_ROOKS;
            case 'Q':
                return PieceBitboard.WHITE_QUEENS;
            case 'K':
                return PieceBitboard.WHITE_KING;

            case 'p':
                return PieceBitboard.BLACK_PAWNS;
            case 'n':
                return PieceBitboard.BLACK_KNIGHTS;
            case 'b':
                return PieceBitboard.BLACK_BISHOPS;
            case 'r':
                return PieceBitboard.BLACK_ROOKS;
            case 'q':
                return PieceBitboard.BLACK_QUEENS;
            case 'k':
                return PieceBitboard.BLACK_KING;

            default:
                throw new IllegalArgumentException(
                        "Invalid FEN piece: ' + fen");
        }
    }

    public int getIndex() {
        return index;
    }
}