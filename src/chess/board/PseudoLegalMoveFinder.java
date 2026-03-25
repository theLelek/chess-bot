package chess.board;

import chess.Move;
import chess.board.model.Board;
import chess.BoardPosition;
import chess.board.model.BoardPiece;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PseudoLegalMoveFinder {
    public static List<Move> getPseudoLegalMoves(Board board) {
        List<Move> pseudoLegalMoves = new ArrayList<>(); // TODO test if LinkedList is faster
        for (BoardPosition boardPosition : board.getPiecesIndexes()) {
            BoardPiece currentPiece = board.getBoardPieces()[boardPosition.y()][boardPosition.x()];

            if(currentPiece == BoardPiece.BLACK_PAWN || currentPiece == BoardPiece.WHITE_PAWN) {
                pseudoLegalMoves.addAll(getLegalPawnMoves(board, boardPosition));
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

    static List<Move> getLegalPawnMoves(Board board, BoardPosition position) {
        List<Move> legalMoves = new ArrayList<>();
        int[][] directions = board.get(position).getMoveRules().getDirections();
        BoardPiece currentPiece = board.get(position);

        if(board.get(position) != BoardPiece.WHITE_PAWN && board.get(position) != BoardPiece.BLACK_PAWN) {
            throw new RuntimeException("Pawn function got other piece");
        }
        if(board.getPossibleEnPassants() != null) {
            legalMoves.add(new Move(position, board.getPossibleEnPassants()));
        }

        boolean backrow = false;
        if(board.get(position).isWhite()) {
            backrow = position.y() == 6;
        } else if(board.get(position).isBlack()) {
            backrow = position.y() == 1;
        }

        try {
            BoardPosition currentPosition = position.move(directions[0]);
            if(!currentPiece.hasSameColor(board.get(currentPosition))) {
                legalMoves.add(new  Move(position, currentPosition));

                if(backrow) {
                    currentPosition = currentPosition.move(directions[0]);
                    if(!currentPiece.hasSameColor(board.get(currentPosition))) {
                        legalMoves.add(new Move(position, currentPosition));
                    }
                }
            }
        } catch (IndexOutOfBoundsException _) {}




        for(int i = 1; i <= 2; i++) {
            try{
                BoardPosition currentPosition = position.move(directions[i]);
                if(currentPiece.hasOppositeColor(board.get(currentPosition))){
                    legalMoves.add(new Move(position, currentPosition));
                }
            } catch (IndexOutOfBoundsException _){}
        }

        return legalMoves;
    }
}
