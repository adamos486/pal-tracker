package io.pivotal.pal.tracker;

import io.pivotal.pal.tracker.models.TimeEntry;
import io.pivotal.pal.tracker.persistency.TimeEntryRepository;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController public class TimeEntryController {
  private final GaugeService gauge;
  private final CounterService counter;
  private TimeEntryRepository repo;
  private final String createdLabel = "TimeEntry.created";
  private final String countedLabel = "TimeEntry.count";
  private final String readLabel = "TimeEntry.read";
  private final String listedLabel = "TimeEntry.listed";
  private final String updatedLabel = "TimeEntry.updated";
  private final String deleteLabel = "TimeEntry.delete";

  public TimeEntryController(TimeEntryRepository repo, CounterService counter, GaugeService gauge) {
    this.repo = repo;
    this.counter = counter;
    this.gauge = gauge;
  }

  @PostMapping("/time-entries") public ResponseEntity create(@RequestBody TimeEntry timeEntry)
      throws Exception {
    try {
      TimeEntry created = repo.create(timeEntry);
      if (created.getId() != -1) {
        counter.increment(createdLabel);
        gauge.submit(countedLabel, repo.list().size());
        return new ResponseEntity<>(created, HttpStatus.CREATED);
      } else {
        throw new Exception("Time Entry creation failed!!!");
      }
    } catch (Exception e) {
      System.out.println("Error while creating time entry: " + e.getLocalizedMessage());
      throw e;
    }
  }

  @GetMapping("/time-entries/{id}") public ResponseEntity<TimeEntry> read(@PathVariable long id) {
    try {
      TimeEntry found = repo.find(id);
      if (found != null) {
        counter.increment(readLabel);
        return new ResponseEntity<>(found, HttpStatus.OK);
      } else {
        return new ResponseEntity<>((TimeEntry) null, HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      throw e;
    }
  }

  @GetMapping("/time-entries") public ResponseEntity<List<TimeEntry>> list() {
    List<TimeEntry> entries = repo.list();
    counter.increment(listedLabel);
    return new ResponseEntity<>(entries, HttpStatus.OK);
  }

  @PutMapping("/time-entries/{id}")
  public ResponseEntity update(@PathVariable long id, @RequestBody TimeEntry entry) {
    TimeEntry updated = repo.update(id, entry);
    if (updated == null) {
      counter.increment(updatedLabel);
      return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(updated, HttpStatus.OK);
  }

  @DeleteMapping("/time-entries/{id}")
  public ResponseEntity<TimeEntry> delete(@PathVariable long id) {
    boolean deleted = repo.delete(id);
    counter.increment(deleteLabel);
    gauge.submit(countedLabel, repo.list().size());
    return new ResponseEntity<>(new TimeEntry(), HttpStatus.NO_CONTENT);
  }
}
