package chess.board;

import chess.Move;

import java.util.ArrayList;
import java.util.List;

public enum BoardPiece {
    WHITE_ROOK('R'), WHITE_KNIGHT('N'), WHITE_BISHOP('B'), WHITE_QUEEN('Q'), WHITE_KING('K'), WHITE_PAWN('P'),
    BLACK_ROOK('r'), BLACK_KNIGHT('n'),  BLACK_BISHOP('b'), BLACK_QUEEN('q'), BLACK_KING('k'), BLACK_PAWN('p');

    private final char fortsythEdwardsNotation;
    private boolean isEnPassantPossible;
    private final List<BoardIndex> legalMoves; // TODO test if LinkedList is faster

    BoardPiece(char fen) {
        this.fortsythEdwardsNotation = fen;
        this.isEnPassantPossible = false;
        legalMoves = new ArrayList<>();
    }

    public boolean isWhite() {
        String pieceName = this.toString();
        return pieceName.startsWith("WHITE");
    }

    public boolean isBlack() {
        return ! isWhite();
    }

    public char getFortsythEdwardsNotation() {
        return fortsythEdwardsNotation;
    }

    public static BoardPiece getByFen(char fen) {
        for (BoardPiece piece : BoardPiece.values()) {
            if (piece.fortsythEdwardsNotation == fen) {
                return piece;
            }
        }
        return null;
    }

    public boolean isEnPassantPossible() {
        return isEnPassantPossible;
    }

    public void setEnPassantPossible(boolean enPassantPossible) {
        isEnPassantPossible = enPassantPossible;
    }

    public List<BoardIndex> getLegalMoves() {
        return legalMoves;
    }
}