package dev.lelek;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@CrossOrigin("*")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        /*
        Bot bot = new Bot(board)
        Board board = new Board();

        while() {
            board.update(api.getUserMove)
            Move move = Bot.getMove()
            print(move)
        }

         */
    }

    @PostMapping("/chess")
    public String chess(@RequestBody String message) throws ClassNotFoundException {
        //handle message
        return null;
    }
}