package chess.board;

public class Board {

    private final BoardPiece[][] boardPieces;
    private final PieceColor sideToMove;
    private final CastlingRights castlingRights;
    private final String possibleEnPassants;
    private int halfMoveClock;
    private int fullMoveNumber;
    // TODO enPassantTarget is not finished yet, will be in each BoardPiece

    public Board(BoardPiece[][] boardPieces, PieceColor sideToMove, CastlingRights castlingRights, String possibleEnPassants, int halfMoveClock, int fullMoveNumber) {
        this.boardPieces = boardPieces;
        this.sideToMove = sideToMove;
        this.castlingRights = castlingRights;
        this.possibleEnPassants = possibleEnPassants;
        this.halfMoveClock = halfMoveClock;
        this.fullMoveNumber = fullMoveNumber;
    }

    public static Board initializeDefaultBoard() {
        return initializeFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    public static Board initializeFromFen(String fen) {
        String[] fenParts = fen.split(" ");
        BoardPiece[][] boardPieces = initializeBoardPiecesFromFen(fenParts[0]);
        PieceColor sideToMove = initializeSideToMoveFromFen(fenParts[1]);
        CastlingRights castlingRights = CastlingRights.initializeByFen(fenParts[2]);
        String enPassantTarget = fenParts[3];
        int halfMoveClock = Integer.parseInt(fenParts[4]);
        int fullMoveNumber = Integer.parseInt(fenParts[5]);
        return new Board(boardPieces, sideToMove, castlingRights, enPassantTarget, halfMoveClock, fullMoveNumber);
    }

    private static BoardPiece[][] initializeBoardPiecesFromFen(String piecePlacements) {
        BoardPiece[][] boardPieces = new BoardPiece[8][8];
        String[] lines = piecePlacements.split("/");
        for (int i = lines.length -1; i >= 0; i--) {
            for (int j = 0; j < lines[i].length(); j++) {
                char currentChar =  lines[i].charAt(j);
                if (Character.isDigit(currentChar)) {
                    j += Integer.parseInt(String.valueOf(currentChar));
                    continue;
                }
                boardPieces[i][j] = BoardPiece.getByFen(currentChar);
            }
        }
        return boardPieces;
    }

    private static PieceColor initializeSideToMoveFromFen(String whiteToMove) {
        if (whiteToMove.equals("w")) {
            return PieceColor.WHITE;
        } else if (whiteToMove.equals("b")) {
            return PieceColor.BLACK;
        }
        return null;
    }

    public BoardPiece[][] getBoardPieces() {
        return boardPieces;
    }

    public PieceColor getSideToMove() {
        return sideToMove;
    }

    public CastlingRights getCastlingRights() {
        return castlingRights;
    }

    public int getHalfMoveClock() {
        return halfMoveClock;
    }

    public void setHalfMoveClock(int halfMoveClock) {
        this.halfMoveClock = halfMoveClock;
    }

    public int getFullMoveNumber() {
        return fullMoveNumber;
    }

    public void setFullMoveNumber(int fullMoveNumber) {
        this.fullMoveNumber = fullMoveNumber;
    }

    public String getPossibleEnPassants() {
        return possibleEnPassants;
    }
}