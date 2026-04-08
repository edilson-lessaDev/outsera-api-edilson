package br.com.outsera.api.service;

import br.com.outsera.api.dto.ProducerIntervalDto;
import br.com.outsera.api.dto.ProducerIntervalResponse;
import br.com.outsera.api.entity.Movie;
import br.com.outsera.api.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProducerIntervalService {

    private final MovieRepository movieRepository;

    public ProducerIntervalService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public ProducerIntervalResponse getProducerIntervals() {
        List<Movie> winners = movieRepository.findByWinnerTrue();

        Map<String, List<Integer>> producerWins = new HashMap<>();

        for (Movie movie : winners) {
            List<String> producers = splitProducers(movie.getProducers());

            for (String producer : producers) {
                producerWins
                        .computeIfAbsent(producer, key -> new ArrayList<>())
                        .add(movie.getYear());
            }
        }

        List<ProducerIntervalDto> intervals = new ArrayList<>();

        for (Map.Entry<String, List<Integer>> entry : producerWins.entrySet()) {
            String producer = entry.getKey();
            List<Integer> years = entry.getValue()
                    .stream()
                    .sorted()
                    .collect(Collectors.toList());

            if (years.size() < 2) {
                continue;
            }

            for (int i = 1; i < years.size(); i++) {
                int previous = years.get(i - 1);
                int following = years.get(i);
                int interval = following - previous;

                intervals.add(new ProducerIntervalDto(producer, interval, previous, following));
            }
        }

        if (intervals.isEmpty()) {
            return new ProducerIntervalResponse(Collections.emptyList(), Collections.emptyList());
        }

        int minInterval = intervals.stream()
                .mapToInt(ProducerIntervalDto::getInterval)
                .min()
                .orElse(0);

        int maxInterval = intervals.stream()
                .mapToInt(ProducerIntervalDto::getInterval)
                .max()
                .orElse(0);

        List<ProducerIntervalDto> minList = intervals.stream()
                .filter(i -> i.getInterval().equals(minInterval))
                .sorted(Comparator.comparing(ProducerIntervalDto::getProducer))
                .collect(Collectors.toList());

        List<ProducerIntervalDto> maxList = intervals.stream()
                .filter(i -> i.getInterval().equals(maxInterval))
                .sorted(Comparator.comparing(ProducerIntervalDto::getProducer))
                .collect(Collectors.toList());

        return new ProducerIntervalResponse(minList, maxList);
    }

    private List<String> splitProducers(String producersText) {
        if (producersText == null || producersText.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String normalized = producersText.replace(" and ", ",");
        String[] parts = normalized.split(",");

        List<String> producers = new ArrayList<>();
        for (String part : parts) {
            String producer = part.trim();
            if (!producer.isEmpty()) {
                producers.add(producer);
            }
        }
        return producers;
    }
}