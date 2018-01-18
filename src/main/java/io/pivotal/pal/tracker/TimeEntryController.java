package io.pivotal.pal.tracker;

import io.pivotal.pal.tracker.models.TimeEntry;
import io.pivotal.pal.tracker.persistency.TimeEntryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TimeEntryController {
  private TimeEntryRepository repo;

  public TimeEntryController(TimeEntryRepository repo) {
    this.repo = repo;
  }

  @PostMapping("/time-entries")
  public ResponseEntity create(@RequestBody TimeEntry timeEntry) throws Exception {
    try {
      TimeEntry created = repo.create(timeEntry);
      if (created.getId() != -1) {
        return new ResponseEntity<>(created, HttpStatus.CREATED);
      } else {
        throw new Exception("Time Entry creation failed!!!");
      }
    } catch (Exception e) {
      System.out.println("Error while creating time entry: " + e.getLocalizedMessage());
      throw e;
    }
  }

  @GetMapping("/time-entries/{id}")
  public ResponseEntity<TimeEntry> read(@PathVariable long id) {
    try {
      TimeEntry found = repo.find(id);
      if (found != null) {
        return new ResponseEntity<>(found, HttpStatus.OK);
      } else {
        return new ResponseEntity<>(found, HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      throw e;
    }
  }

  @GetMapping("/time-entries")
  public ResponseEntity<List<TimeEntry>> list() {
    List<TimeEntry> entries = repo.list();
    return new ResponseEntity<>(entries, HttpStatus.OK);
  }

  @PutMapping("/time-entries/{id}")
  public ResponseEntity update(@PathVariable long id, @RequestBody TimeEntry entry) {
    TimeEntry updated = repo.update(id, entry);
    if (updated == null) {
      return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(updated, HttpStatus.OK);
  }

  @DeleteMapping("/time-entries/{id}")
  public ResponseEntity<TimeEntry> delete(@PathVariable long id) {
    boolean deleted = repo.delete(id);
    return new ResponseEntity<>(new TimeEntry(), HttpStatus.NO_CONTENT);
  }
}
