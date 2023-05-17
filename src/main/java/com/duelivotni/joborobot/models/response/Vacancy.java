package com.duelivotni.joborobot.models.response;
import lombok.Data;

import java.util.List;

@Data
public class Vacancy {
    private Salary salary;
    private String name;
    private InsiderInterview insiderInterview;
    private Area area;
    private String url;
    private String publishedAt;
    private List<String> relations;
    private Employer employer;
    private Contact contacts;
    private Boolean responseLetterRequired;
    private Address address;
    private Double sortPointDistance;
    private String alternateUrl;
    private String applyAlternateUrl;
    private Department department;
    private Type type;
    private String id;
    private Boolean hasTest;
    private String responseUrl;
    private Snippet snippet;
    private Schedule schedule;
    private Counters counters;
    private List<ProfessionalRole> professionalRoles;
    private Boolean acceptIncompleteResumes;
}
