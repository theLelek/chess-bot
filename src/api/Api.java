package api;

import chess.Move;

public class Api {

    public static Move initializeMove(String input) {
        // format: "fromX fromY"
        String[] moveParts = input.split(" ");
        int fromX = Integer.parseInt(moveParts[0]);
        int fromY = Integer.parseInt(moveParts[1]);
        int toX = Integer.parseInt(moveParts[2]);
        int toY = Integer.parseInt(moveParts[3]);
        return new Move(fromX, fromY, toX, toY);
    }
}
