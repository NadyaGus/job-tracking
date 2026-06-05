package ru.vk.education.job.web;

import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.vk.education.job.domain.User;
import ru.vk.education.job.domain.Job;
import ru.vk.education.job.service.StatService;

import java.util.List;

@RestController
@RequestMapping("/stat")
@Validated
public class StatController {

    private final StatService statService;

    public StatController(StatService statService) {
        this.statService = statService;
    }

    @GetMapping("/exp")
    public List<Job> filterByExperience(@RequestParam @Positive int experience) {
        return statService.filterVacanciesByExp(experience);
    }

    @GetMapping("/match")
    public List<User> filterByMatchCount(@RequestParam @Positive int count) {
        return statService.filterUsersByMatchCount(count);
    }

    @GetMapping("/top-skills")
    public List<String> topSkills(@RequestParam @Positive int n) {
        return statService.getTopSkills(n);
    }
}