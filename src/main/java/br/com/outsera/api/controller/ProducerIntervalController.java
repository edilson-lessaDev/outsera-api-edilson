package br.com.outsera.api.controller;

import br.com.outsera.api.dto.ProducerIntervalResponse;
import br.com.outsera.api.service.ProducerIntervalService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerIntervalController {

    private final ProducerIntervalService producerIntervalService;

    public ProducerIntervalController(ProducerIntervalService producerIntervalService) {
        this.producerIntervalService = producerIntervalService;
    }

    @GetMapping("/producers/intervals")
    public ProducerIntervalResponse getProducerIntervals() {
        return producerIntervalService.getProducerIntervals();
    }
}