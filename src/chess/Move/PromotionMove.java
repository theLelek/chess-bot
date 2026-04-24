package chess.Move;

import chess.BoardPosition;
import chess.board.model.BoardPiece;

public class PromotionMove extends Move{
    private BoardPiece promotionPiece;
    public PromotionMove(BoardPosition from, BoardPosition to, BoardPiece promotionPiece) {
        super(from, to);
        this.promotionPiece = promotionPiece;
    }

    @Override
    public String toString() {
        return super.toString() + " Promotion to: " + promotionPiece;
    }
}
