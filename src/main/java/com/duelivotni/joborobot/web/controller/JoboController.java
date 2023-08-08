package com.duelivotni.joborobot.web.controller;

import com.duelivotni.joborobot.models.request.VacanciesSearchRequest;
import com.duelivotni.joborobot.models.response.VacanciesSearchResponse;
import com.duelivotni.joborobot.service.JoboService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class JoboController {

    private final JoboService service;

    @GetMapping("/vacancies")
    public VacanciesSearchResponse getVacancies() {
        var request = VacanciesSearchRequest.builder()
                .onlyWithSalary(true)
                .build();
        return service.getVacancies(request);
    }
}
