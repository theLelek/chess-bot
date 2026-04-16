package chess.board;

import chess.Move;
import chess.board.model.Board;
import chess.BoardPosition;
import chess.board.model.BoardPiece;

import java.util.ArrayList;
import java.util.List;

public class PseudoLegalMoveFinder {
    public static List<Move> getPseudoLegalMoves(Board board, boolean isWhiteToMove) {
        List<Move> pseudoLegalMoves = new ArrayList<>(); // TODO test if LinkedList is faster
        for (BoardPosition boardPosition : board.getPiecesIndexes()) {
            BoardPiece currentPiece = board.getBoardPieces()[boardPosition.y()][boardPosition.x()];
            if (! currentPiece.isWhite() == isWhiteToMove) {
                continue;
            }
            if(currentPiece == BoardPiece.BLACK_PAWN || currentPiece == BoardPiece.WHITE_PAWN) {
                pseudoLegalMoves.addAll(getLegalPawnMoves(board, boardPosition));
                continue;
            }
            pseudoLegalMoves.addAll(getLegalMoves(board, boardPosition));
        }
        return pseudoLegalMoves;
    }

    private static List<Move> getLegalMoves(Board board, BoardPosition position) {
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
                if(board.getBoardPiece(currentPosition) != null){
                    interrupted = true;
                    if(board.getBoardPiece(currentPosition).hasSameColor(currentPiece)){
                        continue;
                    }
                }
                legalMoves.add(new Move(position, currentPosition));
            } while (currentPieceMoveRules.canMoveInfinitely() && !interrupted);
        }
        return legalMoves;
    }

    private static List<Move> getLegalPawnMoves(Board board, BoardPosition position) {
        List<Move> legalMoves = new ArrayList<>();
        int[][] directions = board.getBoardPiece(position).getMoveRules().getDirections();
        BoardPiece currentPiece = board.getBoardPiece(position);

        if(board.getBoardPiece(position) != BoardPiece.WHITE_PAWN && board.getBoardPiece(position) != BoardPiece.BLACK_PAWN) {
            throw new RuntimeException("Pawn function got other piece");
        }
        if(board.getPossibleEnPassant() != null) {
            legalMoves.add(new Move(position, board.getPossibleEnPassant()));
        }

        boolean backrow = false;
        if(board.getBoardPiece(position).isWhite()) {
            backrow = position.y() == 6;
        } else if(board.getBoardPiece(position).isBlack()) {
            backrow = position.y() == 1;
        }

        try {
            BoardPosition currentPosition = position.move(directions[0]);
            if(!currentPiece.hasSameColor(board.getBoardPiece(currentPosition))) {
                legalMoves.add(new  Move(position, currentPosition));

                if(backrow) {
                    currentPosition = currentPosition.move(directions[0]);
                    if(!currentPiece.hasSameColor(board.getBoardPiece(currentPosition))) {
                        legalMoves.add(new Move(position, currentPosition));
                    }
                }
            }
        } catch (IndexOutOfBoundsException _) {}
        for(int i = 1; i <= 2; i++) {
            try{
                BoardPosition currentPosition = position.move(directions[i]);
                if(currentPiece.hasOppositeColor(board.getBoardPiece(currentPosition))){
                    legalMoves.add(new Move(position, currentPosition));
                }
            } catch (IndexOutOfBoundsException _){}
        }

        return legalMoves;
    }
}
