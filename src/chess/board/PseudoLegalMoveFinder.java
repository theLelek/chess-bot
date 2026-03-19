package chess.board;

import chess.Move;
import chess.board.model.Board;
import chess.BoardPosition;
import chess.board.model.BoardPiece;

import java.util.ArrayList;
import java.util.List;

public class PseudoLegalMoveFinder {
    // format: y x
    private static final int[][] KING_MOVES = {
            {-1, -1}, {-1, 0}, {-1, 1},
            { 0, -1},          { 0, 1},
            { 1, -1}, { 1, 0}, { 1, 1}
    };
    private static final int[][] QUEEN_MOVES = {
            {-1, -1}, {-1, 0}, {-1, 1},
            { 0, -1},          { 0, 1},
            { 1, -1}, { 1, 0}, { 1, 1}
    };
    private static final int[][] ROOK_MOVES = {
                    {-1, 0},
            {0, -1},       {0, 1},
                    {1, 0},
    };
    private static final int[][] KNIGHT_MOVES = {{-2, 1}, {-2, -1}, {-1, 2}, {-1, -2}, {1, 2}, {1, -2}, {2, 1}, {2, -1}};
    private static final int[][] BISHOP_MOVES = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

    public static void addPseudoLegalMoves(Board board) {
        List<Move> pseudoLegalMoves = new ArrayList<>(); // TODO test if LinkedList is faster
        for (BoardPosition boardPosition : board.getPiecesIndexes()) {
            BoardPiece currentPiece = board.getBoardPieces()[boardPosition.y()][boardPosition.x()];
            switch(currentPiece.getFortsythEdwardsNotation()) {
                case 'R': case 'r':
                    break;
                case 'N': case 'n':
                    break;
                case 'B': case 'b':
                    break;
                case 'Q': case 'q':
                    break;
                case 'K': case 'k':
                    break;
                case 'P': case 'p':
                    break;
            }
        }
    }
}
