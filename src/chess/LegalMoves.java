package chess;

import java.util.ArrayList;
import java.util.List;

public class LegalMoves {

    private List<Move> legalMoves;
    private List<BoardPosition> legalPromotions;

    public LegalMoves() {
        this.legalMoves = new ArrayList<>();
        this.legalPromotions = new ArrayList<>();
    }

    public List<Move> getLegalMoves() {
        return legalMoves;
    }

    public List<BoardPosition> getLegalPromotions() {
        return legalPromotions;
    }
}
