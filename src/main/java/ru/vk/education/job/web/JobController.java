package ru.vk.education.job.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vk.education.job.domain.Job;
import ru.vk.education.job.service.VacancyService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final VacancyService vacancyService;

    public JobController(VacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }

    @PostMapping
    public ResponseEntity<Job> addJob(@RequestBody Job job) {
        Job created = vacancyService.addVacancy(job);
        if (created == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<Job> getAllJobs() {
        return vacancyService.getAllVacancies();
    }

    @GetMapping("/{title}")
    public ResponseEntity<Job> getJob(@PathVariable String title) {
        Optional<Job> vacancy = vacancyService.getVacancyByTitle(title);
        return vacancy.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{title}")
    public ResponseEntity<Void> deleteJob(@PathVariable String title) {
        boolean deleted = vacancyService.deleteVacancy(title);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}