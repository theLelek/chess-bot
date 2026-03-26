package chess.board.model;

import chess.BoardPosition;
import chess.board.PseudoLegalMoveFinder;

import java.util.*;

public class Board {

    public static final int SIZE = 8;
    private final BoardPiece[][] boardPieces;
    private final List<BoardPosition> piecesIndexes; // TODO O(n) for removing elements find faster way
    private final boolean isWhiteToMove;
    private final CastlingRights castlingRights;
    private final BoardPosition possibleEnPassants;
    private int halfMoveClock;
    private int fullMoveNumber;
    // TODO enPassantTarget is not finished yet, will be in each BoardPiece

    public Board(BoardPiece[][] boardPieces, List<BoardPosition> piecesIndexes, boolean isWhiteToMove, CastlingRights castlingRights, BoardPosition possibleEnPassants, int halfMoveClock, int fullMoveNumber) {
        this.boardPieces = boardPieces;
        this.piecesIndexes = piecesIndexes;
        this.isWhiteToMove = isWhiteToMove;
        this.castlingRights = castlingRights;
        this.possibleEnPassants = possibleEnPassants;
        this.halfMoveClock = halfMoveClock;
        this.fullMoveNumber = fullMoveNumber;
    }

    public static Board initializeDefaultBoard() {
        return initializeFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    public static Board initializeFromFen(String fen) {
        String[] fenParts = fen.split(" ");
        var boardPieces = initializeBoardPiecesFromFen(fenParts[0]);
        var piecesIndexes = initializePiecesIndexes(boardPieces);
        var isWhiteToMove = isWhiteToMoveFromFen(fenParts[1]);
        var castlingRights = CastlingRights.initializeFromFen(fenParts[2]);
        var enPassantTarget = BoardPosition.getFromString(fenParts[3]);
        var halfMoveClock = Integer.parseInt(fenParts[4]);
        var fullMoveNumber = Integer.parseInt(fenParts[5]);
        Board board = new Board(boardPieces, piecesIndexes, isWhiteToMove, castlingRights, enPassantTarget, halfMoveClock, fullMoveNumber);
//        PseudoLegalMoveFinder.addPseudoLegalMoves(board);
        return board;
    }

     private static BoardPiece[][] initializeBoardPiecesFromFen(String piecePlacements) {
        BoardPiece[][] boardPieces = new BoardPiece[SIZE][SIZE];
        String[] lines = piecePlacements.split("/");
        for (int i = lines.length -1; i >= 0; i--) {
            int boardCounter = 0;
            for (int j = 0; j < lines[i].length(); j++) {
                char currentChar =  lines[i].charAt(j);
                if (Character.isDigit(currentChar)) {
                    boardCounter += Integer.parseInt(String.valueOf(currentChar));
                    continue;
                }
                boardPieces[i][boardCounter] = BoardPiece.getByFen(currentChar);
                boardCounter++;
            }
        }
        return boardPieces;
    }

    private static List<BoardPosition> initializePiecesIndexes(BoardPiece[][] boardPieces) {
        List<BoardPosition> piecesIndexes = new ArrayList<>();
        for (int i = 0; i < boardPieces.length; i++) {
            for (int j = 0; j < boardPieces[i].length; j++) {
                if (boardPieces[i][j] != null) {
                    BoardPosition boardPosition = new BoardPosition.Builder()
                            .y(i)
                            .x(j).build();
                    piecesIndexes.add(boardPosition);
                }
            }
        }
        return piecesIndexes;
    }

    private static boolean isWhiteToMoveFromFen(String fen) {
        return fen.equals("w");
    }

    public BoardPiece[][] getBoardPieces() {
        return boardPieces;
    }

    public boolean isWhiteToMove() {
        return isWhiteToMove;
    }

    public boolean isBlackToMove(){
        return !isWhiteToMove;
    }

    public CastlingRights getCastlingRights() {
        return castlingRights;
    }

    public int getHalfMoveClock() {
        return halfMoveClock;
    }

    public void setHalfMoveClock(int halfMoveClock) {
        this.halfMoveClock = halfMoveClock;
    }

    public int getFullMoveNumber() {
        return fullMoveNumber;
    }

    public void setFullMoveNumber(int fullMoveNumber) {
        this.fullMoveNumber = fullMoveNumber;
    }

    public BoardPosition getPossibleEnPassants() {
        return possibleEnPassants;
    }

    public List<BoardPosition> getPiecesIndexes() {
        return piecesIndexes;
    }

    public BoardPiece get(BoardPosition currentPosition) {
        return this.boardPieces[currentPosition.y()][currentPosition.x()];
    }
}