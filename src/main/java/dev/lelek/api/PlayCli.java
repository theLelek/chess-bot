package dev.lelek.api;

import dev.lelek.chess.BoardPosition;
import dev.lelek.chess.Move.Move;
import dev.lelek.chess.BoardPiece;
import dev.lelek.chess.board.model.Board;
import dev.lelek.chess.search.GameStatus;
import dev.lelek.chess.search.LegalMoveFinder;
import dev.lelek.chess.search.MoveGenerator;

class PlayCli { // todo convert to instantiatable class like Uci

    static void start() {
        Board board = Board.initializeDefaultBoard();
        printBoard(board);
        while (GameStatus.getGameStatus(board) == GameStatus.ONGOING) {
            Move playerMove = getPlayerMove(board);
            board.makeMove(playerMove);
            if (GameStatus.getGameStatus(board) != GameStatus.ONGOING) break;

            Move engineMove = MoveGenerator.generateMove(board, (long) 1000); // 1 second for engine move
            board.makeMove(engineMove);

            printBoard(board);
            System.out.println("engine move: " + engineMove);
            System.out.println("----------------------------");
        }
        System.out.println(GameStatus.getGameStatus(board));
    }

    private static Move getPlayerMove(Board board) {
        Move playerMove = null;

        while (playerMove == null) {
            try {
                System.out.println("enter your move (e.g. e2e4)");
                playerMove = Uci.fromUciMoveFormat(board, Api.scanner.nextLine().trim());
                if (! LegalMoveFinder.isMoveLegal(board, playerMove)) throw new Exception();
            } catch (Exception e) {
                System.out.println("invalid move");
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
                    System.out.print(piece.getFen() + " ");
                } else {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}