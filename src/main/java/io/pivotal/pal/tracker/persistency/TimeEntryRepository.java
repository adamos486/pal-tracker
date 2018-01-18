package io.pivotal.pal.tracker.persistency;

import io.pivotal.pal.tracker.models.TimeEntry;

import java.util.List;

public interface TimeEntryRepository {
  TimeEntry create(TimeEntry entry) throws Exception;

  TimeEntry find(long id);

  List<TimeEntry> list();

  TimeEntry update(long id, TimeEntry entry);

  boolean delete(long id);
}
