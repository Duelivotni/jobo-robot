package com.duelivotni.joborobot.models.response;

import lombok.Data;

import java.util.List;

@Data
public class VacanciesSearchResponse {
    private Integer perPage;
    private List<Vacancy> items;
    private Integer page;
    private Integer pages;
    private Integer found;
    private Object clusters;
    private Object arguments;
}
