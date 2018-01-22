package test.pivotal.pal.trackerapi;

import com.jayway.jsonpath.DocumentContext;
import com.mysql.cj.jdbc.MysqlDataSource;
import io.pivotal.pal.tracker.PalTrackerApplication;
import java.util.TimeZone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.jsonpath.JsonPath.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PalTrackerApplication.class, webEnvironment = RANDOM_PORT)
public class HealthApiTest {
  private JdbcTemplate jdbcTemplate;

  @Autowired private TestRestTemplate restTemplate;

  @Before public void setUp() throws Exception {
    MysqlDataSource dataSource = new MysqlDataSource();
    dataSource.setUrl(System.getenv("SPRING_DATASOURCE_URL"));

    jdbcTemplate = new JdbcTemplate(dataSource);
    jdbcTemplate.execute("TRUNCATE time_entries");

    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
  }

  @Test
  public void healthTest() throws InterruptedException {
    Thread.sleep(1000L);
    ResponseEntity<String> response = this.restTemplate.getForEntity("/health", String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    DocumentContext healthJson = parse(response.getBody());

    assertThat(healthJson.read("$.status", String.class)).isEqualTo("UP");
    assertThat(healthJson.read("$.db.status", String.class)).isEqualTo("UP");
    assertThat(healthJson.read("$.diskSpace.status", String.class)).isEqualTo("UP");
  }
}
