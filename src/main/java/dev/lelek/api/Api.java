package dev.lelek.api;

import java.io.IOException;
import java.util.Scanner;

public class Api {

    static Scanner scanner = new Scanner(System.in); // todo use factory method design pattern

    public static void start(String[] args) throws IOException {
        if (args.length == 0) {
            startCli();
        } else if (args[0].equals("--web") || args[0].equals("-w")) {
            WebApi.start();
        } else {
            System.out.println("invalid flag");
        }
    }

    static void startCli() {
        String userInput = scanner.nextLine().trim();
        if (userInput.equals("play")) {
            PlayCli.start();
        } else {
            Uci.start();
        }
    }
}
