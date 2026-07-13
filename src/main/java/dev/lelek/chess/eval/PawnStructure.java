package dev.lelek.chess.eval;

import dev.lelek.chess.BoardPiece;
import dev.lelek.chess.BoardPosition;
import dev.lelek.chess.Color;
import dev.lelek.chess.board.model.Board;

class PawnStructure { // todo add hashing pawns: https://www.chessprogramming.org/Pawn_Hash_Table

    private static final int passedPawnValue = 50;
    private static final long[][] passedPawnMask = {computePassedPawnMask(Color.WHITE), computePassedPawnMask(Color.BLACK)};

    static int getPassedPawnValue(Board board, BoardPosition position) {
        BoardPiece piece = board.getPieceAt(position);
        Color color = piece.getColor();

        long enemyPawns = board.getBitBoardState().getBitboard(color.getOpponentPawn());
        long mask = color == Color.WHITE
                ? passedPawnMask[0][position.getBitBoardSquare()]
                : passedPawnMask[1][position.getBitBoardSquare()];

        boolean isPassed = (mask & enemyPawns) == 0;
        return isPassed ? passedPawnValue : 0;
    }

    private static long[] computePassedPawnMask(Color color) {
        long[] masks = new long[64];

        for (int square = 0; square < 64; square++) {
            long mask = 0L;

            int file = square & 7;
            int rank = square >> 3;

            if (color == Color.WHITE) {
                // white
                for (int r = rank + 1; r < 8; r++) {

                    if (file > 0) {
                        mask |= 1L << (r * 8 + file - 1);
                    }

                    mask |= 1L << (r * 8 + file);

                    if (file < 7) {
                        mask |= 1L << (r * 8 + file + 1);
                    }
                }
            } else {
                // black
                for (int r = rank - 1; r >= 0; r--) {

                    if (file > 0) {
                        mask |= 1L << (r * 8 + file - 1);
                    }

                    mask |= 1L << (r * 8 + file);

                    if (file < 7) {
                        mask |= 1L << (r * 8 + file + 1);
                    }
                }
            }

            masks[square] = mask;
        }

        return masks;
    }
}