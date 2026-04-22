package br.com.outsera.api.service;

import br.com.outsera.api.entity.Movie;
import br.com.outsera.api.repository.MovieRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


@Component
@Order(2)
public class CsvDataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(CsvDataLoader.class);
    private static final String CSV_FILE_NAME = "Movielist.csv";

    private final MovieRepository movieRepository;

    public CsvDataLoader(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (databaseAlreadyLoaded()) {
            log.info("Tabela já está com conteúdo,");
            return;
        }

        List<Movie> movies = loadMoviesFromCsv();
        movieRepository.saveAll(movies);

        log.info("Carga finalizada. Registros importados: {}", movieRepository.count());
    }

    private boolean databaseAlreadyLoaded() {
        return movieRepository.count() > 0;
    }

    private List<Movie> loadMoviesFromCsv() throws Exception {
        ClassPathResource resource = new ClassPathResource(CSV_FILE_NAME);
        List<Movie> movies = new ArrayList<>();

        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
             CSVParser csvParser = new CSVParser(reader,
                     CSVFormat.DEFAULT
                             .withFirstRecordAsHeader()
                             .withIgnoreHeaderCase()
                             .withTrim()
                             .withDelimiter(';'))) {

            for (CSVRecord record : csvParser) {
                movies.add(toMovie(record));
            }
        }

        return movies;
    }

    private Movie toMovie(CSVRecord record) {
        Integer year = Integer.parseInt(record.get("year"));
        String title = record.get("title");
        String studios = record.get("studios");
        String producers = record.get("producers");
        Boolean winner = isWinner(record);

        return new Movie(year, title, studios, producers, winner);
    }

    private Boolean isWinner(CSVRecord record) {
        if (!record.isMapped("winner")) {
            return false;
        }

        String winnerValue = record.get("winner");
        return "yes".equalsIgnoreCase(winnerValue);
    }
}