package dev.lelek.api;

import java.util.Scanner;

public class Cli {

    static final Scanner scanner = new Scanner(System.in);

    private static final String engineName = "chess-engine";
    private static final String author = "lelek";

    public static void start() {
        while (true) {
            String userInput = scanner.nextLine().trim();
            if (userInput.equals("quit")) break;

            switch (userInput) {
                case "uci":
                    handleUciCommand();
                   break;

                default:
                    System.out.println("Invalid command");
            }
        }
    }

    private static void handleUciCommand() {
        System.out.println("id name " + engineName);
        System.out.println("id author " + author);
        System.out.println("uciok");
        Uci.start();
    }
}
