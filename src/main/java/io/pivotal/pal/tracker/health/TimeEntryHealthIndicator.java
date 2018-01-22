package io.pivotal.pal.tracker.health;

import io.pivotal.pal.tracker.persistency.TimeEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component public class TimeEntryHealthIndicator implements HealthIndicator {
  @Autowired TimeEntryRepository timeEntryRepository;

  @Override public Health health() {
    int size = timeEntryRepository.list().size();
    Health.Builder healthBuilder = new Health.Builder();
    if (size < 5) {
      healthBuilder.up();
    } else {
      healthBuilder.down();
    }
    return healthBuilder.build();
  }
}
