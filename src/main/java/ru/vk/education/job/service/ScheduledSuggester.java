package ru.vk.education.job.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.vk.education.job.domain.User;
import ru.vk.education.job.domain.Job;

import java.util.List;

@Component
public class ScheduledSuggester {

    private final SuggestService suggestService;
    private final UserService userService;

    public ScheduledSuggester(SuggestService suggestService, UserService userService) {
        this.suggestService = suggestService;
        this.userService = userService;
    }

    @Scheduled(fixedRate = 60000)
    public void runSuggestion() {
        System.out.println("=== Scheduled suggestions started ===");
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users to suggest for.");
            return;
        }
        for (User user : users) {
            List<Job> suggestions = suggestService.suggest(user.getName());
            if (suggestions.isEmpty()) {
                continue;
            }
            System.out.println("Top suggestions for " + user.getName() + ":");
            int count = 0;
            for (Job entry : suggestions) {
                if (count >= 2) break;
                System.out.println(entry.getTitle() + " at " + entry.getCompany());
                count++;
            }
        }
        System.out.println("=== Scheduled suggestions finished ===");
    }
}
