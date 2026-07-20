package dev.lelek.chess.board.model;

import dev.lelek.chess.BoardPosition;
import dev.lelek.chess.Move.CastlingMove;
import dev.lelek.chess.Move.EnPassantMove;
import dev.lelek.chess.Move.Move;
import dev.lelek.chess.Move.PromotionMove;
import dev.lelek.chess.BoardPiece;
import dev.lelek.chess.board.OccupancyBitboard;
import dev.lelek.chess.board.UnmakeMoveInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

class BoardTest {

    @Test
    void fromFen_defaultPosition() {
        Board board = Fen.initializeDefaultBoard();
        BoardPiece[][] expectedPieceList = new BoardPiece[][]{
                {BoardPiece.BLACK_ROOK, BoardPiece.BLACK_KNIGHT, BoardPiece.BLACK_BISHOP, BoardPiece.BLACK_QUEEN, BoardPiece.BLACK_KING, BoardPiece.BLACK_BISHOP, BoardPiece.BLACK_KNIGHT, BoardPiece.BLACK_ROOK},
                {BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN},
                {BoardPiece.WHITE_ROOK, BoardPiece.WHITE_KNIGHT, BoardPiece.WHITE_BISHOP, BoardPiece.WHITE_QUEEN, BoardPiece.WHITE_KING, BoardPiece.WHITE_BISHOP, BoardPiece.WHITE_KNIGHT, BoardPiece.WHITE_ROOK}
        };
        Assertions.assertArrayEquals(board2dToPieceList(expectedPieceList), board.getPieceList());

        BitBoardState bitBoardState = board.getBitBoardState();
        Assertions.assertEquals(65280, bitBoardState.getBitboard(BoardPiece.WHITE_PAWN));
        Assertions.assertEquals(129, bitBoardState.getBitboard(BoardPiece.WHITE_ROOK));
        Assertions.assertEquals(66, bitBoardState.getBitboard(BoardPiece.WHITE_KNIGHT));
        Assertions.assertEquals(36, bitBoardState.getBitboard(BoardPiece.WHITE_BISHOP));
        Assertions.assertEquals(8, bitBoardState.getBitboard(BoardPiece.WHITE_QUEEN));
        Assertions.assertEquals(16, bitBoardState.getBitboard(BoardPiece.WHITE_KING));

        Assertions.assertEquals(71776119061217280L, bitBoardState.getBitboard(BoardPiece.BLACK_PAWN));
        Assertions.assertEquals(-9151314442816847872L, bitBoardState.getBitboard(BoardPiece.BLACK_ROOK));
        Assertions.assertEquals(4755801206503243776L, bitBoardState.getBitboard(BoardPiece.BLACK_KNIGHT));
        Assertions.assertEquals(2594073385365405696L, bitBoardState.getBitboard(BoardPiece.BLACK_BISHOP));
        Assertions.assertEquals(576460752303423488L, bitBoardState.getBitboard(BoardPiece.BLACK_QUEEN));
        Assertions.assertEquals(1152921504606846976L, bitBoardState.getBitboard(BoardPiece.BLACK_KING));

        Assertions.assertTrue(board.getCastlingRightsWhite().canCastleKingSide());
        Assertions.assertTrue(board.getCastlingRightsWhite().canCastleQueenSide());
        Assertions.assertTrue(board.getCastlingRightsBlack().canCastleQueenSide());
        Assertions.assertTrue(board.getCastlingRightsBlack().canCastleKingSide());

        Assertions.assertEquals(0, board.getHalfmoveClock());
        Assertions.assertEquals(1, board.getFullmoveNumber());
        Assertions.assertNull(board.getEnPassantTargetSquare());
        Assertions.assertTrue(board.isWhiteToMove());

        Assertions.assertEquals(board.getWhiteKingPosition(), new BoardPosition("e1"));
        Assertions.assertEquals(board.getBlackKingPosition(), new BoardPosition("e8"));
    }

    @Test
    void fromFen_OnlyWhitePawns() {
        Board board = Fen.fromFen("8/8/8/8/8/8/PPPPPPPP/8 w KQkq - 0 1");
        BitBoardState bitBoardState = board.getBitBoardState();
        Assertions.assertEquals(65280, bitBoardState.getBitboard(BoardPiece.WHITE_PAWN));
        Assertions.assertEquals(65280, bitBoardState.getBitboard(OccupancyBitboard.WHITE_PIECES));
        Assertions.assertEquals(65280, bitBoardState.getBitboard(OccupancyBitboard.ALL_PIECES));

        BoardPiece[][] expectedPieceList = new BoardPiece[][]{
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN},
                {null, null, null, null, null, null, null, null},
        };
        Assertions.assertArrayEquals(board2dToPieceList(expectedPieceList), board.getPieceList());

        Assertions.assertTrue(board.getCastlingRightsWhite().canCastleKingSide());
        Assertions.assertTrue(board.getCastlingRightsWhite().canCastleQueenSide());
        Assertions.assertTrue(board.getCastlingRightsBlack().canCastleQueenSide());
        Assertions.assertTrue(board.getCastlingRightsBlack().canCastleKingSide());

        Assertions.assertEquals(0, board.getHalfmoveClock());
        Assertions.assertEquals(1, board.getFullmoveNumber());
        Assertions.assertNull(board.getEnPassantTargetSquare());
    }

    @Test
    void fromFen2() {
        Board board = Fen.fromFen("rnbqkbnr/ppp1pppp/8/3pP1B1/1P1P4/N2Q4/P1P2PPP/R3K2R w KQkq d6 0 4");
        BoardPiece[][] expectedPieceList = new BoardPiece[][]{
                {BoardPiece.BLACK_ROOK, BoardPiece.BLACK_KNIGHT, BoardPiece.BLACK_BISHOP, BoardPiece.BLACK_QUEEN, BoardPiece.BLACK_KING, BoardPiece.BLACK_BISHOP, BoardPiece.BLACK_KNIGHT, BoardPiece.BLACK_ROOK},
                {BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, null, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN, BoardPiece.BLACK_PAWN},
                {null, null, null, null, null, null, null, null},
                {null, null, null, BoardPiece.BLACK_PAWN, BoardPiece.WHITE_PAWN, null, BoardPiece.WHITE_BISHOP, null},
                {null, BoardPiece.WHITE_PAWN, null, BoardPiece.WHITE_PAWN, null, null, null, null},
                {BoardPiece.WHITE_KNIGHT, null, null, BoardPiece.WHITE_QUEEN, null, null, null, null},
                {BoardPiece.WHITE_PAWN, null, BoardPiece.WHITE_PAWN, null, null, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN, BoardPiece.WHITE_PAWN},
                {BoardPiece.WHITE_ROOK, null, null, null, BoardPiece.WHITE_KING, null, null, BoardPiece.WHITE_ROOK}
        };
        Assertions.assertArrayEquals(board2dToPieceList(expectedPieceList), board.getPieceList());
    }

    @Test
    void move_italienLine() {
        Board board = Fen.initializeDefaultBoard();
        Move move1 = new Move("e2", "e4");
        board.makeMove(move1);
        Assertions.assertEquals(Fen.fromFen("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1"), board);

        Move move2 = new Move("e7", "e5");
        board.makeMove(move2);
        Assertions.assertEquals(Fen.fromFen("rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w KQkq e6 0 2"), board);

        Move move3 = new Move("g1", "f3");
        board.makeMove(move3);
        Assertions.assertEquals(Fen.fromFen("rnbqkbnr/pppp1ppp/8/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2"), board);

        Move move4 = new Move("b8", "c6");
        board.makeMove(move4);
        Assertions.assertEquals(Fen.fromFen("r1bqkbnr/pppp1ppp/2n5/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R w KQkq - 2 3"), board);

        Move move5 = new Move("f1", "c4");
        board.makeMove(move5);
        Assertions.assertEquals(Fen.fromFen("r1bqkbnr/pppp1ppp/2n5/4p3/2B1P3/5N2/PPPP1PPP/RNBQK2R b KQkq - 3 3"), board);

        Move move6 = new Move("f8", "c5");
        board.makeMove(move6);
        Assertions.assertEquals(Fen.fromFen("r1bqk1nr/pppp1ppp/2n5/2b1p3/2B1P3/5N2/PPPP1PPP/RNBQK2R w KQkq - 4 4"), board);

        Move move7 = new CastlingMove("e1", "g1");
        board.makeMove(move7);
        Assertions.assertEquals(Fen.fromFen("r1bqk1nr/pppp1ppp/2n5/2b1p3/2B1P3/5N2/PPPP1PPP/RNBQ1RK1 b kq - 5 4"), board);

        Move move8 = new Move("g8", "f6");
        board.makeMove(move8);
        Assertions.assertEquals(Fen.fromFen("r1bqk2r/pppp1ppp/2n2n2/2b1p3/2B1P3/5N2/PPPP1PPP/RNBQ1RK1 w kq - 6 5"), board);

        Move move9 = new Move("b1", "c3");
        board.makeMove(move9);
        Assertions.assertEquals(Fen.fromFen("r1bqk2r/pppp1ppp/2n2n2/2b1p3/2B1P3/2N2N2/PPPP1PPP/R1BQ1RK1 b kq - 7 5"), board);


        board.unmakeMove(move9, new UnmakeMoveInfo(null, board.getCastlingRightsWhite(), board.getCastlingRightsBlack(), null, 6));
        Assertions.assertEquals(Fen.fromFen("r1bqk2r/pppp1ppp/2n2n2/2b1p3/2B1P3/5N2/PPPP1PPP/RNBQ1RK1 w kq - 6 5"), board);

        board.unmakeMove(move8, new UnmakeMoveInfo(null, board.getCastlingRightsWhite(), board.getCastlingRightsBlack(), null, 5));
        Assertions.assertEquals(Fen.fromFen("r1bqk1nr/pppp1ppp/2n5/2b1p3/2B1P3/5N2/PPPP1PPP/RNBQ1RK1 b kq - 5 4"), board);

        Board beforeMove7 = Fen.fromFen("r1bqk1nr/pppp1ppp/2n5/2b1p3/2B1P3/5N2/PPPP1PPP/RNBQK2R w KQkq - 4 4");
        board.unmakeMove(move7, new UnmakeMoveInfo(null, beforeMove7.getCastlingRightsWhite(), beforeMove7.getCastlingRightsBlack(), null, 4));
        Assertions.assertEquals(beforeMove7, board);

        board.unmakeMove(move6, new UnmakeMoveInfo(null, board.getCastlingRightsWhite(), board.getCastlingRightsBlack(), null, 3));
        Assertions.assertEquals(Fen.fromFen("r1bqkbnr/pppp1ppp/2n5/4p3/2B1P3/5N2/PPPP1PPP/RNBQK2R b KQkq - 3 3"), board);

        board.unmakeMove(move5, new UnmakeMoveInfo(null, board.getCastlingRightsWhite(), board.getCastlingRightsBlack(), null, 2));
        Assertions.assertEquals(Fen.fromFen("r1bqkbnr/pppp1ppp/2n5/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R w KQkq - 2 3"), board);

        board.unmakeMove(move4, new UnmakeMoveInfo(null, board.getCastlingRightsWhite(), board.getCastlingRightsBlack(), null, 1));
        Assertions.assertEquals(Fen.fromFen("rnbqkbnr/pppp1ppp/8/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2"), board);

        board.unmakeMove(move3, new UnmakeMoveInfo(null, board.getCastlingRightsWhite(), board.getCastlingRightsBlack(), new BoardPosition("e6"), 0));
        Assertions.assertEquals(Fen.fromFen("rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w KQkq e6 0 2"), board);

        board.unmakeMove(move2, new UnmakeMoveInfo(null, board.getCastlingRightsWhite(), board.getCastlingRightsBlack(), new BoardPosition("e3"), 0));
        Assertions.assertEquals(Fen.fromFen("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1"), board);

        board.unmakeMove(move1, new UnmakeMoveInfo(null, board.getCastlingRightsWhite(), board.getCastlingRightsBlack(), null, 0));
        Assertions.assertEquals(Fen.initializeDefaultBoard(), board);
    }

    @Test
    void move_castle() {
        // white kingside castling
        Board originalBoard1 = Fen.fromFen("4k3/8/8/8/8/8/8/R3K2R w KQ - 0 1");
        Board board1 = Fen.fromFen("4k3/8/8/8/8/8/8/R3K2R w KQ - 0 1");
        Move move1 = new CastlingMove("e1", "g1");
        UnmakeMoveInfo unmakeMoveInfo = UnmakeMoveInfo.from(board1, move1);
        board1.makeMove(move1);
        Assertions.assertEquals(Fen.fromFen("4k3/8/8/8/8/8/8/R4RK1 b - - 1 1"), board1);
        board1.unmakeMove(move1, unmakeMoveInfo);
        Assertions.assertEquals(originalBoard1, board1);

        // white queenside castling
        Board originalBoard2 = Fen.fromFen("4k3/8/8/8/8/8/8/R3K2R w KQ - 0 1");
        Board board2 = Fen.fromFen("4k3/8/8/8/8/8/8/R3K2R w KQ - 0 1");
        Move move2 = new CastlingMove("e1", "c1");
        board2.makeMove(move2);
        Assertions.assertEquals(Fen.fromFen("4k3/8/8/8/8/8/8/2KR3R b - - 1 1"), board2);
        board2.unmakeMove(move2, new UnmakeMoveInfo(null, originalBoard2.getCastlingRightsWhite(), originalBoard2.getCastlingRightsBlack(), null, 0));
        Assertions.assertEquals(originalBoard2, board2);

        // black kingside castling
        Board originalBoard3 = Fen.fromFen("r3k2r/8/8/8/8/8/8/4K3 b kq - 0 1");
        Board board3 = Fen.fromFen("r3k2r/8/8/8/8/8/8/4K3 b kq - 0 1");
        Move move3 = new CastlingMove("e8", "g8");
        board3.makeMove(move3);
        Assertions.assertEquals(Fen.fromFen("r4rk1/8/8/8/8/8/8/4K3 w - - 1 2"), board3);
        board3.unmakeMove(move3, new UnmakeMoveInfo(null, originalBoard3.getCastlingRightsWhite(), originalBoard3.getCastlingRightsBlack(), null, 0));
        Assertions.assertEquals(originalBoard3, board3);

        // black queenside castling
        Board originalBoard4 = Fen.fromFen("r3k2r/8/8/8/8/8/8/4K3 b kq - 0 1");
        Board board4 = Fen.fromFen("r3k2r/8/8/8/8/8/8/4K3 b kq - 0 1");
        Move move4 = new CastlingMove("e8", "c8");
        board4.makeMove(move4);
        Assertions.assertEquals(Fen.fromFen("2kr3r/8/8/8/8/8/8/4K3 w - - 1 2"), board4);
        board4.unmakeMove(move4, new UnmakeMoveInfo(null, originalBoard4.getCastlingRightsWhite(), originalBoard4.getCastlingRightsBlack(), null, 0));
        Assertions.assertEquals(originalBoard4, board4);

    }

    @Test
    void move_promotion() {
        // white pawn promoting
        Board board1 = Fen.fromFen("8/P7/8/8/8/8/8/k6K w - - 0 1");
        Move move1 = new PromotionMove("a7", "a8", BoardPiece.WHITE_ROOK);
        board1.makeMove(move1);
        Assertions.assertEquals(Fen.fromFen("R7/8/8/8/8/8/8/k6K b - - 0 1"), board1);
        board1.unmakeMove(move1, new UnmakeMoveInfo(null, board1.getCastlingRightsWhite(), board1.getCastlingRightsBlack(), null, 0));
        Assertions.assertEquals(Fen.fromFen("8/P7/8/8/8/8/8/k6K w - - 0 1"), board1);

        // white pawn promoting while capturing black rook
        Board board2 = Fen.fromFen("4r3/5P2/8/8/8/8/8/k6K w - - 0 1");
        Move move2 = new PromotionMove("f7", "e8", BoardPiece.WHITE_QUEEN);
        board2.makeMove(move2);
        Assertions.assertEquals(Fen.fromFen("4Q3/8/8/8/8/8/8/k6K b - - 0 1"), board2);
        board2.unmakeMove(move2, new UnmakeMoveInfo(BoardPiece.BLACK_ROOK, board2.getCastlingRightsWhite(), board2.getCastlingRightsBlack(), null, 0));
        Assertions.assertEquals(Fen.fromFen("4r3/5P2/8/8/8/8/8/k6K w - - 0 1"), board2);

        // black pawn promoting
        Board board3 = Fen.fromFen("3r4/8/8/3k4/8/6K1/3p4/R7 b - - 0 1");
        Move move3 = new PromotionMove("d2", "d1", BoardPiece.BLACK_KNIGHT);
        board3.makeMove(move3);
        Assertions.assertEquals(Fen.fromFen("3r4/8/8/3k4/8/6K1/8/R2n4 w - - 0 2"), board3);
        board3.unmakeMove(move3, new UnmakeMoveInfo(null, board3.getCastlingRightsWhite(), board3.getCastlingRightsBlack(), null, 0));
        Assertions.assertEquals(Fen.fromFen("3r4/8/8/3k4/8/6K1/3p4/R7 b - - 0 1"), board3);

        // black pawn promoting while capturing white bishop
        Board board4 = Fen.fromFen("3r4/8/8/3k4/8/8/3p4/RKBn4 b - - 0 2");
        Move move4 = new PromotionMove("d2", "c1", BoardPiece.BLACK_BISHOP);
        board4.makeMove(move4);
        Assertions.assertEquals(Fen.fromFen("3r4/8/8/3k4/8/8/8/RKbn4 w - - 0 3"), board4);
        board4.unmakeMove(move4, new UnmakeMoveInfo(BoardPiece.WHITE_BISHOP, board4.getCastlingRightsWhite(), board4.getCastlingRightsBlack(), null, 0));
        Assertions.assertEquals(Fen.fromFen("3r4/8/8/3k4/8/8/3p4/RKBn4 b - - 0 2"), board4);

    }

    @Test
    void move_enPassant() {
        // basic en passant white to move
        Board board1 = Fen.fromFen("8/7k/8/3pP3/8/8/8/RK6 w - d6 0 4");
        Move move1 = new EnPassantMove("e5", "d6");
        board1.makeMove(move1);
        Assertions.assertEquals(Fen.fromFen("8/7k/3P4/8/8/8/8/RK6 b - - 0 4"), board1);
        board1.unmakeMove(move1, new UnmakeMoveInfo(null, board1.getCastlingRightsWhite(), board1.getCastlingRightsBlack(), new BoardPosition("d6"), 0));
        Assertions.assertEquals(Fen.fromFen("8/7k/8/3pP3/8/8/8/RK6 w - d6 0 4"), board1);

        // basic en passant black to move
        Board board2 = Fen.fromFen("8/7k/8/8/3Pp3/8/8/RK6 b - d3 0 4");
        Move move2 = new EnPassantMove("e4", "d3");
        board2.makeMove(move2);
        Assertions.assertEquals(Fen.fromFen("8/7k/8/8/8/3p4/8/RK6 w - - 0 5"), board2);
        board2.unmakeMove(move2, new UnmakeMoveInfo(null, board2.getCastlingRightsWhite(), board2.getCastlingRightsBlack(), new BoardPosition("d3"), 0));
        Assertions.assertEquals(Fen.fromFen("8/7k/8/8/3Pp3/8/8/RK6 b - d3 0 4"), board2);
    }

    @Test
    void move_general() {
        Board board = Fen.initializeDefaultBoard();
        System.out.println(board.getZobristHash());
        Move move = new Move("a2", "a4");
        UnmakeMoveInfo unmakeMoveInfo = UnmakeMoveInfo.from(board, move);
        board.makeMove(move);
        System.out.println(board.getZobristHash());
        Assertions.assertEquals(Fen.fromFen("rnbqkbnr/pppppppp/8/8/P7/8/1PPPPPPP/RNBQKBNR b KQkq a3 0 1"), board);
        board.unmakeMove(move, unmakeMoveInfo);
        Assertions.assertEquals(Fen.initializeDefaultBoard(), board);
    }

    private static BoardPiece[] board2dToPieceList(BoardPiece[][] board){
        ArrayList<BoardPiece> result = new ArrayList<>();
        for (int i = board.length - 1; i >= 0; i--) {
            result.addAll(Arrays.asList(board[i]));
        }
        return result.toArray(BoardPiece[]::new);
    }

    @Test
    void toFen_fromValidFen_returnsOriginalFen() {
        String fen1 = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        Board board1 = Fen.fromFen(fen1);
        Assertions.assertEquals(fen1, Fen.toFen(board1));

        String fen2 = "2kr3r/pppq1ppp/2nbpn2/1B1p4/3PP3/2P2Q2/PP1N1PPP/R1B1R1K1 b - - 0 11";
        Board board2 = Fen.fromFen(fen2);
        Assertions.assertEquals(fen2, Fen.toFen(board2));

        String fen3 = "rn1qkbnr/ppp1pppp/3p4/8/2P1P1b1/3B1N2/PP1P1PPP/RNBQ1RK1 w q - 0 1";
        Board board3 = Fen.fromFen(fen3);
        Assertions.assertEquals(fen3, Fen.toFen(board3));

        String fen4 = "rn2k1r1/pp2pp1p/3p2p1/P4bn1/2pP4/2N1NB2/1PP1PP2/2B1K1RR b Kq d3 0 11";
        Board board4 = Fen.fromFen(fen4);
        Assertions.assertEquals(fen4, Fen.toFen(board4));
    }
}