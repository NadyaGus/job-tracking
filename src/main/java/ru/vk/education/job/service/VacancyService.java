package ru.vk.education.job.service;

import org.springframework.stereotype.Service;
import ru.vk.education.job.domain.Job;
import ru.vk.education.job.repository.JobRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VacancyService {
    private final JobRepository jobRepository;

    public VacancyService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Job addVacancy(Job job) {
        return jobRepository.save(job);
    }

    public List<Job> getAllVacancies() {
        return jobRepository.findAll();
    }

    public Optional<Job> getVacancyByTitle(String title) {
        return jobRepository.findByTitle(title);
    }

    public boolean deleteVacancy(String title) {
        return jobRepository.deleteByTitle(title);
    }

    public List<Job> filterByExperience(int experience) {
        return jobRepository.findAll().stream()
                .filter(v -> v.getExpRequired() >= experience)
                .sorted(Comparator.comparing(Job::getTitle, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }
}