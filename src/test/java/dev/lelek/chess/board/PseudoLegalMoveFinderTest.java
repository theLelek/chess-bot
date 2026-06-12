package dev.lelek.chess.board;

import dev.lelek.chess.BoardPosition;
import dev.lelek.chess.Color;
import dev.lelek.chess.Move.CastlingMove;
import dev.lelek.chess.Move.EnPassantMove;
import dev.lelek.chess.Move.Move;
import dev.lelek.chess.Move.PromotionMove;
import dev.lelek.chess.move_generation.PseudoLegalMoveFinder;
import dev.lelek.chess.board.model.Board;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;



public class PseudoLegalMoveFinderTest {

    // all perft results can be seen on https://www.chessprogramming.org/Perft_Results
    // todo add 2 kinds of tests (long tests with higher depth and short tests with lower depth)
    @Test
    void perftPosition1() {
        // currently takes 3.8-4.1 seconds for depth 5 on the default position
        Board board = Board.initializeDefaultBoard();
        Assertions.assertEquals(4_865_609,  perft(5, board, new Stack<>(), null));
    }

    @Test
    void perftPosition2() {
        /*
        the fen of position 2 uses a short version which is currently not supported,
        thus the - at the end had to be manually changed to 0 1
         */
        Board board = Board.initializeFromFen("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
        Assertions.assertEquals(97_862, perft(3, board, new Stack<>(), null));
    }

    @Test
    void perftPosition3() {
        Board board = Board.initializeFromFen("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1 ");
        Assertions.assertEquals(43238, perft(4, board, new Stack<>(), null));
    }

    @Test
    void perftPosition4() {
        Board board = Board.initializeFromFen("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1");
        Assertions.assertEquals(9467, perft(3, board, new Stack<>(), null));
    }

    @Test
    void perftPosition5() {
        Board board = Board.initializeFromFen("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8");
        Assertions.assertEquals(2103487, perft(4, board, new Stack<>(), null));
    }

    @Test
    void perftPosition6() {
        Board board = Board.initializeFromFen("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10 ");
        Assertions.assertEquals(3894594, perft(4, board, new Stack<>(), null));
    }

    private static int perft(int depth, Board board, Stack<UnmakeMoveInfo> unmakeMoveInfos, Move previousMove) {
        boolean isWhiteToMove = board.isWhiteToMove();
        List<Move> pseudoLegalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, isWhiteToMove);
        BoardPosition kingPosition = isWhiteToMove ? board.getBlackKingPosition() : board.getWhiteKingPosition();

        if (previousMove instanceof CastlingMove castlingMove) {
            BoardPosition positionToCheck = new BoardPosition(castlingMove.isKingSideCastling() ? 5 : 3, castlingMove.from().y());
            if (isPositionTargeted(pseudoLegalMoves, positionToCheck, kingPosition, previousMove.from())) {
                return 0;
            }
        } else if (isPositionTargeted(pseudoLegalMoves, kingPosition)) {
            return 0;
        }

        if (depth == 0) return 1;

        int count = 0;
        for (Move move : pseudoLegalMoves) {
            unmakeMoveInfos.push(new UnmakeMoveInfo(board, move));
            board.makeMove(move);
            int foo = perft(depth - 1, board, unmakeMoveInfos, move);
            count += foo;
            board.unmakeMove(move, unmakeMoveInfos.pop());
        }
        return count;
    }

    private static boolean isPositionTargeted(List<Move> moves, BoardPosition... positions) {
        for (Move move : moves) {
            for (BoardPosition position : positions) {
                if (move.to().equals(position)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Test
    @DisplayName("test find legal moves with black bishop at f8")
    public void testFindLegalMoves1() {
        Board board = Board.initializeFromFen("r1bqkbnr/pppp1ppp/2n5/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R w KQkq - 0 1");
        List<Move> legalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, false);
        List<Move> actual = filterByStartingPosition(legalMoves, new BoardPosition("f8"));
        List<Move> expected = Arrays.asList(new Move("f8", "e7"), new Move("f8", "d6"), new Move("f8", "c5"), new Move("f8", "b4"), new Move("f8", "a3"));
        Assertions.assertTrue(areLegalMovesEqual(actual, expected));
    }

    @Test
    @DisplayName("dev.lelek.Test find legal pawn move with black pawn on h7, can capture and move two fields foreward")
    public void testFindLegalPawnMoves2() {
        Board board = Board.initializeFromFen("rnbqkbnr/p1pppppp/6P1/8/1p6/8/PPPPPP1P/RNBQKBNR w KQkq - 0 1");
        List<Move> legalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, false);
        List<Move> actual = filterByStartingPosition(legalMoves, new BoardPosition(7, 1));
        List<Move> expected = Arrays.asList(new Move("h7", "h6"), new Move("h7", "h5"), new Move("h7", "g6"));
        Assertions.assertTrue(areLegalMovesEqual(expected, actual));
    }

    @Test
    @DisplayName("dev.lelek.Test white knight on b1 in starting position can only move to a3 and c3")
    public void testFindLegalKnightMoves1() {
        Board board = Board.initializeFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        List<Move> legalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, true);
        List<Move> actual = filterByStartingPosition(legalMoves, new BoardPosition(1, 7));
        List<Move> expected = Arrays.asList(new Move("b1", "c3"), new Move("b1", "a3"));
        Assertions.assertTrue(areLegalMovesEqual(expected, actual));
    }

    @Test
    @DisplayName("dev.lelek.Test white pawn on e3 blocked by black pawn on e4 has no legal moves")
    public void testFindLegalPawnMovesBlocked() {
        Board board = Board.initializeFromFen("4k3/8/8/8/4p3/4P3/8/4K3 w - - 0 1");
        List<Move> legalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, true);
        List<Move> actual = filterByStartingPosition(legalMoves, new BoardPosition(4, 5));
        Assertions.assertTrue(actual.isEmpty(), "Expected blocked pawn to have no legal moves");
    }

    @Test
    @DisplayName("dev.lelek.Test black bishop on f8 has no moves when blocked by own pawns")
    public void testFindLegalBishopMovesBlocked() {
        Board board = Board.initializeFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        List<Move> legalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, false);
        List<Move> actual = filterByStartingPosition(legalMoves, new BoardPosition(5, 0));
        Assertions.assertTrue(actual.isEmpty(), "Expected f8 bishop to have no legal moves in starting position");
    }

    @Test
    @DisplayName("dev.lelek.Test white pawn on e2 in starting position can move to e3 or e4")
    public void testFindLegalPawnMovesDoubleStep() {
        Board board = Board.initializeFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        List<Move> legalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, true);
        List<Move> actual = filterByStartingPosition(legalMoves, new BoardPosition(4, 6));
        List<Move> expected = Arrays.asList(new Move("e2", "e3"), new Move("e2", "e4"));
        Assertions.assertTrue(areLegalMovesEqual(expected, actual));
    }

    // castling
    @Test
    void getPseudoLegalCastlingRights() {
        Board board = Board.initializeDefaultBoard();
        List<Move> legalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, true);
        List<CastlingMove> actual = filterByCastlingMove(legalMoves);
        List<CastlingMove> expected = Arrays.asList();
        Assertions.assertTrue(areLegalMovesEqual(actual, expected));

    }

    @Test
    void white_canCastleBothSides_whenAllSquaresClear() {
        Board board = Board.initializeFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w KQkq - 0 1");
        List<Move> legalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, true);
        List<CastlingMove> actual = filterByCastlingMove(legalMoves);
        List<CastlingMove> expected = Arrays.asList(Color.WHITE.getCastlingMoveKingSide(), Color.WHITE.getCastlingMoveQueenSide());
        Assertions.assertTrue(areLegalMovesEqual(actual, expected));
    }

    @Test
    void white_cannotCastleEitherSide_whenSquaresBlocked() {
        Board board = Board.initializeDefaultBoard();
        List<Move> legalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, true);

        List<CastlingMove> actual = filterByCastlingMove(legalMoves);
        List<CastlingMove> expected = Arrays.asList();

        Assertions.assertTrue(areLegalMovesEqual(actual, expected));
    }

    @Test
    void black_canCastleBothSides_whenAllSquaresClear() {
        Board board = Board.initializeFromFen("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1");
        List<Move> legalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, false);

        List<CastlingMove> actual = filterByCastlingMove(legalMoves);
        List<CastlingMove> expected = Arrays.asList(
                Color.BLACK.getCastlingMoveKingSide(),
                Color.BLACK.getCastlingMoveQueenSide()
        );

        Assertions.assertTrue(areLegalMovesEqual(actual, expected));
    }

    // promotion
    @Test
    void getPromotionMoves_returnsEmpty_whenNoPawnsNearPromotion() {
        Board board = Board.initializeDefaultBoard();
        List<Move> legalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, true);
        List<PromotionMove> promotionMoves = filterByPromotionMove(legalMoves);
        Assertions.assertTrue(promotionMoves.isEmpty());
    }

    @Test
    void getPromotionMoves_returnsPromotionMove_whenWhitePawnOnSeventhRank() {
        Board board = Board.initializeFromFen("8/4P3/8/8/8/8/8/4K2k w - - 0 1");
        List<Move> pseudoLegalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, true);
        List<PromotionMove> promotionMoves = filterByPromotionMove(pseudoLegalMoves);
        List<PromotionMove> expected = Arrays.asList(new PromotionMove("e7", "e8", BoardPiece.WHITE_KNIGHT), new PromotionMove("e7", "e8", BoardPiece.WHITE_ROOK), new PromotionMove("e7", "e8", BoardPiece.WHITE_QUEEN), new PromotionMove("e7", "e8", BoardPiece.WHITE_BISHOP));
        Assertions.assertTrue(areLegalMovesEqual(promotionMoves, expected));
    }

    @Test
    void getPromotionMoves_returnsPromotionMove_whenBlackPawnOnSecondRank() {
        Board board = Board.initializeFromFen("4K2k/8/8/8/8/8/4p3/8 b - - 0 1");
        List<Move> pseudoLegalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, false);
        List<PromotionMove> promotionMoves = filterByPromotionMove(pseudoLegalMoves);
        List<PromotionMove> expected = Arrays.asList(new PromotionMove("e2", "e1", BoardPiece.BLACK_KNIGHT), new PromotionMove("e2", "e1", BoardPiece.BLACK_BISHOP), new PromotionMove("e2", "e1", BoardPiece.BLACK_ROOK), new PromotionMove("e2", "e1", BoardPiece.BLACK_QUEEN));
        Assertions.assertTrue(areLegalMovesEqual(promotionMoves, expected));
    }

    @Test
    void getPromotionMoves_returnsMultiplePromotionMoves_whenMultiplePawnsOnSeventhRank() {
        Board board = Board.initializeFromFen("8/2P1P3/8/8/8/8/8/4K2k w - - 0 1");
        List<Move> pseudoLegalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, true);
        List<PromotionMove> promotionMoves = filterByPromotionMove(pseudoLegalMoves);
        List<PromotionMove> expected = Arrays.asList(
                new PromotionMove("e7", "e8", BoardPiece.WHITE_QUEEN),
                new PromotionMove("e7", "e8", BoardPiece.WHITE_ROOK),
                new PromotionMove("e7", "e8", BoardPiece.WHITE_BISHOP),
                new PromotionMove("e7", "e8", BoardPiece.WHITE_KNIGHT),

                new PromotionMove("c7", "c8", BoardPiece.WHITE_QUEEN),
                new PromotionMove("c7", "c8", BoardPiece.WHITE_ROOK),
                new PromotionMove("c7", "c8", BoardPiece.WHITE_BISHOP),
                new PromotionMove("c7", "c8", BoardPiece.WHITE_KNIGHT)
        );
       Assertions.assertTrue(areLegalMovesEqual(promotionMoves, expected));
    }

    // en pessant
    @Test
    void white_canCaptureEnPassant_whenPawnMovesTwoSquaresPast() {
        Board board = Board.initializeFromFen("8/8/8/3pP3/8/8/8/4K2k w - d6 0 1");
        List<Move> legalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, true);
        List<EnPassantMove> enPassantMoves = filterByEnPassantMove(legalMoves);
        List<EnPassantMove> expected = Arrays.asList(new EnPassantMove("e5", "d6"));
        Assertions.assertTrue(areLegalMovesEqual(enPassantMoves, expected));
    }

    @Test
    void black_canCaptureEnPassant_whenPawnMovesTwoSquaresPast() {
        Board board = Board.initializeFromFen("8/8/8/8/3Pp3/8/8/4K2k b - d3 0 1");
        List<Move> legalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, false);
        List<EnPassantMove> enPassantMoves = filterByEnPassantMove(legalMoves);
        List<EnPassantMove> expected = Arrays.asList(new EnPassantMove("e4", "d3"));
        Assertions.assertTrue(areLegalMovesEqual(enPassantMoves, expected));
    }

    @Test
    void no_enPassant_whenPawnDidNotMoveTwoSquares() {
        Board board = Board.initializeFromFen("8/8/8/4p3/3P4/8/8/4K2k w - - 0 1");
        List<Move> legalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, true);
        List<EnPassantMove> enPassantMoves = filterByEnPassantMove(legalMoves);
        Assertions.assertTrue(enPassantMoves.isEmpty());
    }

    @Test
    void no_enPassant_whenPawnsAreNotAdjacent() {
        Board board = Board.initializeFromFen("8/8/8/8/2p1P3/8/8/4K2k w - c6 0 1");
        List<Move> legalMoves = PseudoLegalMoveFinder.getPseudoLegalMoves(board, true);
        List<EnPassantMove> enPassantMoves = filterByEnPassantMove(legalMoves);
        Assertions.assertTrue(enPassantMoves.isEmpty());
    }

    private boolean areLegalMovesEqual(List<? extends Move> moves1, List<? extends Move> moves2) {
        return moves1.size() == moves2.size() &&
                moves1.containsAll(moves2) &&
                moves2.containsAll(moves1);
    }

    private static List<Move> filterByStartingPosition(List<Move> legalMoves, BoardPosition boardPosition) {
        List<Move> moves = new ArrayList<>();
        for (Move move : legalMoves) {
            if (move.from().equals(boardPosition)) {
                moves.add(move);
            }
        }
        return moves;
    }

    // TODO replace with 1 method for every subclass
    private static List<CastlingMove> filterByCastlingMove(List<Move> legalMoves) {
        List<CastlingMove> castlingMoves = new ArrayList<>();
        for (Move move : legalMoves) {
            if (move instanceof CastlingMove) {
                castlingMoves.add((CastlingMove) move);
            }
        }
        return castlingMoves;
    }

    private static List<PromotionMove> filterByPromotionMove(List<Move> legalMoves) {
        List<PromotionMove> castlingMoves = new ArrayList<>();
        for (Move move : legalMoves) {
            if (move instanceof PromotionMove) {
                castlingMoves.add((PromotionMove) move);
            }
        }
        return castlingMoves;
    }

    private static List<EnPassantMove> filterByEnPassantMove(List<Move> legalMoves) {
        List<EnPassantMove> enPassantMoves = new ArrayList<>();
        for (Move move : legalMoves) {
            if (move instanceof EnPassantMove) {
                enPassantMoves.add((EnPassantMove) move);
            }
        }
        return enPassantMoves;
    }
}