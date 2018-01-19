package io.pivotal.pal.tracker.persistency;

import io.pivotal.pal.tracker.models.TimeEntry;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JdbcTimeEntryRepository implements TimeEntryRepository {
  private DataSource dataSource;
  private JdbcTemplate jdbcTemplate;

  public JdbcTimeEntryRepository(DataSource dataSource) {
    this.dataSource = dataSource;
    jdbcTemplate = new JdbcTemplate(this.dataSource);
  }

  @Override public TimeEntry create(TimeEntry entry) throws Exception {
    KeyHolder holder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement statement = connection.prepareStatement(
          "INSERT INTO time_entries (project_id, user_id, date, hours) " + "VALUES (?, ?, ?, ?)",
          RETURN_GENERATED_KEYS);
      statement.setLong(1, entry.getProjectId());
      statement.setLong(2, entry.getUserId());
      statement.setDate(3, Date.valueOf(entry.getDate()));
      statement.setInt(4, entry.getHours());

      return statement;
    }, holder);

    return find(holder.getKey().longValue());
  }

  @Override public TimeEntry find(long id) {
    return jdbcTemplate.query(
        "SELECT id, project_id, user_id, date, hours FROM time_entries WHERE id = ?",
        new Object[] { id }, extractor);
  }

  @Override public List<TimeEntry> list() {
    return jdbcTemplate.query("SELECT id, project_id, user_id, date, hours FROM time_entries",
        mapper);
  }

  @Override public TimeEntry update(long id, TimeEntry entry) {
    jdbcTemplate.update(
        "UPDATE time_entries set project_id=?, user_id=?, date=?, hours=? where id=?",
        entry.getProjectId(), entry.getUserId(), entry.getDate(), entry.getHours(), id);
    return find(id);
  }

  @Override public boolean delete(long id) {
    int result = jdbcTemplate.update(
        "DELETE FROM time_entries WHERE id=?",
        id
    );
    return result > 0;
  }

  private final RowMapper<TimeEntry> mapper =
      (rs, rowNum) -> new TimeEntry(rs.getLong("id"), rs.getLong("project_id"),
          rs.getLong("user_id"), rs.getDate("date").toLocalDate(), rs.getInt("hours"));

  private final ResultSetExtractor<TimeEntry> extractor =
      (rs) -> rs.next() ? mapper.mapRow(rs, 1) : null;
}
