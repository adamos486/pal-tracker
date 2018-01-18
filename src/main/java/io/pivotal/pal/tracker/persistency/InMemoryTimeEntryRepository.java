package io.pivotal.pal.tracker.persistency;

import io.pivotal.pal.tracker.models.TimeEntry;

import java.util.*;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {
  private long idInc = 1L;
  private HashMap<Long, TimeEntry> timeMap = new HashMap<>();

  @Override
  public TimeEntry create(TimeEntry entry) throws Exception {
    if (entry.getId() == -1) {
      entry.setId(idInc);
      ++idInc;
    }
    timeMap.put(entry.getId(), entry);
    return entry;
  }

  @Override
  public TimeEntry find(long id) {
    return timeMap.get(id);
  }

  @Override
  public List<TimeEntry> list() {
    return new ArrayList<>(timeMap.values());
  }

  @Override
  public TimeEntry update(long id, TimeEntry entry) {
    if (entry.getId() == -1) {
      entry.setId(id);
    }
    if (find(id) == null) {
      return null;
    }
    timeMap.put(id, entry);
    return entry;
  }

  @Override
  public boolean delete(long id) {
    TimeEntry removed = timeMap.remove(id);
    if (removed != null) {
      return true;
    }
    return false;
  }
}
