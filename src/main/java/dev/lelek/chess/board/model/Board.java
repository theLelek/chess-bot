package dev.lelek.chess.board.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.lelek.chess.BoardPosition;
import dev.lelek.chess.Color;
import dev.lelek.chess.Move.CastlingMove;
import dev.lelek.chess.Move.EnPassantMove;
import dev.lelek.chess.Move.Move;
import dev.lelek.chess.Move.PromotionMove;
import dev.lelek.chess.BoardPiece;
import dev.lelek.chess.board.OccupancyBitboard;
import dev.lelek.chess.board.UnmakeMoveInfo;

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

    private BoardPosition whiteKingPosition;
    private BoardPosition blackKingPosition;

    private long zobristHash;

    private static final Logger log = LoggerFactory.getLogger(Board.class);

    private Board(boolean isWhiteToMove, CastlingRights castlingRightsWhite, CastlingRights castlingRightsBlack, BoardPosition enPassantTargetSquare, int halfmoveClock, int fullmoveNumber, BitBoardState bitBoardState, BoardPiece[] pieceList, BoardPosition whiteKingPosition, BoardPosition blackKingPosition, long zobristHash) {
        this.isWhiteToMove = isWhiteToMove;
        this.castlingRightsWhite = castlingRightsWhite;
        this.castlingRightsBlack = castlingRightsBlack;
        this.enPassantTargetSquare = enPassantTargetSquare;
        this.halfmoveClock = halfmoveClock;
        this.fullmoveNumber = fullmoveNumber;
        this.bitBoardState = bitBoardState;
        this.pieceList = pieceList;
        this.whiteKingPosition = whiteKingPosition;
        this.blackKingPosition = blackKingPosition;
        this.zobristHash = zobristHash;
    }

    public static Board initializeDefaultBoard() {
        return fromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    public static Board fromFen(String fen) {
        log.debug("loaded fen: {}", fen);
        String[] fenParts = fen.split(" ");
        var isWhiteToMove = fenParts[1].equals("w");
        var castlingRightsWhite = CastlingRights.fromFen(fenParts[2], true);
        var castlingRightsBlack = CastlingRights.fromFen(fenParts[2], false);
        var enPassantTarget = (! fenParts[3].equals("-")) ? new BoardPosition(fenParts[3]) : null;
        var halfMoveClock = Integer.parseInt(fenParts[4]);
        var fullMoveNumber = Integer.parseInt(fenParts[5]);
        var pieceList = initializePieceList(fenParts[0]);
        var bitboardState = BitBoardState.initializeFromPieceList(pieceList);
        var whiteKingPosition = getKingBoardPosition(pieceList, Color.WHITE);
        var blackKingPosition = getKingBoardPosition(pieceList, Color.BLACK);
        Board board = new Board(isWhiteToMove, castlingRightsWhite, castlingRightsBlack, enPassantTarget, halfMoveClock, fullMoveNumber, bitboardState, pieceList, whiteKingPosition, blackKingPosition, -1);
        board.zobristHash = Zobrist.fromBoard(board);
        return board;
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

    private static BoardPosition getKingBoardPosition(BoardPiece[] pieceList, Color color) {
        try {
            return initializeKingPosition(pieceList, color);
        } catch (NoKingFoundException e) {
            log.warn("No king found in board: {}", Arrays.toString(pieceList));
        }
        return null;
    }

    private static BoardPosition initializeKingPosition(BoardPiece[] pieceList, Color color) {
        for (int i = 0; i < pieceList.length; i++) {
            if (pieceList[i] == color.getKing()) {
                return new BoardPosition(i);
            }
        }
        throw new NoKingFoundException("couldnt find king in board");
    }

    public String toFen() {
        String fen = piecePlacementsToFen();
        fen += " " + (isWhiteToMove() ? "w" : "b");
        fen += " " + castlingRightsToFen();
        fen += " " + (enPassantTargetSquare == null ? "-" : enPassantTargetSquare.toFen());
        fen += " " + halfmoveClock;
        fen += " " + fullmoveNumber;
        log.debug("generated fen: {}", fen);
        return fen;
    }

    private String piecePlacementsToFen() {
        String[] piecePlacements = new String[Board.SIZE];
        Arrays.fill(piecePlacements, "");
        for (int i = 0; i < Board.SIZE; i++) {
            int column = 0;
            for (int j = 0; j < Board.SIZE; j++) {
                BoardPosition position = new BoardPosition(j, i);
                BoardPiece piece = getPieceAt(position);
                if (piece != null) {
                    if (column != 0) piecePlacements[i] += column;
                    piecePlacements[i] += piece.toFen();
                    column = 0;
                } else {
                    column++;
                }
            }
            if (column != 0) piecePlacements[i] += column;
        }
        return String.join("/", piecePlacements);
    }

    private String castlingRightsToFen() {
        String castlingRights = "";
        castlingRights += castlingRightsWhite.toFen(Color.WHITE);
        castlingRights += castlingRightsBlack.toFen(Color.BLACK);
        if (castlingRights.isEmpty()) castlingRights = "-";
        return castlingRights;
    }

    public void makeMove(Move move) {
        BoardPiece pieceToMove = pieceList[move.getFrom().getBitBoardSquare()];
        BoardPiece pieceToCapture = pieceList[move.getTo().getBitBoardSquare()];

        if (isEnPassantCaptureAvailable()) zobristHash ^= Zobrist.getEnPassantKeys(enPassantTargetSquare); // unmake previous possible en passant
        zobristHash ^= Zobrist.SIDE_TO_MOVE_KEY;
        halfmoveClock = (pieceToMove.isPawn() || pieceToCapture != null) ? 0 : halfmoveClock + 1;
        if (isBlackToMove()) fullmoveNumber++;

        updateCastlingRights(move);
        updatePieces(move);
        isWhiteToMove = ! isWhiteToMove;
        if (isEnPassantCaptureAvailable()) zobristHash ^= Zobrist.getEnPassantKeys(enPassantTargetSquare);

        if (pieceToMove.isKing()) {
            if (pieceToMove.isWhite()) {
                whiteKingPosition = move.getTo();
            } else {
                blackKingPosition = move.getTo();
            }
        }
    }

    private void updateCastlingRights(Move move) {
        Color color = isWhiteToMove ? Color.WHITE : Color.BLACK;
        BoardPiece pieceToMove = pieceList[move.getFrom().getBitBoardSquare()];

        int homeRank = color.getHomeRank();
        int backRank = color.getBackRank();
        CastlingRights castlingRights =         isWhiteToMove ? castlingRightsWhite : castlingRightsBlack;
        CastlingRights castlingRightsOpponent = isWhiteToMove ? castlingRightsBlack : castlingRightsWhite;
        long kingSideCastleKey =            isWhiteToMove ? Zobrist.getKingSideCastleWhiteKey() : Zobrist.getKingSideCastleBlackKey();
        long kingSideCastleOpponentKey =    isWhiteToMove ? Zobrist.getKingSideCastleBlackKey() : Zobrist.getKingSideCastleWhiteKey();
        long queenSideCastleKey =           isWhiteToMove ? Zobrist.getQueenSideCastleWhiteKey() : Zobrist.getQueenSideCastleBlackKey();
        long queenSideCastleOpponentKey =   isWhiteToMove ? Zobrist.getQueenSideCastleBlackKey() : Zobrist.getQueenSideCastleWhiteKey();

        if (pieceToMove.isKing()) {
            if (castlingRights.canCastleKingSide()) {
                castlingRights.setCanCastleKingSide(false);
                zobristHash ^= kingSideCastleKey;
            }
            if (castlingRights.canCastleQueenSide()) {
                castlingRights.setCanCastleQueenSide(false);
                zobristHash ^= queenSideCastleKey;
            }
        }
        if (move.getFrom().equals(new BoardPosition(0, homeRank))) {
            if (castlingRights.canCastleQueenSide()) {
                castlingRights.setCanCastleQueenSide(false);
                zobristHash ^= queenSideCastleKey;
            }
        }
        if (move.getFrom().equals(new BoardPosition(Board.SIZE - 1, homeRank))) {
            if (castlingRights.canCastleKingSide()) {
                castlingRights.setCanCastleKingSide(false);
                zobristHash ^= kingSideCastleKey;
            }
        }
        if (move.getTo().equals(new BoardPosition(0, backRank))) {
            if (castlingRightsOpponent.canCastleQueenSide()) {
                castlingRightsOpponent.setCanCastleQueenSide(false);
                zobristHash ^= queenSideCastleOpponentKey;
            }
        }
        if (move.getTo().equals(new BoardPosition(Board.SIZE - 1, backRank))) {
            if (castlingRightsOpponent.canCastleKingSide()) {
                castlingRightsOpponent.setCanCastleKingSide(false);
                zobristHash ^= kingSideCastleOpponentKey;
            }
        }
    }

    private void updatePieces(Move move) {
        BoardPiece pieceToMove = pieceList[move.getFrom().getBitBoardSquare()];
        Color color = (pieceToMove.isWhite()) ? Color.WHITE : Color.BLACK;

        switch (move) {
            case PromotionMove m:
                changePieceNormal(move.getFrom(), move.getTo(), null, m.getPromotionPiece());
                break;
            case EnPassantMove m:
                updatePiecesEnPassantMove(m, false);
                break;
            case CastlingMove m:
                changePiecesCastlingMove(m, false);
                break;
            default:
                changePieceNormal(move.getFrom(), move.getTo(), null, pieceList[move.getFrom().getBitBoardSquare()]);
                break;
        }
        enPassantTargetSquare = (pieceToMove.isPawn() && Math.abs(move.getFrom().getY() - move.getTo().getY()) == 2) ? new BoardPosition(move.getTo().getX(), move.getTo().getY() - color.getMovingDirection()) : null;
    }

    boolean isEnPassantCaptureAvailable() {
        Color color = isWhiteToMove() ? Color.WHITE : Color.BLACK;
        BoardPosition position = getEnPassantPiecePosition();
        if (position == null) return false;

        BoardPosition positionLeft = position.getX() == 0 ? null : new BoardPosition(position.getX() - 1, position.getY());
        BoardPosition positionRight = position.getX() == Board.SIZE - 1 ? null : new BoardPosition(position.getX() + 1, position.getY());

        if (positionLeft != null && getPieceAt(positionLeft) == color.getPawn()) return true;
        if (positionRight != null && getPieceAt(positionRight) == color.getPawn()) return true;

        return false;
    }

    public void unmakeMove(Move move, UnmakeMoveInfo unmakeMoveInfo) {
        BoardPiece pieceToMove = pieceList[move.getTo().getBitBoardSquare()];
        zobristHash ^= Zobrist.SIDE_TO_MOVE_KEY;

        unmakeCastlingRights(unmakeMoveInfo);
        enPassantTargetSquare = unmakeMoveInfo.enPassantTargetSquare();
        halfmoveClock = unmakeMoveInfo.halfMoveClock();
        if (isWhiteToMove) fullmoveNumber--;
        isWhiteToMove = ! isWhiteToMove;
        outdatePieces(move, unmakeMoveInfo);

        if (isEnPassantCaptureAvailable()) zobristHash ^= Zobrist.getEnPassantKeys(enPassantTargetSquare); // unmake previous possible en passant

        if (pieceToMove.isKing()) {
            if (pieceToMove.isWhite()) {
                whiteKingPosition = move.getFrom();
            } else {
                blackKingPosition = move.getFrom();
            }
        }
    }

    private void unmakeCastlingRights(UnmakeMoveInfo unmakeMoveInfo) {
        if (castlingRightsWhite.canCastleKingSide() != unmakeMoveInfo.castlingRightsWhite().canCastleKingSide())
            zobristHash ^= Zobrist.getKingSideCastleWhiteKey();

        if (castlingRightsWhite.canCastleQueenSide() != unmakeMoveInfo.castlingRightsWhite().canCastleQueenSide())
            zobristHash ^= Zobrist.getQueenSideCastleWhiteKey();

        if (castlingRightsBlack.canCastleKingSide() != unmakeMoveInfo.castlingRightsBlack().canCastleKingSide())
            zobristHash ^= Zobrist.getKingSideCastleBlackKey();

        if (castlingRightsBlack.canCastleQueenSide() != unmakeMoveInfo.castlingRightsBlack().canCastleQueenSide())
            zobristHash ^= Zobrist.getQueenSideCastleBlackKey();

        castlingRightsWhite = unmakeMoveInfo.castlingRightsWhite();
        castlingRightsBlack = unmakeMoveInfo.castlingRightsBlack();
    }

    private void outdatePieces(Move move, UnmakeMoveInfo unmakeMoveInfo) {
        BoardPiece pieceToMove = pieceList[move.getTo().getBitBoardSquare()];
        Color color = (pieceToMove.isWhite()) ? Color.WHITE : Color.BLACK;

        switch (move) {
            case PromotionMove m:
                changePieceNormal(move.getTo(), move.getFrom(), unmakeMoveInfo.capturedPiece(), color.getPawn());
                break;
            case EnPassantMove m:
                updatePiecesEnPassantMove(m, true);
                break;
            case CastlingMove m:
                changePiecesCastlingMove(m, true);
                break;
            default:
                changePieceNormal(move.getTo(), move.getFrom(), unmakeMoveInfo.capturedPiece(), pieceToMove);
                break;
        }
        enPassantTargetSquare = unmakeMoveInfo.enPassantTargetSquare();
    }

    private void changePiecesCastlingMove(CastlingMove move, boolean undo) {
        Color color = (isWhiteToMove) ? Color.WHITE : Color.BLACK;

        BoardPosition kingFrom = undo ? move.getTo() : move.getFrom();
        BoardPosition kingTo = undo ? move.getFrom() : move.getTo();

        changePieceNormal(kingFrom, kingTo, null, color.getKing());

        boolean isKingSideCastling = move.getTo().getX() == Board.SIZE - 2;

        BoardPosition rookOriginalFrom = new BoardPosition(isKingSideCastling ? Board.SIZE - 1 : 0, move.getTo().getY());
        BoardPosition rookOriginalTo = new BoardPosition(isKingSideCastling ? Board.SIZE - 3 : 3, move.getTo().getY());

        BoardPosition rookFrom = undo ? rookOriginalTo : rookOriginalFrom;
        BoardPosition rookTo = undo ? rookOriginalFrom : rookOriginalTo;

        changePieceNormal(rookFrom, rookTo, null, color.getRook());
    }

    private void updatePiecesEnPassantMove(EnPassantMove move, boolean undo) {
        Color color = (isWhiteToMove) ? Color.WHITE : Color.BLACK;
        BoardPosition enPassantPiecePosition = getEnPassantPiecePosition();

        if (! undo) {
            changePieceNormal(move.getFrom(), move.getTo(), null, pieceList[move.getFrom().getBitBoardSquare()]);

            // remove pawn to be captured
            bitBoardState.clearBit(color.getOpponentPawn(), enPassantPiecePosition);
            bitBoardState.clearBit(color.getOpponentOccupancyBitboard(), enPassantPiecePosition);
            bitBoardState.clearBit(OccupancyBitboard.ALL_PIECES, enPassantPiecePosition);
            pieceList[enPassantPiecePosition.getBitBoardSquare()] = null;
        } else {
            changePieceNormal(move.getTo(), move.getFrom(), null, pieceList[move.getTo().getBitBoardSquare()]);

            // restore captured pawn
            bitBoardState.setBit(color.getOpponentPawn(), enPassantPiecePosition);
            bitBoardState.setBit(color.getOpponentOccupancyBitboard(), enPassantPiecePosition);
            bitBoardState.setBit(OccupancyBitboard.ALL_PIECES, enPassantPiecePosition);
            pieceList[enPassantPiecePosition.getBitBoardSquare()] = color.getOpponentPawn();
        }
        zobristHash ^= Zobrist.getPieceSquareKey(color.getOpponentPawn(), enPassantPiecePosition);
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
        zobristHash ^= Zobrist.getPieceSquareKey(pieceToMove, from);

        // un-capturing a piece (used for unmakeMove)
        if (pieceToReplaceWith != null) {
            bitBoardState.setBit(pieceToReplaceWith, from);
            bitBoardState.setBit(OccupancyBitboard.ALL_PIECES, from);
            bitBoardState.setBit(color.getOpponentOccupancyBitboard(), from);
            pieceList[from.getBitBoardSquare()] = pieceToReplaceWith;
            zobristHash ^= Zobrist.getPieceSquareKey(pieceToReplaceWith, from);
        }

        // capture piece if it exists
        if (pieceToCapture != null) {
            bitBoardState.clearBit(pieceToCapture, to);
            bitBoardState.clearBit(OccupancyBitboard.ALL_PIECES, to);
            bitBoardState.clearBit(color.getOpponentOccupancyBitboard(), to);
            zobristHash ^= Zobrist.getPieceSquareKey(pieceToCapture, to);
        }

        // place piece on to
        bitBoardState.setBit(pieceToBecome, to);
        bitBoardState.setBit(OccupancyBitboard.ALL_PIECES, to);
        bitBoardState.setBit(color.getOwnOccupancyBitboard(), to);
        pieceList[to.getBitBoardSquare()] = pieceToBecome;
        zobristHash ^= Zobrist.getPieceSquareKey(pieceToBecome, to);
    }

    public BoardPosition getEnPassantPiecePosition() {
        if (enPassantTargetSquare == null) return null;
        Color color = (isWhiteToMove) ? Color.WHITE : Color.BLACK;
        return new BoardPosition(enPassantTargetSquare.getX(), enPassantTargetSquare.getY() - color.getMovingDirection());
    }

    public BoardPiece getPieceAt(BoardPosition position) {
        return pieceList[position.getBitBoardSquare()];
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return isWhiteToMove == board.isWhiteToMove && halfmoveClock == board.halfmoveClock && fullmoveNumber == board.fullmoveNumber && zobristHash == board.zobristHash && Objects.equals(castlingRightsWhite, board.castlingRightsWhite) && Objects.equals(castlingRightsBlack, board.castlingRightsBlack) && Objects.equals(enPassantTargetSquare, board.enPassantTargetSquare) && Objects.equals(bitBoardState, board.bitBoardState) && Objects.deepEquals(pieceList, board.pieceList) && Objects.equals(whiteKingPosition, board.whiteKingPosition) && Objects.equals(blackKingPosition, board.blackKingPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isWhiteToMove, castlingRightsWhite, castlingRightsBlack, enPassantTargetSquare, halfmoveClock, fullmoveNumber, bitBoardState, Arrays.hashCode(pieceList), whiteKingPosition, blackKingPosition, zobristHash);
    }

    public boolean isWhiteToMove() {
        return isWhiteToMove;
    }

    public boolean isBlackToMove(){
        return ! isWhiteToMove;
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

    public int getFullmoveNumber() {
        return fullmoveNumber;
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

    public BoardPosition getWhiteKingPosition() {
        return whiteKingPosition;
    }

    public BoardPosition getBlackKingPosition() {
        return blackKingPosition;
    }

    public long getZobristHash() {
        return zobristHash;
    }
}