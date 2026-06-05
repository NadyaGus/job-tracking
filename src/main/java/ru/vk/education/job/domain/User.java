package ru.vk.education.job.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.HashSet;

public class User {
    @JsonProperty("name")
    private final String name;
    @JsonProperty("skills")
    private final HashSet<String> skills;
    @JsonProperty("experience")
    private final int experience;

    // Jackson requires a default constructor for deserialization
    public User() {
        this.name = "";
        this.skills = new HashSet<>();
        this.experience = 0;
    }

    @JsonCreator
    public User(
            @JsonProperty("name") String name,
            @JsonProperty("skills") String[] skills,
            @JsonProperty("experience") Integer experience) {
        this.name = name;
        this.skills = standardizeSkills(skills);
        this.experience = (experience != null) ? experience : 0;
    }

    private HashSet<String> standardizeSkills(String[] tags) {
        return new HashSet<>(Arrays.asList(tags));
    }

    public String getSkillsString() {
        StringBuilder str = new StringBuilder();
        String[] arr = skills.toArray(String[]::new);

        for (int i = 0; i < arr.length; i++) {
            str.append(arr[i]);
            if (i != arr.length - 1) str.append(',');
        }

        return str.toString();
    }

    public void addSkill(String skill) {
        if (skill != null && !skill.trim().isEmpty()) {
            this.skills.add(skill.trim().toLowerCase());
        }
    }

    @Override
    public String toString() {
        return getName() + " " + getSkillsString() + " " + experience;
    }

    public String getName() {
        return name;
    }

    public String[] getSkills() {
        return skills.toArray(String[]::new);
    }

    public int getExperience() {
        return experience;
    }
}
