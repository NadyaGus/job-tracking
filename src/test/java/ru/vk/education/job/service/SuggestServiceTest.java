package ru.vk.education.job.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vk.education.job.domain.Job;
import ru.vk.education.job.domain.User;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SuggestServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private JobService jobService;

    @InjectMocks
    private SuggestService suggestService;

    private User sampleUser;
    private Job job1, job2, job3;

    @BeforeEach
    void setUp() {
        sampleUser = new User("Alice", new String[]{"java", "spring"}, 3);

        job1 = new Job("Java Developer", "Company A", new String[]{"java", "sql"}, 2);
        job2 = new Job("Senior Java Dev", "Company B", new String[]{"java", "spring"}, 2);
        job3 = new Job("Python Dev", "Company C", new String[]{"python"}, 1);
    }

    @Test
    void suggestTest() {
        when(userService.getUserByName("Alice")).thenReturn(Optional.of(sampleUser));
        when(jobService.getVacancies()).thenReturn(Arrays.asList(job1, job2, job3));

        List<Job> result = suggestService.suggest("Alice");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(job2, result.get(0));
        assertEquals(job1, result.get(1));
    }

    @Test
    void emptyVacanciesTest() {
        when(userService.getUserByName("Alice")).thenReturn(Optional.of(sampleUser));
        when(jobService.getVacancies()).thenReturn(Collections.emptyList());

        List<Job> result = suggestService.suggest("Alice");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void singleVacancyTest() {
        when(userService.getUserByName("Alice")).thenReturn(Optional.of(sampleUser));
        when(jobService.getVacancies()).thenReturn(List.of(job1));

        List<Job> result = suggestService.suggest("Alice");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(job1, result.get(0));
    }

    @Test
    void userNotFoundTest() {
        when(userService.getUserByName("unknown")).thenReturn(Optional.empty());

        List<Job> result = suggestService.suggest("unknown");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}