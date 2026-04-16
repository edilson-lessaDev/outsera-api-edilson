package br.com.outsera.api.service;

import br.com.outsera.api.dto.ProducerIntervalDto;
import br.com.outsera.api.dto.ProducerIntervalResponse;
import br.com.outsera.api.entity.Movie;
import br.com.outsera.api.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProducerIntervalService {

    private final MovieRepository movieRepository;

    public ProducerIntervalService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public ProducerIntervalResponse getProducerIntervals() {
        List<Movie> winners = movieRepository.findByWinnerTrue();

        Map<String, List<Integer>> producerWins = new HashMap<String, List<Integer>>();

        for (Movie movie : winners) {
            List<String> producers = splitProducers(movie.getProducers());

            for (String producer : producers) {
                if (!producerWins.containsKey(producer)) {
                    producerWins.put(producer, new ArrayList<Integer>());
                }
                producerWins.get(producer).add(movie.getYear());
            }
        }

        List<ProducerIntervalDto> minList = new ArrayList<ProducerIntervalDto>();
        List<ProducerIntervalDto> maxList = new ArrayList<ProducerIntervalDto>();

        Integer minInterval = null;
        Integer maxInterval = null;

        for (Map.Entry<String, List<Integer>> entry : producerWins.entrySet()) {
            String producer = entry.getKey();
            List<Integer> years = entry.getValue();

            if (years.size() < 2) {
                continue;
            }

            Collections.sort(years);

            for (int i = 1; i < years.size(); i++) {
                int previous = years.get(i - 1);
                int following = years.get(i);
                int interval = following - previous;

                ProducerIntervalDto dto =
                        new ProducerIntervalDto(producer, interval, previous, following);

                if (minInterval == null || interval < minInterval) {
                    minInterval = interval;
                    minList.clear();
                    minList.add(dto);
                } else if (interval == minInterval) {
                    minList.add(dto);
                }

                if (maxInterval == null || interval > maxInterval) {
                    maxInterval = interval;
                    maxList.clear();
                    maxList.add(dto);
                } else if (interval == maxInterval) {
                    maxList.add(dto);
                }
            }
        }

        if (minList.isEmpty() && maxList.isEmpty()) {
            return new ProducerIntervalResponse(Collections.<ProducerIntervalDto>emptyList(),
                    Collections.<ProducerIntervalDto>emptyList());
        }

        Collections.sort(minList, new Comparator<ProducerIntervalDto>() {
            @Override
            public int compare(ProducerIntervalDto o1, ProducerIntervalDto o2) {
                return o1.getProducer().compareTo(o2.getProducer());
            }
        });

        Collections.sort(maxList, new Comparator<ProducerIntervalDto>() {
            @Override
            public int compare(ProducerIntervalDto o1, ProducerIntervalDto o2) {
                return o1.getProducer().compareTo(o2.getProducer());
            }
        });

        return new ProducerIntervalResponse(minList, maxList);
    }

    private List<String> splitProducers(String producersText) {
        if (producersText == null || producersText.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String normalized = producersText.replace(" and ", ",");
        String[] parts = normalized.split(",");

        List<String> producers = new ArrayList<String>();
        for (String part : parts) {
            String producer = part.trim();
            if (!producer.isEmpty()) {
                producers.add(producer);
            }
        }
        return producers;
    }
}