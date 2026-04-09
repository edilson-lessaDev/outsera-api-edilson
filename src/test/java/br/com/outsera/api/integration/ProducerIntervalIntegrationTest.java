package br.com.outsera.api.integration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProducerIntervalIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnProducerIntervalsSuccessfully() throws Exception {

        mockMvc.perform(get("/producers/intervals"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.min").isArray())
                .andExpect(jsonPath("$.max").isArray());
    }

    @Test
    void shouldReturn404WhenEndpointIsWrong() throws Exception {

        mockMvc.perform(get("/producers/interval"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn405WhenMethodIsInvalid() throws Exception {

        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/producers/intervals")
        ).andExpect(status().isMethodNotAllowed());
    }
}