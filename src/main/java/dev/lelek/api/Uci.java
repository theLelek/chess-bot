package dev.lelek.api;

import dev.lelek.chess.BoardPosition;
import dev.lelek.chess.Move.CastlingMove;
import dev.lelek.chess.Move.EnPassantMove;
import dev.lelek.chess.Move.Move;
import dev.lelek.chess.Move.PromotionMove;
import dev.lelek.chess.BoardPiece;
import dev.lelek.chess.board.model.Board;
import dev.lelek.chess.search.MoveGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

class Uci {

    private static final Logger log = LoggerFactory.getLogger(Uci.class);

    private static final String engineName = "chess-bot";
    private static final String author = "lelek";

    private Board board;

    static void start() {
        Uci uci = new Uci();
        System.out.println(uci.handleCommand("uci"));
        while (true) {
            String command = Api.scanner.nextLine();
            if (command.equals("quit")) return;

            Runnable runnable = () -> {
                String response = uci.handleCommand(command);
                if (response != null) System.out.println(response);
            };
            new Thread(runnable).start();
        }
    }
    
    String handleCommand(String command) {
        String out = null;
        String[] parts = command.split(" ");
        switch (parts[0]) {
            case "uci":
                out = String.format("""
                        id name %s
                        id author %s
                        uciok""", engineName, author);
                break;
            case "position":
                board = getPosition(command);
                break;
            case "isready":
                out = "readyok";
                break;
            case "go":
                Move bestMove = MoveGenerator.generateMove(board, (long) 1000);
                out = "bestmove " + toUciMoveFormat(bestMove);
                break;
            default:
                log.warn("invalid or non supported uci command was entered: {}", command);
        }
        return out;
    }

    private static Board getPosition(String command) {
        Board board = extractStartPosition(command);
        List<Move> moves = extractMoves(board, command);
        for (Move move : moves) {
            board.makeMove(move);
        }
        return board;
    }

    private static Board extractStartPosition(String command) {
        if (command.startsWith("position startpos")) {
            return Board.initializeDefaultBoard();
        } else {
            return Board.initializeFromFen(extractFen(command));
        }
    }

    private static String extractFen(String command) {
        int start = "position fen ".length();
        int end = command.indexOf(" moves");
        return (end == -1) ? command.substring(start) : command.substring(start, end);
    }

    private static List<Move> extractMoves(Board board, String command) {
        List<Move> moves = new ArrayList<>();
        if (! command.contains("moves")) {
            return moves;
        }
        int start = command.indexOf("moves") + "moves".length() + 1;
        String[] movesPart = command.substring(start).split(" ");
        for (String move : movesPart) {
            moves.add(fromUciMoveFormat(board, move));
        }
        return moves;
    }

    static Move fromUciMoveFormat(Board board, String uciMove) {
        BoardPosition from = new BoardPosition(uciMove.substring(0, 2));
        BoardPosition to = new BoardPosition(uciMove.substring(2, 4));

        if (uciMove.length() > 4) {
            BoardPiece pieceToPromote = BoardPiece.fromFen(uciMove.charAt(4));
            return new PromotionMove(from, to, pieceToPromote);
        }

        if (board.getPieceAt(from).isKing() && Math.abs(from.x() - to.x()) == 2) {
            return new CastlingMove(from, to);
        }

        if (board.getPieceAt(from).isPawn() && board.getPieceAt(to) == null && from.x() != to.x()) {
            return new EnPassantMove(from, to);
        }

        return new Move(from, to);
    }

    private static String toUciMoveFormat(Move move) {
        String out = move.from().toString() + move.to().toString();
        if (move instanceof PromotionMove) {
            out += ((PromotionMove) move).getPromotionPiece().getFen();
        }
        return out;
    }

    public Board getBoard() {
        return board;
    }
}