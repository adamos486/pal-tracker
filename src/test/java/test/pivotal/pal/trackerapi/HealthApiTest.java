package test.pivotal.pal.trackerapi;

import com.jayway.jsonpath.DocumentContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import test.pivotal.pal.RestTemplateConfigurer;

import static com.jayway.jsonpath.JsonPath.parse;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class HealthApiTest extends RestTemplateConfigurer {

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
