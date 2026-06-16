package dev.lelek.api;

import dev.lelek.chess.board.model.Board;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Uci {

    private static final Logger log = LoggerFactory.getLogger(Uci.class);

    private static final String engineName = "chess-bot";
    private static final String author = "lelek";

    public static void start() {
        System.out.println("id name " + engineName);
        System.out.println("id author " + author);
        System.out.println("uciok");

        Board board;

        while (true) {
            String guiInput = Cli.scanner.nextLine().trim();
            if (guiInput.equals("quit"))
                System.exit(0); // todo not sure if thats the best way to stop the engine
            String[] parts = guiInput.split(" ");
            switch (parts[0]) {
                case "position":
                    board = getPosition(guiInput);
                   break;
                case "isready":
                    System.out.println("readyok");
                    break;
                case "go":
                    break;
                default:
                    log.warn("invalid or non supported uci command was entered: " + guiInput);
            }
        }
    }

    private static Board getPosition(String guiInput) {
        String[] parts = guiInput.split(" ");
        String fen = parts[1];
        return fen.equals("startpos") ? Board.initializeDefaultBoard() : Board.initializeFromFen(fen);
        // todo implement making moves which can be additionaly sent by the gui
    }
}
