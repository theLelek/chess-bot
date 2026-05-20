package chess.board.model;

import chess.BoardPosition;
import chess.Color;
import chess.Move.CastlingMove;
import chess.Move.EnPassantMove;
import chess.Move.Move;
import chess.Move.PromotionMove;

import java.util.*;

public class Board {

    public static final int SIZE = 8;

    private final BoardPiece[][] boardPieces;
    private final Set<BoardPosition> piecesIndexes; // TODO O(n) for removing elements find faster way
    private boolean isWhiteToMove;
    private final CastlingRights castlingRightsWhite;
    private final CastlingRights castlingRightsBlack;
    private BoardPosition enPassantTargetSquare;
    private int halfmoveClock;
    private int fullmoveNumber;

    private final Position position;

    public Board(BoardPiece[][] boardPieces, Set<BoardPosition> piecesIndexes, boolean isWhiteToMove, CastlingRights castlingRightsWhite, CastlingRights castlingRightsBlack, BoardPosition enPassantTargetSquare, int halfmoveClock, int fullmoveNumber, Position position) {
        this.boardPieces = boardPieces;
        this.piecesIndexes = piecesIndexes;
        this.isWhiteToMove = isWhiteToMove;
        this.castlingRightsWhite = castlingRightsWhite;
        this.castlingRightsBlack = castlingRightsBlack;
        this.enPassantTargetSquare = enPassantTargetSquare;
        this.halfmoveClock = halfmoveClock;
        this.fullmoveNumber = fullmoveNumber;
        this.position = position;
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
        var position = Position.initializeFromFen(fenParts[0]);
        return new Board(boardPieces, piecesIndexes, isWhiteToMove, castlingRightsWhite, castlingRightsBlack, enPassantTarget, halfMoveClock, fullMoveNumber, position);
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
                boardPieces[i][boardCounter] = BoardPiece.fromFen(currentChar);
                boardCounter++;
            }
        }
        return boardPieces;
    }

    private static Set<BoardPosition> initializePiecesIndexes(BoardPiece[][] boardPieces) {
        Set<BoardPosition> piecesIndexes = new HashSet<>();
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

    public void move(Move move) {
        BoardPiece pieceToMove = getBoardPiece(move.from());
        BoardPiece pieceToCapture = getBoardPiece(move.to());

        updateCastlingRights(move);
        halfmoveClock = (pieceToMove.isPawn() || pieceToCapture != null) ? 0 : halfmoveClock + 1;
        if (isBlackToMove()) fullmoveNumber++;
        updatePieces(move);
        isWhiteToMove = ! isWhiteToMove;
    }

    private void updateCastlingRights(Move move) {
        Color color = (isWhiteToMove) ? Color.WHITE : Color.BLACK;
        BoardPiece pieceToMove = getBoardPiece(move.from());
        int homeRank = color.getHomeRank();
        int backRank = color.getBackRank();
        CastlingRights castlingRights = (isWhiteToMove) ? castlingRightsWhite : castlingRightsBlack;
        CastlingRights castlingRightsOpponent = (isWhiteToMove) ? castlingRightsBlack : castlingRightsWhite;

        if (pieceToMove.isKing()) {
            castlingRights.setCanCastleKingSide(false);
            castlingRights.setCanCastleQueenSide(false);
        }
        if (move.from().equals(new BoardPosition(0, homeRank))) {
            castlingRights.setCanCastleQueenSide(false);
        }
        if (move.from().equals(new BoardPosition(Board.SIZE - 1, homeRank))) {
            castlingRights.setCanCastleKingSide(false);
        }
        if (move.to().equals(new BoardPosition(0, backRank))) {
            castlingRightsOpponent.setCanCastleQueenSide(false);
        }
        if (move.to().equals(new BoardPosition(Board.SIZE - 1, backRank))) {
            castlingRightsOpponent.setCanCastleKingSide(false);
        }
    }

    private void updatePieces(Move move) {
        BoardPiece pieceToMove = boardPieces[move.from().y()][move.from().x()];
        setBoardPiece(move.from(), null);
        setBoardPiece(move.to(), pieceToMove);
        piecesIndexes.remove(move.from());
        piecesIndexes.add(move.to());
        Color colorToMove = (pieceToMove.isWhite()) ? Color.WHITE : Color.BLACK;

        switch (move) {
            case PromotionMove m:
                setBoardPiece(move.to(), m.getPromotionPiece());
                break;
            case EnPassantMove _:
                setBoardPiece(move.from(), null);
                piecesIndexes.remove(new BoardPosition(move.to().x(), move.from().y()));
                break;
            case CastlingMove m:
                if (m.to().x() == Board.SIZE - 2) { // king side castling
                    boardPieces[move.from().y()][Board.SIZE - 3] = boardPieces[move.from().y()][Board.SIZE - 1];
                    boardPieces[move.from().y()][Board.SIZE - 1] = null;
                    piecesIndexes.remove(new BoardPosition(Board.SIZE - 1, move.from().y()));
                    piecesIndexes.add(new BoardPosition(Board.SIZE - 3, move.from().y()));
                } else { // queen side castling
                    boardPieces[move.from().y()][3] = boardPieces[move.from().y()][0];
                    boardPieces[move.from().y()][0] = null;
                    piecesIndexes.remove(new BoardPosition(0, move.from().y()));
                    piecesIndexes.add(new BoardPosition(3, move.from().y()));
                }
                break;
            default:
                break;
        }
        enPassantTargetSquare = (pieceToMove.isPawn() && Math.abs(move.from().y() - move.to().y()) == 2) ? new BoardPosition(move.to().x(), move.to().y() - colorToMove.getMovingDirection()) : null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < boardPieces.length; y++) {
            for (int x = 0; x < boardPieces[y].length; x++) {
                BoardPiece piece = boardPieces[y][x];
                if (piece == null) {
                    sb.append(". ");
                } else {
                    sb.append(piece.getFen()).append(" ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public BoardPosition getEnPassantPiecePosition() {
        if (enPassantTargetSquare == null) return null;
        Color color = (isWhiteToMove) ? Color.WHITE : Color.BLACK;
        return new BoardPosition(enPassantTargetSquare.x(), enPassantTargetSquare.y() - color.getMovingDirection());
    }

    public BoardPiece[][] getBoardPieces() {
        return boardPieces;
    }

    public BoardPiece getBoardPiece(BoardPosition currentPosition) {
        return boardPieces[currentPosition.y()][currentPosition.x()];
    }

    public void setBoardPiece(BoardPosition currentPosition, BoardPiece piece) {
        boardPieces[currentPosition.y()][currentPosition.x()] = piece;
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

    public BoardPosition getEnPassantTargetSquare() {
        return enPassantTargetSquare;
    }

    public Set<BoardPosition> getPiecesIndexes() {
        return piecesIndexes;
    }

    public Position getPosition() {
        return position;
    }
}