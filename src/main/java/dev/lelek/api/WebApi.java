package dev.lelek.api;

import dev.lelek.chess.search.GameStatus;
import dev.lelek.chess.search.LegalMoveFinder;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@SpringBootApplication
@CrossOrigin("*")
class WebApi {

    private final static Uci uci = new Uci();

    static void start() throws IOException {
        System.out.println("hello");
        SpringApplication.run(WebApi.class);
    }



    @PostMapping("/chess")
    String chess(@RequestBody String message) throws ClassNotFoundException, IOException {
        System.out.println(message);
        String response;
        switch (message) {
            case "get-legal-moves" -> response = LegalMoveFinder.getLegalMoves(uci.getBoard()).toString();
            default -> response = uci.handleCommand(message);
        }
        response += " " + GameStatus.getGameStatus(uci.getBoard());
        return response;
    }
}
