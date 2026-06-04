package dev.lelek.chess.board.model;

import dev.lelek.chess.BoardPosition;
import dev.lelek.chess.board.BitboardIndexProvider;
import dev.lelek.chess.board.BoardPiece;
import dev.lelek.chess.board.OccupancyBitboard;

import java.util.Arrays;
import java.util.Objects;

public class BitBoardState {

    private final long[] bitboards = new long[BoardPiece.values().length + OccupancyBitboard.values().length];

    public static BitBoardState initializeFromPieceList(BoardPiece[] pieceList) {
        BitBoardState bitBoardState = new BitBoardState();
        for (int i = 0; i < pieceList.length; i++) {
            BoardPiece piece = pieceList[i];
            if (piece == null) {
                continue;
            }
            BoardPosition square = new BoardPosition(i);
            bitBoardState.setBit(piece, square);
            bitBoardState.setBit(OccupancyBitboard.ALL_PIECES, square);
            if (piece.isWhite()) {
                bitBoardState.setBit(OccupancyBitboard.WHITE_PIECES, square);
            } else {
                bitBoardState.setBit(OccupancyBitboard.BLACK_PIECES, square);
            }
        }
        return bitBoardState;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BitBoardState that = (BitBoardState) o;
        return Objects.deepEquals(bitboards, that.bitboards);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bitboards);
    }

    public void setBit(BitboardIndexProvider bitboardIndexProvider, int square) {
        bitboards[bitboardIndexProvider.getBitboardIndex()] |= (1L << square);
    }

    public void setBit(BitboardIndexProvider bitboardIndexProvider, BoardPosition boardPosition) {
        setBit(bitboardIndexProvider, boardPosition.getBitBoardSquare());
    }

    public void clearBit(BitboardIndexProvider bitboardIndexProvider, int square) {
        bitboards[bitboardIndexProvider.getBitboardIndex()] &= ~(1L << square);
    }

    public void clearBit(BitboardIndexProvider bitboardIndexProvider, BoardPosition boardPosition) {
        clearBit(bitboardIndexProvider, boardPosition.getBitBoardSquare());
    }

    public boolean getBit(BitboardIndexProvider bitboardIndexProvider, int square) {
        return (bitboards[bitboardIndexProvider.getBitboardIndex()] & (1L << square)) != 0;
    }

    public boolean getBit(BitboardIndexProvider bitboardIndexProvider, BoardPosition boardPosition) {
        return getBit(bitboardIndexProvider, boardPosition.getBitBoardSquare());
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