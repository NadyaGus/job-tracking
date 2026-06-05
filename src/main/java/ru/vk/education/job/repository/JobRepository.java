package ru.vk.education.job.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.vk.education.job.domain.Job;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class JobRepository {

    private final JdbcTemplate jdbc;

    public JobRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Transactional
    public Job save(Job job) {
        try {
            jdbc.update(
                    "INSERT INTO jobs (title, company, exp_required) VALUES (?, ?, ?)",
                    job.getTitle(),
                    job.getCompany(),
                    job.getExpRequired()
            );
        } catch (DuplicateKeyException e) {
            throw new DuplicateKeyException("Job with title '" + job.getTitle() + "' already exists", e);
        }

        String[] tags = job.getTags();
        if (tags != null && tags.length > 0) {
            List<Object[]> batchArgs = new ArrayList<>();
            for (String tag : tags) {
                String normalized = tag.trim().toLowerCase();
                if (!normalized.isEmpty()) {
                    batchArgs.add(new Object[]{job.getTitle(), normalized});
                }
            }
            jdbc.batchUpdate(
                    "INSERT INTO job_tags (job_title, tag) VALUES (?, ?)",
                    batchArgs
            );
        }
        return job;
    }

    @Transactional(readOnly = true)
    public Optional<Job> findByTitle(String title) {
        String sql = """
                SELECT j.title, j.company, j.exp_required, jt.tag
                FROM jobs j
                LEFT JOIN job_tags jt ON j.title = jt.job_title
                WHERE j.title = ?
                """;
        List<Job> jobs = jdbc.query(sql, new ResultSetExtractor<List<Job>>() {
            @Override
            public List<Job> extractData(ResultSet rs) throws SQLException, DataAccessException {
                Map<String, Job> jobMap = new LinkedHashMap<>();
                while (rs.next()) {
                    String jobTitle = rs.getString("title");
                    String company = rs.getString("company");
                    int expRequired = rs.getInt("exp_required");
                    String tag = rs.getString("tag");

                    Job job = jobMap.computeIfAbsent(jobTitle,
                            key -> new Job(key, company, new String[]{}, expRequired));
                    if (tag != null && !tag.isEmpty()) {
                        job.addTag(tag);
                    }
                }
                return new ArrayList<>(jobMap.values());
            }
        }, title);

        return jobs.isEmpty() ? Optional.empty() : Optional.of(jobs.get(0));
    }

    @Transactional
    public boolean deleteByTitle(String title) {
        int rows = jdbc.update("DELETE FROM jobs WHERE title = ?", title);
        return rows > 0;
    }

    @Transactional(readOnly = true)
    public List<Job> findAll() {
        String sql = """
                SELECT j.title, j.company, j.exp_required, jt.tag
                FROM jobs j
                LEFT JOIN job_tags jt ON j.title = jt.job_title
                ORDER BY j.title
                """;
        return jdbc.query(sql, new ResultSetExtractor<List<Job>>() {
            @Override
            public List<Job> extractData(ResultSet rs) throws SQLException, DataAccessException {
                Map<String, Job> jobMap = new LinkedHashMap<>();
                while (rs.next()) {
                    String jobTitle = rs.getString("title");
                    String company = rs.getString("company");
                    int expRequired = rs.getInt("exp_required");
                    String tag = rs.getString("tag");

                    Job job = jobMap.computeIfAbsent(jobTitle,
                            key -> new Job(key, company, new String[]{}, expRequired));
                    if (tag != null && !tag.isEmpty()) {
                        job.addTag(tag);
                    }
                }
                return new ArrayList<>(jobMap.values());
            }
        });
    }
}