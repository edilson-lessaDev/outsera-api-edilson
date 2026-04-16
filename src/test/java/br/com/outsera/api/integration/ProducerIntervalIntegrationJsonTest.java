package br.com.outsera.api.integration;

import br.com.outsera.api.dto.ProducerIntervalDto;
import br.com.outsera.api.dto.ProducerIntervalResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProducerIntervalIntegrationJsonTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturnExpectedProducerIntervalsBasedOnDefaultCsvFile() {
        ProducerIntervalResponse response =
                restTemplate.getForObject("/producers/intervals", ProducerIntervalResponse.class);

        assertThat(response).isNotNull();

        ProducerIntervalResponse expected = new ProducerIntervalResponse(
                Arrays.asList(new ProducerIntervalDto("Joel Silver", 1, 1990, 1991)),
                Arrays.asList(new ProducerIntervalDto("Matthew Vaughn", 13, 2002, 2015))
        );

        assertThat(response).usingRecursiveComparison().isEqualTo(expected);
    }
}