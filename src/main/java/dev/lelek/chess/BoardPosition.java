package dev.lelek.chess;

import dev.lelek.chess.board.model.Board;

public class BoardPosition {

    private final byte bitBoardSquare;

    public BoardPosition(int x, int y) {
        if (x < 0 || x >= Board.SIZE || y < 0 || y >= Board.SIZE) {
            throw new IllegalArgumentException("Position out of bounds");
        }
        this.bitBoardSquare = (byte) ((7 - y) * 8 + x);
    }

    public BoardPosition(String str) {
        if (str == null) {
            throw new NullPointerException();
        }

        str = str.toLowerCase();
        if (str.length() != 2) {
            throw new IllegalArgumentException();
        }

        int x = str.charAt(0) - 'a';
        int y = Board.SIZE - (str.charAt(1) - '0');

        if (x < 0 || x >= Board.SIZE || y < 0 || y >= Board.SIZE) {
            throw new IllegalArgumentException("Position out of bounds");
        }

        this.bitBoardSquare = (byte) ((7 - y) * 8 + x);
    }

    public BoardPosition(int bitBoardSquare) {
        if (bitBoardSquare < 0 || bitBoardSquare >= 64) {
            throw new IllegalArgumentException("Position out of bounds");
        }
        this.bitBoardSquare = (byte) bitBoardSquare;
    }

    public BoardPosition copy() {
        return new BoardPosition(bitBoardSquare);
    }

    public static BoardPosition copyOf(BoardPosition other) {
        if (other == null) {
            throw new IllegalArgumentException("boardPosition cannot be null");
        }
        return new BoardPosition(other.bitBoardSquare);
    }

    public BoardPosition move(int[] direction) {
        int x = getX() + direction[0];
        int y = getY() + direction[1];

        if (x < 0 || y < 0 || x >= Board.SIZE || y >= Board.SIZE) {
            throw new IndexOutOfBoundsException("Position out of bounds");
        }

        return new BoardPosition(x, y);
    }

    public String toFen() {
        return "" + (char) ('a' + getX()) + (Board.SIZE - getY());
    }

    public int getBitBoardSquare() {
        return bitBoardSquare;
    }

    public int getX() {
        return bitBoardSquare & 7;
    }

    public int getY() {
        return 7 - (bitBoardSquare >>> 3);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardPosition that)) return false;
        return bitBoardSquare == that.bitBoardSquare;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(bitBoardSquare);
    }

    @Override
    public String toString() {
        return toFen();
    }
}