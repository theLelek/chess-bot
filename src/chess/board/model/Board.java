package chess.board.model;

import chess.BoardPosition;
import chess.Color;
import chess.Move.CastlingMove;
import chess.Move.EnPassantMove;
import chess.Move.Move;
import chess.Move.PromotionMove;
import chess.board.BoardPiece;
import chess.board.OccupancyBitboard;
import chess.board.UnmakeMoveInfo;

import java.rmi.server.RMIFailureHandler;
import java.util.Arrays;
import java.util.Objects;

public class Board {

    public static final int SIZE = 8;

    private boolean isWhiteToMove;
    private CastlingRights castlingRightsWhite;
    private CastlingRights castlingRightsBlack;
    private BoardPosition enPassantTargetSquare;
    private int halfmoveClock;
    private int fullmoveNumber;

    private final BitBoardState bitBoardState;
    private final BoardPiece[] pieceList;

    public Board(boolean isWhiteToMove, CastlingRights castlingRightsWhite, CastlingRights castlingRightsBlack, BoardPosition enPassantTargetSquare, int halfmoveClock, int fullmoveNumber, BitBoardState bitBoardState, BoardPiece[] pieceList) {
        this.isWhiteToMove = isWhiteToMove;
        this.castlingRightsWhite = castlingRightsWhite;
        this.castlingRightsBlack = castlingRightsBlack;
        this.enPassantTargetSquare = enPassantTargetSquare;
        this.halfmoveClock = halfmoveClock;
        this.fullmoveNumber = fullmoveNumber;
        this.bitBoardState = bitBoardState;
        this.pieceList = pieceList;
    }

    public static Board initializeDefaultBoard() {
        return initializeFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    public static Board initializeFromFen(String fen) {
        String[] fenParts = fen.split(" ");
        var isWhiteToMove = fenParts[1].equals("w");
        var castlingRightsWhite = CastlingRights.fromFen(fenParts[2], true);
        var castlingRightsBlack = CastlingRights.fromFen(fenParts[2], false);
        var enPassantTarget = (! fenParts[3].equals("-")) ? new BoardPosition(fenParts[3]) : null;
        var halfMoveClock = Integer.parseInt(fenParts[4]);
        var fullMoveNumber = Integer.parseInt(fenParts[5]);
        var pieceList = initializePieceList(fenParts[0]);
        var bitboardState = BitBoardState.initializeFromPieceList(pieceList);
        return new Board(isWhiteToMove, castlingRightsWhite, castlingRightsBlack, enPassantTarget, halfMoveClock, fullMoveNumber, bitboardState, pieceList);
    }

    private static BoardPiece[] initializePieceList(String fen) {
        BoardPiece[] pieceList = new BoardPiece[Board.SIZE * Board.SIZE];
        String[] lines = fen.split("/");

        for (int i = 0; i < lines.length; i++) {
            int column = 0;
            for (int j = 0; j < lines[i].length(); j++) {
                char currentChar = lines[i].charAt(j);
                BoardPosition currentPosition = new BoardPosition(column, i);
                if (Character.isDigit(currentChar)) {
                    column += currentChar - '0';
                } else {
                    BoardPiece piece = BoardPiece.fromFen(currentChar);
                    pieceList[currentPosition.getBitBoardSquare()] = piece;
                    column++;
                }
            }
        }
        return pieceList;
    }

    public void makeMove(Move move) {
        BoardPiece pieceToMove = pieceList[move.from().getBitBoardSquare()];
        BoardPiece pieceToCapture = pieceList[move.to().getBitBoardSquare()];

        updateCastlingRights(move);
        halfmoveClock = (pieceToMove.isPawn() || pieceToCapture != null) ? 0 : halfmoveClock + 1;
        if (isBlackToMove()) fullmoveNumber++;
        updatePieces(move);
        isWhiteToMove = ! isWhiteToMove;
    }

    private void updateCastlingRights(Move move) {
        Color color = (isWhiteToMove) ? Color.WHITE : Color.BLACK;
        BoardPiece pieceToMove = pieceList[move.from().getBitBoardSquare()];

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
        BoardPiece pieceToMove = pieceList[move.from().getBitBoardSquare()];
        Color color = (pieceToMove.isWhite()) ? Color.WHITE : Color.BLACK;

        switch (move) {
            case PromotionMove m:
                changePieceNormal(move.from(), move.to(), null, m.getPromotionPiece());
                break;
            case EnPassantMove m:
                updatePiecesEnPassantMove(m, false);
                break;
            case CastlingMove m:
                changePiecesCastlingMove(m, false);
                break;
            default:
                changePieceNormal(move.from(), move.to(), null, pieceList[move.from().getBitBoardSquare()]);
                break;
        }
        enPassantTargetSquare = (pieceToMove.isPawn() && Math.abs(move.from().y() - move.to().y()) == 2) ? new BoardPosition(move.to().x(), move.to().y() - color.getMovingDirection()) : null;
    }

    public void unmakeMove(Move move, UnmakeMoveInfo unmakeMoveInfo) {
        castlingRightsWhite = unmakeMoveInfo.castlingRightsWhite();
        castlingRightsBlack = unmakeMoveInfo.castlingRightsBlack();
        halfmoveClock = unmakeMoveInfo.halfMoveClock();
        if (isWhiteToMove) fullmoveNumber--;
        isWhiteToMove = ! isWhiteToMove;
        outdatePieces(move, unmakeMoveInfo);
    }

    private void outdatePieces(Move move, UnmakeMoveInfo unmakeMoveInfo) {
        BoardPiece pieceToMove = pieceList[move.to().getBitBoardSquare()];
        Color color = (pieceToMove.isWhite()) ? Color.WHITE : Color.BLACK;

        switch (move) {
            case PromotionMove _:
                changePieceNormal(move.to(), move.from(), unmakeMoveInfo.capturedPiece(), color.getPawn());
                break;
            case EnPassantMove m:
                updatePiecesEnPassantMove(m, true);
                break;
            case CastlingMove m:
                changePiecesCastlingMove(m, true);
                break;
            default:
                changePieceNormal(move.to(), move.from(), unmakeMoveInfo.capturedPiece(), pieceToMove);
                break;
        }
        enPassantTargetSquare = unmakeMoveInfo.enPassantTargetSquare();
    }

    private void changePiecesCastlingMove(CastlingMove move, boolean undo) {
        Color color = undo ? (isWhiteToMove ? Color.BLACK : Color.WHITE) : (isWhiteToMove ? Color.WHITE : Color.BLACK);

        BoardPosition kingFrom = undo ? move.to() : move.from();
        BoardPosition kingTo = undo ? move.from() : move.to();

        changePieceNormal(kingFrom, kingTo, null, color.getKing());

        boolean isKingSideCastling = move.to().x() == Board.SIZE - 2;

        BoardPosition rookOriginalFrom = new BoardPosition(isKingSideCastling ? Board.SIZE - 1 : 0, move.to().y());
        BoardPosition rookOriginalTo = new BoardPosition(isKingSideCastling ? Board.SIZE - 3 : 3, move.to().y());

        BoardPosition rookFrom = undo ? rookOriginalTo : rookOriginalFrom;
        BoardPosition rookTo = undo ? rookOriginalFrom : rookOriginalTo;

        changePieceNormal(rookFrom, rookTo, null, color.getRook());
    }

    private void updatePiecesEnPassantMove(EnPassantMove move, boolean undo) {
        Color color = (isWhiteToMove) ? Color.WHITE : Color.BLACK;
        BoardPosition enPassantPiecePosition = getEnPassantPiecePosition();

        if (!undo) {
            changePieceNormal(move.from(), move.to(), null, pieceList[move.from().getBitBoardSquare()]);

            // remove pawn to be captured
            bitBoardState.clearBit(color.getOpponentPawn(), enPassantPiecePosition);
            bitBoardState.clearBit(color.getOpponentOccupancyBitboard(), enPassantPiecePosition);
            bitBoardState.clearBit(OccupancyBitboard.ALL_PIECES, enPassantPiecePosition);
            pieceList[enPassantPiecePosition.getBitBoardSquare()] = null;
        } else {
            changePieceNormal(move.to(), move.from(), null, pieceList[move.to().getBitBoardSquare()]);

            // restore captured pawn
            bitBoardState.setBit(color.getOpponentPawn(), enPassantPiecePosition);
            bitBoardState.setBit(color.getOpponentOccupancyBitboard(), enPassantPiecePosition);
            bitBoardState.setBit(OccupancyBitboard.ALL_PIECES, enPassantPiecePosition);
            pieceList[enPassantPiecePosition.getBitBoardSquare()] = color.getOpponentPawn();
        }
    }

    private void changePieceNormal(BoardPosition from, BoardPosition to, BoardPiece pieceToReplaceWith, BoardPiece pieceToBecome) {
        BoardPiece pieceToMove = pieceList[from.getBitBoardSquare()];
        Color color = (isWhiteToMove) ? Color.WHITE : Color.BLACK;
        BoardPiece pieceToCapture = pieceList[to.getBitBoardSquare()];

        // remove piece on from
        bitBoardState.clearBit(pieceToMove, from);
        bitBoardState.clearBit(OccupancyBitboard.ALL_PIECES, from);
        bitBoardState.clearBit(color.getOwnOccupancyBitboard(), from);
        pieceList[from.getBitBoardSquare()] = null;

        // un-capturing a piece (used for unmakeMove)
        if (pieceToReplaceWith != null) {
            bitBoardState.setBit(pieceToReplaceWith, from);
            bitBoardState.setBit(OccupancyBitboard.ALL_PIECES, from);
            bitBoardState.setBit(color.getOpponentOccupancyBitboard(), from); // todo color.getOpponent... should be changed
            pieceList[from.getBitBoardSquare()] = pieceToReplaceWith;
        }

        // capture piece if it exists
        if (pieceToCapture != null) {
            bitBoardState.clearBit(pieceToCapture, to);
            bitBoardState.clearBit(OccupancyBitboard.ALL_PIECES, to);
            bitBoardState.clearBit(color.getOpponentOccupancyBitboard(), to);
        }

        // place piece on to
        bitBoardState.setBit(pieceToBecome, to);
        bitBoardState.setBit(OccupancyBitboard.ALL_PIECES, to);
        bitBoardState.setBit(color.getOwnOccupancyBitboard(), to);
        pieceList[to.getBitBoardSquare()] = pieceToBecome;
    }

    public BoardPosition getEnPassantPiecePosition() { // todo test
        if (enPassantTargetSquare == null) throw new RuntimeException("No en passant target square set");
        Color color = (isWhiteToMove) ? Color.WHITE : Color.BLACK;
        return new BoardPosition(enPassantTargetSquare.x(), enPassantTargetSquare.y() - color.getMovingDirection());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return isWhiteToMove == board.isWhiteToMove && halfmoveClock == board.halfmoveClock && fullmoveNumber == board.fullmoveNumber && Objects.equals(castlingRightsWhite, board.castlingRightsWhite) && Objects.equals(castlingRightsBlack, board.castlingRightsBlack) && Objects.equals(enPassantTargetSquare, board.enPassantTargetSquare) && Objects.equals(bitBoardState, board.bitBoardState) && Objects.deepEquals(pieceList, board.pieceList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isWhiteToMove, castlingRightsWhite, castlingRightsBlack, enPassantTargetSquare, halfmoveClock, fullmoveNumber, bitBoardState, Arrays.hashCode(pieceList));
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

    public BitBoardState getPosition() {
        return bitBoardState;
    }

    public BoardPiece[] getPieceList() {
        return pieceList;
    }

    public BitBoardState getBitBoardState() {
        return bitBoardState;
    }
}