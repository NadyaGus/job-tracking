package ru.vk.education.job.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.vk.education.job.domain.Job;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class SuggestServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.sql.init.mode", () -> "always");
    }

    @Autowired
    private SuggestService suggestService;

    @Test
    @Sql(scripts = "/test-data.sql")
    void shouldSuggestJobsForExistingUser() {
        List<Job> suggestions = suggestService.suggest("Bob");

        assertNotNull(suggestions);
        assertFalse(suggestions.isEmpty());
        assertEquals(2, suggestions.size(), "Должно быть предложено ровно две вакансии");
        assertEquals("Backend Developer", suggestions.get(0).getTitle());
        assertEquals("Senior Java Developer", suggestions.get(1).getTitle());
    }
}