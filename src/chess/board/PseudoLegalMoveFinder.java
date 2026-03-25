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

            if(currentPiece == BoardPiece.BLACK_PAWN || currentPiece == BoardPiece.WHITE_PAWN) {
                System.out.println("TODO, not created yet");
                continue;
            }

            PieceMoveRules moveRules = currentPiece.getMoveRules();

            pseudoLegalMoves.addAll(getLegalMoves(board, boardPosition));
        }
        return null;
    }

    public static List<Move> getLegalMoves(Board board, BoardPosition position) {
        List<Move> legalMoves = new ArrayList<>();
        BoardPiece currentPiece = board.getBoardPieces()[position.y()][position.x()];
        PieceMoveRules currentPieceMoveRules = currentPiece.getMoveRules();

        for (int[] direction : currentPieceMoveRules.getDirections()) {
            BoardPosition currentPosition = position.copy();
            boolean interrupted = false;
            do{
                try {
                    currentPosition = currentPosition.move(direction);
                } catch (IndexOutOfBoundsException e){
                    interrupted = true;
                    continue;
                }
                if(board.get(currentPosition) != null){
                    interrupted = true;
                    if(board.get(currentPosition).hasSameColor(currentPiece)){
                        continue;
                    }
                }
                legalMoves.add(new Move(position, currentPosition));
            } while (currentPieceMoveRules.canMoveInfinitely() && !interrupted);
        }
        return legalMoves;
    }

    private List<Move> getLegalPawnMoves(Board board, BoardPosition position) {
        List<Move> legalMoves = new ArrayList<>();
        if(board.get(position) == BoardPiece.WHITE_PAWN || board.get(position) == BoardPiece.BLACK_PAWN) {
            throw new RuntimeException("Pawn function got other piece");
        }

        return legalMoves;
    }
}
