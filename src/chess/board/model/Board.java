package chess.board.model;

import chess.BoardPosition;
import chess.Move.Move;
import chess.Move.PromotionMove;

import java.util.*;

public class Board {

    public static final int SIZE = 8;

    private final BoardPiece[][] boardPieces;
    private final List<BoardPosition> piecesIndexes; // TODO O(n) for removing elements find faster way
    private boolean isWhiteToMove;
    private final CastlingRights castlingRightsWhite;
    private final CastlingRights castlingRightsBlack;
    private BoardPosition possibleEnPassant; // TODO is currently stored 2 times
    private int halfmoveClock;
    private int fullmoveNumber;

    public Board(BoardPiece[][] boardPieces, List<BoardPosition> piecesIndexes, boolean isWhiteToMove, CastlingRights castlingRightsWhite, CastlingRights castlingRightsBlack, BoardPosition possibleEnPassant, int halfmoveClock, int fullmoveNumber) {
        this.boardPieces = boardPieces;
        this.piecesIndexes = piecesIndexes;
        this.isWhiteToMove = isWhiteToMove;
        this.castlingRightsWhite = castlingRightsWhite;
        this.castlingRightsBlack = castlingRightsBlack;
        this.possibleEnPassant = possibleEnPassant;
        this.halfmoveClock = halfmoveClock;
        this.fullmoveNumber = fullmoveNumber;
    }

    public void move(Move move) {
        BoardPiece pieceToMove = getBoardPiece(move.from());
        BoardPiece pieceToCapture = getBoardPiece(move.to());
        int startRow = (isWhiteToMove) ? Board.SIZE - 1 : 0;

        updateCastlingRights(move);
        halfmoveClock = (! pieceToMove.isPawn() && pieceToCapture == null) ? halfmoveClock + 1 : 0;
        if (isBlackToMove()) fullmoveNumber++;
        updateBoardPieces(move);
        updatePiecesIndexes(move);
        possibleEnPassant = null;
        updatePossibleEnPassant(move);
        isWhiteToMove = ! isWhiteToMove;
    }

    private void updateCastlingRights(Move move) {
        BoardPiece pieceToMove = getBoardPiece(move.from());
        int startRow = (isWhiteToMove) ? Board.SIZE - 1 : 0;
        int endRow = (isWhiteToMove) ? 0 : Board.SIZE - 1;
        CastlingRights castlingRights = (isWhiteToMove) ? castlingRightsWhite : castlingRightsBlack;
        CastlingRights castlingRightsOpponent = (isWhiteToMove) ? castlingRightsBlack : castlingRightsWhite;

        if (pieceToMove.isKing()) {
            castlingRights.setCanCastleKingSide(false);
            castlingRights.setCanCastleQueenSide(false);
        } else if (move.from().equals(new BoardPosition(0, startRow))) {
            castlingRights.setCanCastleQueenSide(false);
        } else if (move.from().equals(new BoardPosition(Board.SIZE - 1, startRow))) {
            castlingRights.setCanCastleKingSide(false);
        }

        if (move.to().equals(new BoardPosition(0, endRow))) {
            castlingRightsOpponent.setCanCastleQueenSide(false);
        } else if (move.to().equals(new BoardPosition(Board.SIZE - 1, endRow))) {
            castlingRightsOpponent.setCanCastleKingSide(false);
        }
    }

    private void updatePossibleEnPassant(Move move) {
        BoardPiece pieceToMove = getBoardPiece(move.to());
        int pawnStartingRow = (isWhiteToMove) ? Board.SIZE - 2 : 1;
        int pawnDoublePushRow = (isWhiteToMove) ? Board.SIZE - 4 : 3;
        possibleEnPassant = (pieceToMove.isPawn() && move.from().y() == pawnStartingRow && move.to().y() == pawnDoublePushRow) ? move.to() : null;
    }

    private void updateBoardPieces(Move move) {
        BoardPiece pieceToMove = boardPieces[move.from().y()][move.from().x()];
        BoardPiece pieceToCapture = boardPieces[move.to().y()][move.to().x()];
        boardPieces[move.from().y()][move.from().x()] = null;
        BoardPiece pieceToPut = pieceToMove;

        if (move instanceof PromotionMove) {
            pieceToPut = ((PromotionMove) move).getPromotionPiece();
        } else if (pieceToMove.isPawn() && pieceToCapture == null && move.from().x() != move.to().x()) {
            boardPieces[possibleEnPassant.y()][possibleEnPassant.x()] = null;
        }
        boardPieces[move.to().y()][move.to().x()] = pieceToPut;
    }

    private void updatePiecesIndexes(Move move) {
        piecesIndexes.remove(new BoardPosition(move.from().x(), move.from().y()));
        piecesIndexes.add(new BoardPosition(move.to().x(), move.to().y()));
    }

    public static Board initializeDefaultBoard() {
        return initializeFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    public static Board initializeFromFen(String fen) {
        String[] fenParts = fen.split(" ");
        var boardPieces = initializeBoardPiecesFromFen(fenParts[0]);
        var piecesIndexes = initializePiecesIndexes(boardPieces);
        var isWhiteToMove = fenParts[1].equals("w");
        var castlingRightsWhite = CastlingRights.fromFen(fenParts[2], true);
        var castlingRightsBlack = CastlingRights.fromFen(fenParts[2], false);
        var enPassantTarget = (! fenParts[3].equals("-")) ? new BoardPosition(fenParts[3]) : null;
        var halfMoveClock = Integer.parseInt(fenParts[4]);
        var fullMoveNumber = Integer.parseInt(fenParts[5]);
        Board board = new Board(boardPieces, piecesIndexes, isWhiteToMove, castlingRightsWhite, castlingRightsBlack, enPassantTarget, halfMoveClock, fullMoveNumber);
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

    public BoardPiece[][] getBoardPieces() {
        return boardPieces;
    }

    public boolean isWhiteToMove() {
        return isWhiteToMove;
    }

    public boolean isBlackToMove(){
        return !isWhiteToMove;
    }

    public CastlingRights getCastlingRightsWhite() {
        return castlingRightsWhite;
    }

    public CastlingRights getCastlingRightsBlack() {
        return castlingRightsBlack;
    }

    public int getHalfmoveClock() {
        return halfmoveClock;
    }

    public void setHalfmoveClock(int halfmoveClock) {
        this.halfmoveClock = halfmoveClock;
    }

    public int getFullmoveNumber() {
        return fullmoveNumber;
    }

    public void setFullmoveNumber(int fullmoveNumber) {
        this.fullmoveNumber = fullmoveNumber;
    }

    public BoardPosition getPossibleEnPassant() {
        return possibleEnPassant;
    }

    public List<BoardPosition> getPiecesIndexes() {
        return piecesIndexes;
    }

    public BoardPiece getBoardPiece(BoardPosition currentPosition) {
        return this.boardPieces[currentPosition.y()][currentPosition.x()];
    }
}