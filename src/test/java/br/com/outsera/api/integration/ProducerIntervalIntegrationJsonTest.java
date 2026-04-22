package br.com.outsera.api.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProducerIntervalIntegrationJsonTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnProducerIntervalsSuccessfully() throws Exception {
        mockMvc.perform(get("/producers/intervals")
                .header("X-API-KEY", "AFD0745X3459728"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn404WhenEndpointIsWrong() throws Exception {
        mockMvc.perform(get("/producers/interval")
                .header("X-API-KEY", "AFD0745X3459728"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn405WhenMethodIsInvalid() throws Exception {
        mockMvc.perform(post("/producers/intervals")
                .header("X-API-KEY", "AFD0745X3459728"))
                .andExpect(status().isMethodNotAllowed());
    }
}