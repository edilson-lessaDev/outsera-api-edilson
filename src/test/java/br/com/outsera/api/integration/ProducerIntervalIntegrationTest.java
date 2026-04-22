package br.com.outsera.api.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "api.security.key=AFD0745X3459728")
@AutoConfigureMockMvc
class ProducerIntervalIntegrationTest {

    private static final String API_KEY_HEADER = "X-API-KEY";
    private static final String API_KEY_VALUE = "AFD0745X3459728";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnProducerIntervalsSuccessfully() throws Exception {
        mockMvc.perform(get("/producers/intervals")
                .header(API_KEY_HEADER, API_KEY_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn404WhenEndpointIsWrong() throws Exception {
        mockMvc.perform(get("/producers/interval")
                .header(API_KEY_HEADER, API_KEY_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn405WhenMethodIsInvalid() throws Exception {
        mockMvc.perform(post("/producers/intervals")
                .header(API_KEY_HEADER, API_KEY_VALUE))
                .andExpect(status().isMethodNotAllowed());
    }
}