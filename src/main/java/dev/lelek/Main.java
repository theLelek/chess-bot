package dev.lelek;

import dev.lelek.api.Uci;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

@RestController
@SpringBootApplication
@CrossOrigin("*")
public class Main {
    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);

        System.out.println("Starting uci");

        Uci.start();

        System.out.println("Server started");
    }

    @PostMapping("/chess")
    public String chess(@RequestBody String message) throws ClassNotFoundException {
        System.out.println("Connected");
        System.out.println(message);
        PrintStream originalOutput = System.out;

        ByteArrayOutputStream response = new ByteArrayOutputStream();

        PrintStream console = new PrintStream(response);

        InputStream originalIn = System.in;

        try {
            System.setOut(console);
            System.setIn(new ByteArrayInputStream(message.getBytes()));
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            System.setOut(originalOutput);
            System.setIn(originalIn);
        }


        System.out.println("Server Response: " + response.toString());
        return response.toString();
    }
}