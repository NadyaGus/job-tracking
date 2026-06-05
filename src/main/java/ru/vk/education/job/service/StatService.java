package ru.vk.education.job.service;

import org.springframework.stereotype.Service;
import ru.vk.education.job.domain.User;
import ru.vk.education.job.domain.Job;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatService {
    private final UserService userService;
    private final VacancyService vacancyService;
    private final SuggestService suggestService;

    public StatService(UserService userService, VacancyService vacancyService, SuggestService suggestService) {
        this.userService = userService;
        this.vacancyService = vacancyService;
        this.suggestService = suggestService;
    }

    public List<Job> filterVacanciesByExp(int experience) {
        return vacancyService.filterByExperience(experience);
    }

    public List<User> filterUsersByMatchCount(int n) {
        Map<User, List<Job>> matches = new HashMap<>();
        for (User user : userService.getAllUsers()) {
            List<Job> suggestions = suggestService.suggest(user.getName());
            matches.put(user, suggestions);
        }
        return matches.keySet().stream()
                .filter(user -> matches.get(user).size() >= n)
                .sorted(Comparator.comparing(User::getName))
                .collect(Collectors.toList());
    }

    public List<String> getTopSkills(int n) {
        Map<String, Integer> popularity = new HashMap<>();
        for (User user : userService.getAllUsers()) {
            for (String skill : user.getSkills()) {
                popularity.merge(skill, 1, Integer::sum);
            }
        }
        return popularity.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(n)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}