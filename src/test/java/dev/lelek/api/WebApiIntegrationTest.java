package dev.lelek.api;

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