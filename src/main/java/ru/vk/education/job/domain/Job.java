package ru.vk.education.job.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Job {
    @JsonProperty("title")
    private final String title;
    @JsonProperty("company")
    private final String company;
    @JsonProperty("tags")
    private final Set<String> tags;
    @JsonProperty("expRequired")
    private final int expRequired;

    // Jackson requires a default constructor for deserialization
    public Job() {
        this.title = "";
        this.company = "";
        this.tags = new HashSet<>();
        this.expRequired = 0;
    }

    @JsonCreator
    public Job(
            @JsonProperty("title") String title,
            @JsonProperty("company") String company,
            @JsonProperty("tags") String[] tags,
            @JsonProperty("expRequired") Integer experience) {
        this.title = title;
        this.company = company;
        this.tags = standardizeTags(tags);
        this.expRequired = (experience != null) ? experience : 0;
    }

    private HashSet<String> standardizeTags(String[] tags) {
       return new HashSet<>(Arrays.asList(tags));
    }

    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }

    public void addTag(String tag) {
        if (tag != null && !tag.trim().isEmpty()) {
            this.tags.add(tag.trim().toLowerCase());
        }
    }

    @Override
    public String toString() {
        return title + " " + company;
    }

    public String[] getTags() {
        return tags.toArray(String[]::new);
    }

    public int getExpRequired() {
        return expRequired;
    }
}
