package dev.lelek.chess.Move;

import dev.lelek.chess.BoardPosition;
import dev.lelek.chess.board.BoardPiece;

import java.util.Objects;

public class PromotionMove extends Move {

    private final BoardPiece promotionPiece;

    public PromotionMove(BoardPosition from, BoardPosition to, BoardPiece promotionPiece) {
        super(from, to);
        Objects.requireNonNull(promotionPiece);
        this.promotionPiece = promotionPiece;
    }

    public PromotionMove(int fromX, int fromY, int toX, int toY, BoardPiece promotionPiece) {
        super(new BoardPosition(fromX, fromY), new BoardPosition(toX, toY));
        Objects.requireNonNull(promotionPiece);
        this.promotionPiece = promotionPiece;
    }

    public PromotionMove(String from, String to, BoardPiece promotionPiece) {
        super(from, to);
        Objects.requireNonNull(promotionPiece);
        this.promotionPiece = promotionPiece;
    }

    public BoardPiece getPromotionPiece() {
        return promotionPiece;
    }

    @Override
    public String toString() {
        return super.toString() + " Promotion to: " + promotionPiece;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PromotionMove that = (PromotionMove) o;
        return promotionPiece == that.promotionPiece;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), promotionPiece);
    }
}
