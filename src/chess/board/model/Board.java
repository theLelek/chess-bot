package chess.board.model;

import chess.BoardPosition;
import chess.Color;
import chess.Move;
import chess.board.PseudoLegalMoveFinder;

import java.util.*;

public class Board {

    public static final int SIZE = 8;

    private final BoardPiece[][] boardPieces;
    private final List<BoardPosition> piecesIndexes; // TODO O(n) for removing elements find faster way
    private final boolean isWhiteToMove;
    private final CastlingRights castlingRightsWhite;
    private final CastlingRights castlingRightsBlack;
    private final BoardPosition possibleEnPassant; // TODO is currently stored 2 times
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

//    public Board move(Move move) {
//        BoardPiece pieceToMove = boardPieces[move.from().y()][move.from().x()];
//        BoardPiece pieceToCapture = boardPieces[move.to().y()][move.to().x()];
//
//        var boardPiecesUpdated = updateBoardPieces(move);
//        var piecesIndexesUpdated = updatePiecesIndexes(move);
//        var isWhiteToMoveCopy = ! isWhiteToMove;
//        var castlingRightsWhiteCopy = updateCastlingRightsForRookMove(castlingRightsWhite, move, Color.WHITE);
//        var castlingRightsBlackCopy = updateCastlingRightsForRookMove(castlingRightsBlack, move, Color.BLACK);
//        var possibleEnPassantsCopy = getPossibleEnPassant().copy();
//        var halfMoveClockCopy = (pieceToMove == BoardPiece.WHITE_PAWN || pieceToMove == BoardPiece.BLACK_PAWN || pieceToCapture != null) ? 0 : halfmoveClock + 1;
//        var fullMoveNumberCopy = (pieceToMove.isBlack()) ? fullmoveNumber + 1 : fullmoveNumber;
//
//        return new Board(boardPiecesUpdated, piecesIndexesUpdated, isWhiteToMoveCopy, castlingRightsWhiteCopy, castlingRightsBlackCopy, possibleEnPassantsCopy, halfMoveClockCopy, fullMoveNumberCopy);
//    }
//
//    private BoardPiece[][] updateBoardPieces(Move move) {
//        BoardPiece[][] boardPiecesUpdated = new BoardPiece[Board.SIZE][Board.SIZE];
//        for (int i = 0; i < Board.SIZE; i++) {
//            boardPiecesUpdated[i] = this.boardPieces[i].clone();
//        }
//        BoardPiece foo = boardPiecesUpdated[move.from().y()][move.from().x()];
//        boardPiecesUpdated[move.from().y()][move.from().x()] = null;
//        boardPiecesUpdated[move.to().y()][move.to().x()] = foo;
//        return boardPiecesUpdated;
//    }
//
//    private List<BoardPosition> updatePiecesIndexes(Move move) {
//        List<BoardPosition> piecesIndexesUpdated = new ArrayList<>(piecesIndexes);
//        piecesIndexesUpdated.remove(new BoardPosition(move.from().x(), move.from().y()));
//        piecesIndexesUpdated.add(new BoardPosition(move.to().x(), move.to().y()));
//        return piecesIndexesUpdated;
//    }
//
//    private CastlingRights updateCastlingRightsForRookMove(CastlingRights rights, Move move, Color color) {
//        int xFrom = move.from().x();
//        int yFrom = move.from().y();
//
//        if (color == Color.WHITE) {
//            // bottom right
//            if (xFrom == Board.SIZE - 1 && yFrom == Board.SIZE - 1) {
//                return new CastlingRights(false, rights.canCastleQueenSide());
//            }
//            // bottom left
//            if (xFrom == 0 && yFrom == Board.SIZE - 1) {
//                return new CastlingRights(rights.canCastleKingSide(), false);
//            }
//        } else {
//            // top right
//            if (xFrom == Board.SIZE - 1 && yFrom == 0) {
//                return new CastlingRights(false, rights.canCastleQueenSide());
//            }
//            // top left
//            if (xFrom == 0 && yFrom == 0) {
//                return new CastlingRights(rights.canCastleKingSide(), false);
//            }
//        }
//        return rights;
//    }

    public static Board initializeDefaultBoard() {
        return initializeFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    public static Board initializeFromFen(String fen) {
        String[] fenParts = fen.split(" ");
        var boardPieces = initializeBoardPiecesFromFen(fenParts[0]);
        var piecesIndexes = initializePiecesIndexes(boardPieces);
        var isWhiteToMove = fenParts[1].equals("w");
        var castlingRightsWhite = CastlingRights.fromFen(fenParts[2], Color.WHITE);
        var castlingRightsBlack = CastlingRights.fromFen(fenParts[2], Color.BLACK);
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