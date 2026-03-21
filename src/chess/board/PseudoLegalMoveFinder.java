package chess.board;

import chess.Move;
import chess.board.model.Board;
import chess.BoardPosition;
import chess.board.model.BoardPiece;

import java.util.ArrayList;
import java.util.List;

public class PseudoLegalMoveFinder {
    public static List<Move> getPseudoLegalMoves(Board board) {
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
        return null;
    }

    private static void addLegalMoves(Board board, BoardPosition piecePosition, List<Move> pseudoLegalMoves) {
        BoardPiece currentPiece = board.getBoardPieces()[piecePosition.y()][piecePosition.x()];
        PieceMoveRules moveRules = currentPiece.getMoveRules();
        for (int[] move : moveRules.getDirections()) {
            int moveX = move[0];
            int moveY = move[1];
            if (piecePosition.x() + moveX >= board.SIZE || piecePosition.x() + moveX < 0) {
                continue;
            }

//            pseudoLegalMoves.add(new Move.Builder()
        }


    }
}
