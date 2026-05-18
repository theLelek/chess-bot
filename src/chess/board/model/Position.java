package chess.board.model;

import chess.BoardPosition;

public class Position {

    private final long[] bitboards = new long[PieceBitboard.values().length];

    public static Position initializeFromFen(String fen) {
        Position position = new Position();
        String[] lines = fen.split("/");
        for (int i = 0; i < lines.length; i++) {
            int column = 0;
            for (int j = 0; j < lines[i].length(); j++) {
                char currentChar =  lines[i].charAt(j);
                BoardPosition currentPosition = new BoardPosition(column, i);
                if (Character.isDigit(currentChar)) {
                    column += Integer.parseInt(String.valueOf(currentChar));
                    continue;
                }
                position.setBit(PieceBitboard.fromFen(currentChar), currentPosition.getBitBoardSquare()); // todo add general bitboards
                column++;
            }
        }
        return position;
    }

    public void setBit(PieceBitboard pieceBitboard, int square) {
        bitboards[pieceBitboard.getIndex()] |= (1L << square);
    }

    public void clearBit(PieceBitboard pieceBitboard, int square) {
        bitboards[pieceBitboard.getIndex()] &= ~(1L << square);
    }

    public boolean getBit(PieceBitboard pieceBitboard, int square) {
        return (bitboards[pieceBitboard.getIndex()] & (1L << square)) != 0;
    }

    public long getBitboard(PieceBitboard pieceBitboard) {
        return bitboards[pieceBitboard.getIndex()];
    }

    public void setBitboard(PieceBitboard pieceBitboard, long value) {
        bitboards[pieceBitboard.getIndex()] = value;
    }
}