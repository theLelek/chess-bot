package dev.lelek;

import dev.lelek.chess.Move.Move;
import dev.lelek.chess.board.model.Board;
import dev.lelek.chess.search.MoveGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

@SpringBootApplication
@RestController
@CrossOrigin(origins="*")
public class Test {

    public static void main(String[] args) {
        SpringApplication.run(Test.class, args);
    }

    @PostMapping("/test")
    public String chess(@RequestBody String message) throws ClassNotFoundException {
        return "Hello from Server, received message: " + message;
    }








}
