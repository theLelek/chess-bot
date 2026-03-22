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


        }
        return null;
    }

    private static void addLegalMoves(Board board, BoardPosition currentPosition, List<Move> pseudoLegalMoves) {
        BoardPiece currentPiece = board.getBoardPieces()[currentPosition.y()][currentPosition.x()];
        PieceMoveRules currentPieceMoveRules = currentPiece.getMoveRules();
        boolean[] isBlocked = new boolean[currentPieceMoveRules.getDirections().length];

        int currentX = currentPosition.x();
        int currentY = currentPosition.y();
        do {
            for (int i = 0; i < currentPieceMoveRules.getDirections().length; i++) {
                int[] move = currentPieceMoveRules.getDirections()[i];
                currentX += move[0];
                currentY += move[1];

                if (currentX >= Board.SIZE || currentX < 0 || currentY >= Board.SIZE || currentY < 0) {
                    continue;
                }
                pseudoLegalMoves.add(new Move.Builder()
                        .fromX(currentPosition.x())
                        .fromY(currentPosition.y())
                        .toX(currentX)
                        .toY(currentY).build());
            }
        } while (currentPieceMoveRules.canMoveInfinitely());
    }
}
