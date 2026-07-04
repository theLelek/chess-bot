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

class Uci {

    private static final Logger log = LoggerFactory.getLogger(Uci.class);

    private static final String engineName = "chess-bot";
    private static final String author = "lelek";

    private Board board;

    static void start() {
        Uci uci = new Uci();
        while (true) {
            String command = Api.scanner.nextLine();
            String response = uci.handleCommand(command);
            System.out.println(response);
        }
    }
    
    String handleCommand(String command) {
        log.error("test");
        String out = "";
        String[] parts = command.split(" ");
        switch (parts[0]) {
            case "quit":
                System.exit(0);
                break;
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

    private static Board getPosition(String guiInput) {
        String[] parts = guiInput.split(" ");
        Board board = parts[1].equals("startpos") ? Board.initializeDefaultBoard() : Board.initializeFromFen(parts[1]);
        if (parts.length == 2) {
            return board;
        }
        for (int i = 3; i < parts.length; i++) {
            Move move = fromUciMoveFormat(board, parts[i]);
            board.makeMove(move);
        }
        return board;
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