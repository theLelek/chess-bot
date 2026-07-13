package dev.lelek.api;

import java.util.Scanner;

public class Api {

    static Scanner scanner = new Scanner(System.in); // todo use factory method design pattern or singleton

    public static void start(String[] args) {
        if (args.length == 0) {
            startCli();
        } else if (args[0].equals("--web") || args[0].equals("-w")) {
            WebApi.start();
        } else {
            System.out.println("invalid flag");
        }
    }

    private static void startCli() {
        String userInput = scanner.nextLine().trim();
        if (userInput.equals("play")) {
            PlayCli.start();
        } else if (userInput.equals("uci")){
            Uci.start();
        } else {
            System.out.println("invalid input");
        }
    }
}
