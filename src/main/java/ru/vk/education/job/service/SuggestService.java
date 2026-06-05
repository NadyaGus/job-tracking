package ru.vk.education.job.service;

import org.springframework.stereotype.Service;
import ru.vk.education.job.domain.Job;
import ru.vk.education.job.domain.User;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SuggestService {

    private final UserService userService;
    private final JobService jobService;

    public SuggestService(UserService userService, JobService jobService) {
        this.userService = userService;
        this.jobService = jobService;
    }

    /**
     * Возвращает до двух лучших вакансий без очков (для тестов).
     */
    public List<Job> suggest(String username) {
        return suggestWithScores(username).stream()
                .limit(2)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Возвращает отсортированный список пар (вакансия, очки) – используется контроллером.
     */
    public List<Map.Entry<Job, Double>> suggestWithScores(String username) {
        Optional<User> userOpt = userService.getUserByName(username);
        if (userOpt.isEmpty()) {
            return Collections.emptyList();
        }
        User user = userOpt.get();
        String[] userSkills = user.getSkills();
        int userExp = user.getExperience();

        List<Job> allJobs = jobService.getVacancies();
        Map<Job, Double> scores = new HashMap<>();
        for (Job job : allJobs) {
            double score = calculateScore(userSkills, userExp, job);
            if (score > 0) {
                scores.put(job, score);
            }
        }
        return scores.entrySet().stream()
                .sorted(Map.Entry.<Job, Double>comparingByValue().reversed())
                .collect(Collectors.toList());
    }

    private double calculateScore(String[] userSkills, int userExp, Job job) {
        Set<String> userSet = new HashSet<>(Arrays.asList(userSkills));
        Set<String> vacSet = new HashSet<>(Arrays.asList(job.getTags()));
        userSet.retainAll(vacSet);
        double score = userSet.size();
        if (userExp < job.getExpRequired()) {
            score /= 2.0;
        }
        return score;
    }
}