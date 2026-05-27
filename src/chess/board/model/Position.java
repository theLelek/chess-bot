package chess.board.model;

import chess.BoardPosition;

public class Position {

    private final long[] bitboards = new long[BoardPiece.values().length + PieceBitboard.values().length];

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
                position.setBit(BoardPiece.fromFen(currentChar), currentPosition);
                position.setBit(PieceBitboard.ALL_PIECES, currentPosition);
                if (Character.isUpperCase(currentChar)) { // white TODO refactor not sure yet where to put fen color check function bc BoardPiece will probably be removed in the future
                    position.setBit(PieceBitboard.WHITE_PIECES, currentPosition);
                } else { // black
                    position.setBit(PieceBitboard.BLACK_PIECES, currentPosition);
                }
                column++;
            }
        }
        return position;
    }

    public static void printBitBoard(long bitboard) {
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

    public void setBit(BitboardIndexProvider bitboardIndexProvider, int square) {
        bitboards[bitboardIndexProvider.getBitboardIndex()] |= (1L << square);
    }

    public void setBit(BitboardIndexProvider bitboardIndexProvider, BoardPosition boardPosition) {
        setBit(bitboardIndexProvider, boardPosition.getBitBoardSquare());
    }

    public void setBit(BoardPosition boardPosition) {
        setBit(PieceBitboard.ALL_PIECES, boardPosition);
    }

    public void setBit(int square) {
        setBit(PieceBitboard.ALL_PIECES, square);
    }

    public void clearBit(BitboardIndexProvider bitboardIndexProvider, int square) {
        bitboards[bitboardIndexProvider.getBitboardIndex()] &= ~(1L << square);
    }

    public void clearBit(BitboardIndexProvider bitboardIndexProvider, BoardPosition boardPosition) {
        clearBit(bitboardIndexProvider, boardPosition.getBitBoardSquare());
    }

    public void clearBit(BoardPosition boardPosition) {
        clearBit(PieceBitboard.ALL_PIECES, boardPosition);
    }

    public void clearBit(int square) {
        clearBit(PieceBitboard.ALL_PIECES, square);
    }

    public boolean getBit(BitboardIndexProvider bitboardIndexProvider, int square) {
        return (bitboards[bitboardIndexProvider.getBitboardIndex()] & (1L << square)) != 0;
    }

    public boolean getBit(BitboardIndexProvider bitboardIndexProvider, BoardPosition boardPosition) {
        return getBit(bitboardIndexProvider, boardPosition.getBitBoardSquare());
    }

    public boolean getBit(BoardPosition boardPosition) {
        return getBit(PieceBitboard.ALL_PIECES, boardPosition);
    }

    public boolean getBit(int square) {
        return getBit(PieceBitboard.ALL_PIECES, square);
    }

    public long getBitboard(BitboardIndexProvider bitboardIndexProvider) {
        return bitboards[bitboardIndexProvider.getBitboardIndex()];
    }

    public long getBitboard(int index) {
        return bitboards[index];
    }

    public void setBitboard(BitboardIndexProvider bitboardIndexProvider, long value) {
        bitboards[bitboardIndexProvider.getBitboardIndex()] = value;
    }

    // todo maybe add toggleBit helper

    public long[] getBitboards() {
        return bitboards;
    }
}