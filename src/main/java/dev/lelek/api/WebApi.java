package dev.lelek.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lelek.chess.search.GameStatus;
import dev.lelek.chess.search.LegalMoveFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@CrossOrigin("*")
class WebApi {

    private static final Logger log = LoggerFactory.getLogger(WebApi.class);

    private final static Uci uci = new Uci();


    static void start() {
        log.info("started web api");
        SpringApplication.run(WebApi.class);
    }

    @PostMapping("/chess")
    private String chess(@RequestBody String message) throws JsonProcessingException {
        log.info("web api command: {}", message);
        ObjectMapper mapper = new ObjectMapper();
        String response;
        switch (message) {
            case "legal-moves" -> response = mapper.writeValueAsString(LegalMoveFinder.getLegalMoves(uci.getBoard()));
            case "game-status" -> response = GameStatus.getGameStatus(uci.getBoard()).toString();
            case "fen" -> response = uci.getBoard().toFen();
            default -> response = uci.handleCommand(message);
        }
        log.info("web api response: {}", response);
        return response;
    }
}
