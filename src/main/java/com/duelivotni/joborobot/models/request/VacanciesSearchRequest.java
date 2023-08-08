package com.duelivotni.joborobot.models.request;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class VacanciesSearchRequest implements Serializable {

    private Integer page;
    private Integer perPage;
    private String text;
    private String searchField;
    private String experience;
    private String employment;
    private String schedule;
    private String area;
    private String metro;
    private String professionalRole;
    private String industry;
    private String employerId;
    private String currency;
    private Double salary;
    private String label;
    private Boolean onlyWithSalary;
    private Integer period;
    private String dateFrom;
    private String dateTo;
    private Double topLat;
    private Double bottomLat;
    private Double leftLng;
    private Double rightLng;
    private String orderBy;
    private Double sortPointLat;
    private Double sortPointLng;
    private Boolean clusters;
    private Boolean describeArguments;
    private Boolean noMagic;
    private Boolean premium;
    private Boolean responsesCountEnabled;
    private String partTime;
    private String locale;
    private String host;
}
