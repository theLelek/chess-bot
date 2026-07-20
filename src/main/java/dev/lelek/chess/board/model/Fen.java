package dev.lelek.chess.board.model;

import dev.lelek.chess.BoardPiece;
import dev.lelek.chess.BoardPosition;
import dev.lelek.chess.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class Fen {

    private static final Logger log = LoggerFactory.getLogger(Fen.class);

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
        long zobristHash = Zobrist.fromBoard(board);
        board.setZobristHash(zobristHash);
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

    public static String toFen(Board board) {
        String fen = piecePlacementsToFen(board);
        fen += " " + (board.isWhiteToMove() ? "w" : "b");
        fen += " " + castlingRightsToFen(board);
        fen += " " + (board.getEnPassantTargetSquare() == null ? "-" : board.getEnPassantTargetSquare().toFen());
        fen += " " + board.getHalfmoveClock();
        fen += " " + board.getFullmoveNumber();
        log.debug("generated fen: {}", fen);
        return fen;
    }

    private static String piecePlacementsToFen(Board board) {
        String[] piecePlacements = new String[Board.SIZE];
        Arrays.fill(piecePlacements, "");
        for (int i = 0; i < Board.SIZE; i++) {
            int column = 0;
            for (int j = 0; j < Board.SIZE; j++) {
                BoardPosition position = new BoardPosition(j, i);
                BoardPiece piece = board.getPieceAt(position);
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

    private static String castlingRightsToFen(Board board) {
        String castlingRights = "";
        castlingRights += board.getCastlingRightsWhite().toFen(Color.WHITE);
        castlingRights += board.getCastlingRightsBlack().toFen(Color.BLACK);
        if (castlingRights.isEmpty()) castlingRights = "-";
        return castlingRights;
    }
}