package dev.lelek.chess.board.model;

public class NoKingFoundException extends RuntimeException {
    public NoKingFoundException(String message) {
        super(message);
    }
}
