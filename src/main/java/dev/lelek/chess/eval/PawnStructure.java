package dev.lelek.chess.eval;

import dev.lelek.chess.BoardPosition;
import dev.lelek.chess.board.model.BitBoardState;
import dev.lelek.chess.board.model.Board;

class PawnStructure {

    private static final long[] passedPawnMask = computePassedPawnMask();

    static int getPassedPawnValue(Board board, BoardPosition position) {



    }


    public static void main(String[] args) {
        BitBoardState.printBitBoard(passedPawnMask[new BoardPosition("e4").getBitBoardSquare()]);

    }

    private static long[] computePassedPawnMask() {
        long[] masks = new long[64];

        for (int square = 0; square < 64; square++) {
            long mask = 0L;

            int file = square & 7;
            int rank = square >> 3;

            // Squares ahead of a WHITE pawn
            for (int r = rank + 1; r < 8; r++) {

                // same file
                mask |= 1L << (r * 8 + file);

                // left diagonal file
                if (file > 0) {
                    mask |= 1L << (r * 8 + (file - 1));
                }

                // right diagonal file
                if (file < 7) {
                    mask |= 1L << (r * 8 + (file + 1));
                }
            }

            masks[square] = mask;
        }

        return masks;
    }
}