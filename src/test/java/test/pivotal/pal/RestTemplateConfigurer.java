package test.pivotal.pal;

import com.mysql.cj.jdbc.MysqlDataSource;
import io.pivotal.pal.tracker.PalTrackerApplication;
import java.util.TimeZone;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(classes = PalTrackerApplication.class, webEnvironment = RANDOM_PORT)
public class RestTemplateConfigurer {
  public JdbcTemplate jdbcTemplate;
  public TestRestTemplate restTemplate;

  @Value("${USER}") private String user;
  @Value("${PASSWORD}") private String password;
  @LocalServerPort private String port;

  @Before public void setUp() throws Exception {
    RestTemplate restTemplateBuilder = new RestTemplateBuilder()
        .rootUri("http://localhost:" + port)
        .basicAuthorization(user, password)
        .build();

    MysqlDataSource dataSource = new MysqlDataSource();
    dataSource.setUrl(System.getenv("SPRING_DATASOURCE_URL"));

    jdbcTemplate = new JdbcTemplate(dataSource);
    jdbcTemplate.execute("TRUNCATE time_entries");

    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

    restTemplate = new TestRestTemplate(restTemplateBuilder);
  }
}
