package dev.lelek.chess.board.model;

class NoKingFoundException extends RuntimeException {
    public NoKingFoundException(String message) {
        super(message);
    }
}
