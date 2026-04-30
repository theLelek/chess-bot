package chess.board;

import chess.BoardPosition;
import chess.Move.Move;
import chess.board.model.Board;
import chess.board.model.BoardPiece;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LegalMoveFinder {
    public List<Move> getLegalMoves(Board board, boolean isWhiteToMove) {
        List<Move> pseudoLegalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, isWhiteToMove);
        List<Move> legalMoves = new LinkedList<>();
        for(Move move : pseudoLegalMoves){
            board.move(move);
            if(isMovePossible(isWhiteToMove, board)){
                legalMoves.add(move);
            }
            board.unmove(move);
        }
        return legalMoves;
    }

    public boolean isMovePossible(boolean isWhiteToMove, Board board) {
        List<Move> pseudoLegalMovesInAdvancedPosition = PseudoLegalMoveFinder.getPseudoLegalMoves(board, !isWhiteToMove);
        BoardPiece targetedKing = (isWhiteToMove) ? BoardPiece.WHITE_KING : BoardPiece.BLACK_KING;
        for(Move move : pseudoLegalMovesInAdvancedPosition){

            if(board.getBoardPiece(move.to()) == targetedKing){
                return false;
            }
        }
        return true;
    }


}
