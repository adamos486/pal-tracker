package test.pivotal.pal.trackerapi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import test.pivotal.pal.RestTemplateConfigurer;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class WelcomeApiTest extends RestTemplateConfigurer {

  @Test public void exampleTest() {
    String body = this.restTemplate.getForObject("/", String.class);
    assertThat(body).isEqualTo("Hello from test");
  }
}
