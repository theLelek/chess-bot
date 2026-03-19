package chess.board.model;

import chess.board.PseudoLegalMoveFinder;

import java.util.*;

public class Board {

    private final BoardPiece[][] boardPieces;
    private final List<BoardIndex> piecesIndexes; // TODO O(n) for removing elements find faster way
    private final PieceColor sideToMove;
    private final CastlingRights castlingRights;
    private final String possibleEnPassants;
    private int halfMoveClock;
    private int fullMoveNumber;
    // TODO enPassantTarget is not finished yet, will be in each BoardPiece

    public Board(BoardPiece[][] boardPieces, List<BoardIndex> piecesIndexes, PieceColor sideToMove, CastlingRights castlingRights, String possibleEnPassants, int halfMoveClock, int fullMoveNumber) {
        this.boardPieces = boardPieces;
        this.piecesIndexes = piecesIndexes;
        this.sideToMove = sideToMove;
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
        var sideToMove = PieceColor.initializeFromFen(fenParts[1]);
        var castlingRights = CastlingRights.initializeFromFen(fenParts[2]);
        var enPassantTarget = fenParts[3];
        var halfMoveClock = Integer.parseInt(fenParts[4]);
        var fullMoveNumber = Integer.parseInt(fenParts[5]);
        Board board = new Board(boardPieces, piecesIndexes, sideToMove, castlingRights, enPassantTarget, halfMoveClock, fullMoveNumber);
        PseudoLegalMoveFinder.addPseudoLegalMoves(board);
        return board;
    }

     private static BoardPiece[][] initializeBoardPiecesFromFen(String piecePlacements) {
        BoardPiece[][] boardPieces = new BoardPiece[8][8];
        String[] lines = piecePlacements.split("/");
        for (int i = lines.length -1; i >= 0; i--) {
            for (int j = 0; j < lines[i].length(); j++) {
                char currentChar =  lines[i].charAt(j);
                if (Character.isDigit(currentChar)) {
                    j += Integer.parseInt(String.valueOf(currentChar));
                    continue;
                }
                boardPieces[i][j] = BoardPiece.getByFen(currentChar);
            }
        }
        return boardPieces;
    }

    private static List<BoardIndex> initializePiecesIndexes(BoardPiece[][] boardPieces) {
        List<BoardIndex> piecesIndexes = new ArrayList<>();
        for (int i = 0; i < boardPieces.length; i++) {
            for (int j = 0; j < boardPieces[i].length; j++) {
                if (boardPieces[i][j] != null) {
                    piecesIndexes.add(new BoardIndex(i, j));
                }
            }
        }
        return piecesIndexes;
    }

    public BoardPiece[][] getBoardPieces() {
        return boardPieces;
    }

    public PieceColor getSideToMove() {
        return sideToMove;
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

    public String getPossibleEnPassants() {
        return possibleEnPassants;
    }

    public List<BoardIndex> getPiecesIndexes() {
        return piecesIndexes;
    }
}