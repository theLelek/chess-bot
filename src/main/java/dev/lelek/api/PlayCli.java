package dev.lelek.api;

import dev.lelek.chess.BoardPosition;
import dev.lelek.chess.Move.CastlingMove;
import dev.lelek.chess.Move.EnPassantMove;
import dev.lelek.chess.Move.Move;
import dev.lelek.chess.Move.PromotionMove;
import dev.lelek.chess.board.BoardPiece;
import dev.lelek.chess.board.model.Board;
import dev.lelek.chess.move_generation.GameStatus;
import dev.lelek.chess.move_generation.MoveGenerator;

public class PlayCli {

    public static void start() {
        Board board = Board.initializeDefaultBoard();
        printBoard(board);
        while (GameStatus.getGameStatus(board) == GameStatus.ONGOING) {
            Move playerMove = getPlayerMove(board);
            board.makeMove(playerMove);

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
                playerMove = initializeMove();
                if (! MoveGenerator.isMoveLegal(board, playerMove)) throw new Exception();
            } catch (Exception e) {
                System.out.println("invalid move");
                playerMove = null;
            }
        }
        return playerMove;
    }

    private static Move initializeMove() {
        Move move;
        System.out.println("enter your move type");
        System.out.println("normal move (0), castling move (1), en passant move (2) promotion move (3)");
        int moveType = Integer.parseInt(Cli.scanner.nextLine().trim());
        Move normalMove = initializeNormalMove();

        switch (moveType) {
            case 0:
                move = normalMove;
                break;
            case 1:
                move = (CastlingMove) normalMove;
                break;
            case 2:
                move = (EnPassantMove) normalMove;
                break;
            case 3:
                System.out.println("enter the piece to promote to (e.g. WHITE_KING");
                move = new PromotionMove(normalMove.from(), normalMove.to(), BoardPiece.valueOf(Cli.scanner.nextLine().trim()));
            default:
                throw new IllegalArgumentException("invalid move type");
        }
        return move;
    }

    private static Move initializeNormalMove() {
        System.out.println("enter the move (e.g. e2 e4)");
        String[] parts = Cli.scanner.nextLine().trim().split(" ");
        return new Move(parts[0], parts[1]);
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