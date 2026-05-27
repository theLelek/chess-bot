package chess.board.model;

import chess.BoardPosition;
import chess.board.BitboardIndexProvider;
import chess.board.BoardPiece;
import chess.board.OccupancyBitboard;

import java.util.ArrayList;

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

    public void setBit(BitboardIndexProvider bitboardIndexProvider, int square) {
        bitboards[bitboardIndexProvider.getBitboardIndex()] |= (1L << square);
    }

    public void setBit(BitboardIndexProvider bitboardIndexProvider, BoardPosition boardPosition) {
        setBit(bitboardIndexProvider, boardPosition.getBitBoardSquare());
    }

    public void setBit(BoardPosition boardPosition) {
        setBit(OccupancyBitboard.ALL_PIECES, boardPosition);
    }

    public void setBit(int square) {
        setBit(OccupancyBitboard.ALL_PIECES, square);
    }

    public void clearBit(BitboardIndexProvider bitboardIndexProvider, int square) {
        bitboards[bitboardIndexProvider.getBitboardIndex()] &= ~(1L << square);
    }

    public void clearBit(BitboardIndexProvider bitboardIndexProvider, BoardPosition boardPosition) {
        clearBit(bitboardIndexProvider, boardPosition.getBitBoardSquare());
    }

    public void clearBit(BoardPosition boardPosition) {
        clearBit(OccupancyBitboard.ALL_PIECES, boardPosition);
    }

    public void clearBit(int square) {
        clearBit(OccupancyBitboard.ALL_PIECES, square);
    }

    public boolean getBit(BitboardIndexProvider bitboardIndexProvider, int square) {
        return (bitboards[bitboardIndexProvider.getBitboardIndex()] & (1L << square)) != 0;
    }

    public boolean getBit(BitboardIndexProvider bitboardIndexProvider, BoardPosition boardPosition) {
        return getBit(bitboardIndexProvider, boardPosition.getBitBoardSquare());
    }

    public boolean getBit(BoardPosition boardPosition) {
        return getBit(OccupancyBitboard.ALL_PIECES, boardPosition);
    }

    public boolean getBit(int square) {
        return getBit(OccupancyBitboard.ALL_PIECES, square);
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