package dev.lelek.api;

import dev.lelek.chess.BoardPosition;
import dev.lelek.chess.Move.Move;
import dev.lelek.chess.BoardPiece;
import dev.lelek.chess.board.model.Board;
import dev.lelek.chess.search.GameStatus;
import dev.lelek.chess.search.LegalMoveFinder;
import dev.lelek.chess.search.MoveGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PlayCli { // todo convert to instantiatable class like Uci

    private static final Logger log = LoggerFactory.getLogger(PlayCli.class);

    static void start() {
        log.info("started play cli");
        Board board = Board.initializeDefaultBoard();
        log.info("fen: {}", board.toFen());
        printBoard(board);
        while (GameStatus.getGameStatus(board) == GameStatus.ONGOING) {
            Move playerMove = getPlayerMove(board);
            board.makeMove(playerMove);
            log.info("player move: {}", playerMove);
            log.info("fen: {}", board.toFen());
            if (GameStatus.getGameStatus(board) != GameStatus.ONGOING) break;

            Move engineMove = MoveGenerator.generateMove(board, (long) 1000); // 1 second for engine move
            board.makeMove(engineMove);
            log.info("engine move: {}", engineMove);
            log.info("fen: {}", board.toFen());

            printBoard(board);
            System.out.println("engine move: " + engineMove);
            System.out.println("----------------------------");
        }
        GameStatus gameStatus = GameStatus.getGameStatus(board);
        System.out.println(GameStatus.getGameStatus(board));
        log.info("game over {}", gameStatus);
    }

    private static Move getPlayerMove(Board board) {
        Move playerMove = null;

        while (playerMove == null) {
            String userInput = null;
            try {
                System.out.println("enter your move (e.g. e2e4)");
                userInput = Api.scanner.nextLine().trim();
                playerMove = Uci.fromUciMoveFormat(board, userInput);
                if (! LegalMoveFinder.isMoveLegal(board, playerMove)) throw new Exception();
            } catch (Exception e) {
                System.out.println("invalid move");
                log.warn("invalid move was entered {}", userInput);
                playerMove = null;
            }
        }
        return playerMove;
    }

    private static void printBoard(Board board) {
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                BoardPosition position = new BoardPosition(j, i);
                BoardPiece piece = board.getPieceList()[position.getBitBoardSquare()];
                if (piece != null) {
                    System.out.print(piece.toFen() + " ");
                } else {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}