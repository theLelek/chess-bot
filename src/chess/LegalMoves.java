package chess;

import java.util.List;

public class LegalMoves {

    private List<Move> legalMoves;
    private List<BoardPosition> legalPromotions;

    public LegalMoves(List<Move> legalMoves, List<BoardPosition> legalPromotions) {
        this.legalMoves = legalMoves;
        this.legalPromotions = legalPromotions;
    }

    public List<Move> getLegalMoves() {
        return legalMoves;
    }

    public List<BoardPosition> getLegalPromotions() {
        return legalPromotions;
    }
}
