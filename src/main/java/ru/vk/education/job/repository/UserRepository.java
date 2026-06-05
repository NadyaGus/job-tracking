package ru.vk.education.job.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.vk.education.job.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbc;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Transactional
    public User save(User user) {
        try {
            jdbc.update(
                    "INSERT INTO users (name, experience) VALUES (?, ?)",
                    user.getName(),
                    user.getExperience()
            );
        } catch (DuplicateKeyException e) {
            throw new DuplicateKeyException("User with name '" + user.getName() + "' already exists", e);
        }

        String[] skills = user.getSkills();
        if (skills != null && skills.length > 0) {
            List<Object[]> batchArgs = new ArrayList<>();
            for (String skill : skills) {
                String normalized = skill.trim().toLowerCase();
                if (!normalized.isEmpty()) {
                    batchArgs.add(new Object[]{user.getName(), normalized});
                }
            }
            jdbc.batchUpdate(
                    "INSERT INTO user_skills (user_name, skill) VALUES (?, ?)",
                    batchArgs
            );
        }
        return user;
    }

    @Transactional(readOnly = true)
    public Optional<User> findByName(String name) {
        String sql = """
                SELECT u.name, u.experience, us.skill
                FROM users u
                LEFT JOIN user_skills us ON u.name = us.user_name
                WHERE u.name = ?
                """;
        List<User> users = jdbc.query(sql, new ResultSetExtractor<List<User>>() {
            @Override
            public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
                Map<String, User> userMap = new LinkedHashMap<>();
                while (rs.next()) {
                    String userName = rs.getString("name");
                    int experience = rs.getInt("experience");
                    String skill = rs.getString("skill");

                    User user = userMap.computeIfAbsent(userName,
                            key -> new User(key, new String[]{}, experience));
                    if (skill != null && !skill.isEmpty()) {
                        user.addSkill(skill);
                    }
                }
                return new ArrayList<>(userMap.values());
            }
        }, name);

        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    @Transactional
    public boolean deleteByName(String name) {
        int rows = jdbc.update("DELETE FROM users WHERE name = ?", name);
        return rows > 0;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        String sql = """
                SELECT u.name, u.experience, us.skill
                FROM users u
                LEFT JOIN user_skills us ON u.name = us.user_name
                ORDER BY u.name
                """;
        return jdbc.query(sql, new ResultSetExtractor<List<User>>() {
            @Override
            public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
                Map<String, User> userMap = new LinkedHashMap<>();
                while (rs.next()) {
                    String userName = rs.getString("name");
                    int experience = rs.getInt("experience");
                    String skill = rs.getString("skill");

                    User user = userMap.computeIfAbsent(userName,
                            key -> new User(key, new String[]{}, experience));
                    if (skill != null && !skill.isEmpty()) {
                        user.addSkill(skill);
                    }
                }
                return new ArrayList<>(userMap.values());
            }
        });
    }
}