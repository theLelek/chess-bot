package chess.board;

public class Board {

    private final BoardPiece[][] board;

    public Board(BoardPiece[][] board) {
        this.board = board;
    }

    public static Board initializeBoardDefault() {
//        BoardPiece[] boardPieces = new BoardPiece[8][8];

        return new Board(new BoardPiece[8][8]);
    }

    public static Board initializeBoardFen(String fen) {
        BoardPiece[][] boardPieces = new BoardPiece[8][8];
        String[] lines = fen.split("/");
        for (int i = 0; i < lines.length; i++) {
            for (int j = 0; j < lines[i].length(); j++) {
                char currentChar =  lines[i].charAt(j);
                if (Character.isDigit(currentChar)) {
                    j += Integer.parseInt(String.valueOf(currentChar));
                }
                boardPieces[i][j] =
            }
        }
        return null;
    }

    public BoardPiece[][] getBoard() {
        return board;
    }
}
