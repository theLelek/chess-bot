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
                char currentChar = lines[i].charAt(j);
                BoardPosition currentPosition = new BoardPosition(column, i);
                if (Character.isDigit(currentChar)) {
                    column += Integer.parseInt(String.valueOf(currentChar));
                    continue;
                }
                position.setBit(PieceBitboard.fromFen(currentChar), currentPosition.getBitBoardSquare());
                position.setBit(PieceBitboard.ALL_PIECES, currentPosition.getBitBoardSquare());
                if (Character.isUpperCase(currentChar)) { // white TODO refactor not sure yet where to put fen color check function bc BoardPiece will probably be removed in the future
                    position.setBit(PieceBitboard.WHITE_PIECES, currentPosition.getBitBoardSquare());
                } else { // black
                    position.setBit(PieceBitboard.BLACK_PIECES, currentPosition.getBitBoardSquare());
                }
                column++;
            }
        }
        return position;
    }

    public void printBitBoard(long bitboard) {
        for (int rank = 7; rank >= 0; rank--) {
            for (int file = 0; file < Board.SIZE; file++) {
                int square = rank * Board.SIZE + file;
                boolean isSet = (bitboard & (1L << square)) != 0;

                System.out.print(isSet ? "1 " : "0 ");
            }
            System.out.println();
        }
        System.out.println();
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

    public long[] getBitboards() {
        return bitboards;
    }
}