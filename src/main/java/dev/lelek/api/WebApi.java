package dev.lelek.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper mapper = new ObjectMapper();

    static void start() {
        SpringApplication.run(WebApi.class);
    }

    @PostMapping("/chess")
    String chess(@RequestBody String message) throws JsonProcessingException {
        System.out.println(message);
        String response = null;
        switch (message) {
            case "legal-moves" -> response = mapper.writeValueAsString(LegalMoveFinder.getLegalMoves(uci.getBoard()));
            case "game-status" -> GameStatus.getGameStatus(uci.getBoard());
            default -> response = uci.handleCommand(message);
        }
        return response;
    }
}
