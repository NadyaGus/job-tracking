package ru.vk.education.job.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vk.education.job.domain.Job;
import ru.vk.education.job.service.SuggestService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/suggest")
public class SuggestController {

    private final SuggestService suggestService;

    public SuggestController(SuggestService suggestService) {
        this.suggestService = suggestService;
    }

    @GetMapping
    public ResponseEntity<List<Suggestion>> suggestForUser(@RequestParam String username) {
        List<Map.Entry<Job, Double>> sorted = suggestService.suggestWithScores(username);
        if (sorted.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Suggestion> suggestions = sorted.stream()
                .limit(2)
                .map(entry -> new Suggestion(
                        entry.getKey().getTitle(),
                        entry.getKey().getCompany(),
                        entry.getValue()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(suggestions);
    }

    public record Suggestion(String title, String company, double score) {}
}