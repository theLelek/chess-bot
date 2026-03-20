package chess.board.model;

import chess.BoardPosition;
import chess.board.PieceMoveRules;

import java.util.ArrayList;
import java.util.List;

public enum BoardPiece {
    WHITE_ROOK('R', new PieceMoveRules(

    )),
    WHITE_KNIGHT('N'),
    WHITE_BISHOP('B'),
    WHITE_QUEEN('Q'),
    WHITE_KING('K'),
    WHITE_PAWN('P'),
    BLACK_ROOK('r'),
    BLACK_KNIGHT('n'),
    BLACK_BISHOP('b'),
    BLACK_QUEEN('q'),
    BLACK_KING('k'),
    BLACK_PAWN('p');

    private final char fortsythEdwardsNotation;
    private final PieceMoveRules rules;

    BoardPiece(char fen,  PieceMoveRules rules) {
        this.fortsythEdwardsNotation = fen;
        this.rules = rules;
    }

    public boolean isWhite() {
        String pieceName = this.toString();
        return pieceName.startsWith("WHITE");
    }

    public boolean isBlack() {
        return !isWhite();
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
}