package dev.lelek.chess.search;

import dev.lelek.chess.BoardPosition;
import dev.lelek.chess.Move.Move;

import java.util.List;

public class Utils {
    public static boolean isPositionAttacked(List<Move> moves, BoardPosition... positions) {
        for (Move move : moves) {
            for (BoardPosition position : positions) {
                if (move.to().equals(position)) {
                    return true;
                }
            }
        }
        return false;
    }
}
