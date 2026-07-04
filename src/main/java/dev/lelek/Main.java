package dev.lelek;

import dev.lelek.api.Api;
import org.springframework.boot.SpringApplication;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Api.start(args);
    }
}