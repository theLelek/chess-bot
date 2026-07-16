package dev.lelek.api;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class WebApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void legalMovesEndpointReturnsOk() throws Exception {
        sendCommand("uci");
        sendCommand("position startpos");
        Assertions.assertEquals("readyok", sendCommand("isready"));
        Assertions.assertEquals("ONGOING", sendCommand("game-status"));
        Assertions.assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", sendCommand("fen"));
    }

    @Test
    void fenCommandTest() throws Exception {
        sendCommand("uci");
        String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        sendCommand("position fen " + fen);
        String response = sendCommand("fen");
        Assertions.assertEquals(fen, response);
    }
    
    @Test
    void positionFenCommandWithMoves() throws Exception {
        sendCommand("uci");
        String position = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 moves e2e4 g8f6 e4e5";
        sendCommand("position fen " + position);
        Assertions.assertEquals("rnbqkb1r/pppppppp/5n2/4P3/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 2", sendCommand("fen"));
    }

    private String sendCommand(String command) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/chess")
                        .content(command)
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
}