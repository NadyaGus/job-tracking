package ru.vk.education.job.service;

import org.springframework.stereotype.Service;
import ru.vk.education.job.domain.User;
import ru.vk.education.job.domain.Job;
import ru.vk.education.job.repository.JobRepository;
import ru.vk.education.job.repository.UserRepository;

import java.util.*;

@Service
public class JobService {
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public JobService(UserRepository userRepository, JobRepository jobRepository) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public List<Job> getVacancies() {
        return jobRepository.findAll();
    }

    private User addUser(User user) {
        return userRepository.save(user);
    }

    private Optional<User> getUserByName(String name) {
        return userRepository.findByName(name);
    }

    private boolean deleteUser(String name) {
        return userRepository.deleteByName(name);
    }

    private Job addVacancy(Job job) {
        return jobRepository.save(job);
    }

    private Optional<Job> getVacancyByTitle(String title) {
        return jobRepository.findByTitle(title);
    }

    private boolean deleteVacancy(String title) {
        return jobRepository.deleteByTitle(title);
    }
}