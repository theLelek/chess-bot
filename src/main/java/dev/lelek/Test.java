package dev.lelek;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@CrossOrigin(origins="*")
public class Test {

    public static void main(String[] args) {
        SpringApplication.run(Test.class, args);
    }

    @PostMapping("/chess")
    public String chess(@RequestBody String message) throws ClassNotFoundException {
        return "Hello from Server, received message: " + message;
    }








}