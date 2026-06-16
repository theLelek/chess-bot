package dev.lelek.api;

import java.util.Scanner;

public class Cli {

    static final Scanner scanner = new Scanner(System.in);

    public static void start() {
        while (true) {
            String userInput = scanner.nextLine().trim();
            if (userInput.equals("quit")) break;

            switch (userInput) {
                case "uci":
                    Uci.start();
                   break;
                case "play":
                    PlayCli.start();
                    break;
                default:
                    System.out.println("Invalid command");
            }
        }
    }
}
