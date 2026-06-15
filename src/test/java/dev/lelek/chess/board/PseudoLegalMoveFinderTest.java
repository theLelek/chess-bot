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

public class PseudoLegalMoveFinderTest {

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
//
//    private static <T extends Move> List<T> filter(List<Move> legalMoves, T type) {
//        List<T> moves = new ArrayList<>();
//        for (Move move : legalMoves) {
//            moves.add((T) move);
//        }
//        return moves;
//    }

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