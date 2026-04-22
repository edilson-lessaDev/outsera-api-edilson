package br.com.outsera.api.integration;

import br.com.outsera.api.dto.ProducerIntervalDto;
import br.com.outsera.api.dto.ProducerIntervalResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "api.security.key=AFD0745X3459728"
)
class ProducerIntervalIntegrationJsonTest {

    private static final String API_KEY_HEADER = "X-API-KEY";
    private static final String API_KEY_VALUE = "AFD0745X3459728";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturnExpectedProducerIntervalsBasedOnDefaultCsvFile() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(API_KEY_HEADER, API_KEY_VALUE);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<ProducerIntervalResponse> response = restTemplate.exchange(
                "/producers/intervals",
                HttpMethod.GET,
                entity,
                ProducerIntervalResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        ProducerIntervalResponse expected = new ProducerIntervalResponse(
                Arrays.asList(new ProducerIntervalDto("Joel Silver", 1, 1990, 1991)),
                Arrays.asList(new ProducerIntervalDto("Matthew Vaughn", 13, 2002, 2015))
        );

        assertThat(response.getBody()).usingRecursiveComparison().isEqualTo(expected);
    }
}