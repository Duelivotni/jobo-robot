package com.duelivotni.joborobot.service;

import com.duelivotni.joborobot.models.request.VacanciesSearchRequest;
import com.duelivotni.joborobot.models.response.VacanciesSearchResponse;
import com.duelivotni.joborobot.web.JoboClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoboService {

    private final JoboClient client;

    public VacanciesSearchResponse getVacancies(VacanciesSearchRequest request) {
        return client.fetchVacancies(request);
    }
}
