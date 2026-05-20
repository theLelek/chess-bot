package chess.board.model;

import chess.board.PieceMoveRules;

public enum BoardPiece {
    WHITE_ROOK('R', PieceMoveRules.ROOK),
    WHITE_KNIGHT('N', PieceMoveRules.KNIGHT),
    WHITE_BISHOP('B',  PieceMoveRules.BISHOP),
    WHITE_QUEEN('Q', PieceMoveRules.QUEEN),
    WHITE_KING('K',  PieceMoveRules.KING),
    WHITE_PAWN('P', PieceMoveRules.WHITE_PAWN),

    BLACK_ROOK('r', PieceMoveRules.ROOK),
    BLACK_KNIGHT('n', PieceMoveRules.KNIGHT),
    BLACK_BISHOP('b', PieceMoveRules.BISHOP),
    BLACK_QUEEN('q', PieceMoveRules.QUEEN),
    BLACK_KING('k', PieceMoveRules.KING),
    BLACK_PAWN('p', PieceMoveRules.BLACK_PAWN);

    private final char fen;
    private final PieceMoveRules moveRules;

    BoardPiece(char fen, PieceMoveRules moveRules) {
        this.fen = fen;
        this.moveRules = moveRules;
    }

    public static BoardPiece fromFen(char fen) {
        for (BoardPiece piece : BoardPiece.values()) {
            if (piece.fen == fen) {
                return piece;
            }
        }
        return null;
    }

    public static BoardPiece fromPieceBitBoard(PieceBitboard pieceBitboard) {
        return switch (pieceBitboard) {
            case WHITE_PAWNS -> BoardPiece.WHITE_PAWN;
            case WHITE_KNIGHTS -> BoardPiece.WHITE_KNIGHT;
            case WHITE_BISHOPS -> BoardPiece.WHITE_BISHOP;
            case WHITE_ROOKS -> BoardPiece.WHITE_ROOK;
            case WHITE_QUEEN -> BoardPiece.WHITE_QUEEN;
            case WHITE_KING -> BoardPiece.WHITE_KING;

            case BLACK_PAWNS -> BoardPiece.BLACK_PAWN;
            case BLACK_KNIGHTS -> BoardPiece.BLACK_KNIGHT;
            case BLACK_BISHOPS -> BoardPiece.BLACK_BISHOP;
            case BLACK_ROOKS -> BoardPiece.BLACK_ROOK;
            case BLACK_QUEEN -> BoardPiece.BLACK_QUEEN;
            case BLACK_KING -> BoardPiece.BLACK_KING;
            default -> throw new IllegalStateException("Unexpected value: " + pieceBitboard);
        };
    }

    public boolean hasSameColor(BoardPiece currentPiece) {
        if (currentPiece == null) {
            throw new IllegalArgumentException();
        }
        return this.isWhite() == currentPiece.isWhite();

    }

    public boolean hasOppositeColor(BoardPiece currentPiece) {
        if(currentPiece == null) {
            return false;
        }
        return this.isBlack() == currentPiece.isWhite();
    }

    public boolean isWhite() {
        String pieceName = this.toString();
        return pieceName.startsWith("WHITE");
    }

    public boolean isBlack() {
        return !isWhite();
    }

    public char getFen() {
        return fen;
    }

    public PieceMoveRules getMoveRules() {
        return moveRules;
    }

    public boolean isRook() {
        return fen == 'r' || fen == 'R';
    }

    public boolean isKnight() {
        return fen == 'n' || fen == 'N';
    }

    public boolean isBishop() {
        return fen == 'b' || fen == 'B';
    }

    public boolean isQueen() {
        return fen == 'q' || fen == 'Q';
    }

    public boolean isKing() {
        return fen == 'k' || fen == 'K';
    }

    public boolean isPawn() {
        return fen == 'p' || fen == 'P';
    }
}