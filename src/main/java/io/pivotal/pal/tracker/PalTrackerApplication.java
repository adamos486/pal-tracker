package io.pivotal.pal.tracker;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mysql.cj.jdbc.MysqlDataSource;
import io.pivotal.pal.tracker.persistency.InMemoryTimeEntryRepository;
import io.pivotal.pal.tracker.persistency.JdbcTimeEntryRepository;
import io.pivotal.pal.tracker.persistency.TimeEntryRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@SpringBootApplication public class PalTrackerApplication {

  @Bean TimeEntryRepository timeEntryRepository() {
    MysqlDataSource dataSource = new MysqlDataSource();
    dataSource.setUrl(System.getenv("SPRING_DATASOURCE_URL"));
    return new JdbcTimeEntryRepository(dataSource);
  }

  @Bean public ObjectMapper jsonObjectMapper() {
    return Jackson2ObjectMapperBuilder.json()
        .serializationInclusion(JsonInclude.Include.NON_NULL)
        .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .modules(new JavaTimeModule())
        .build();
  }

  public static void main(String[] args) {
    SpringApplication.run(PalTrackerApplication.class, args);
  }
}