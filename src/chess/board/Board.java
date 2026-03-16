package chess.board;

public class Board {

    private final BoardPiece[][] board;

    public Board(BoardPiece[][] board) {
        this.board = board;
    }

    public static Board initializeDefaultBoard() {
        return initializeBoardByFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
    }

    public static Board initializeBoardByFen(String fen) {
        BoardPiece[][] boardPieces = new BoardPiece[8][8];
        String[] lines = fen.split("/");
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
        return new Board(boardPieces);
    }

    public BoardPiece[][] getBoard() {
        return board;
    }
}
