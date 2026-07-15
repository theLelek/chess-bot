package dev.lelek;

import dev.lelek.api.Api;
import dev.lelek.chess.board.model.Board;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Api.start(args);
    }
}