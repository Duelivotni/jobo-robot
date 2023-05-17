package com.duelivotni.joborobot.models.response;
import lombok.Data;

@Data
public class Salary {
    private Integer to;
    private Integer from;
    private String currency;
    private Boolean gross;
}