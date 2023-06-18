package com.duelivotni.joborobot.service;

import com.duelivotni.joborobot.auth.session.UserSessionInRedis;
import com.duelivotni.joborobot.models.request.VacanciesSearchRequest;
import com.duelivotni.joborobot.models.response.VacanciesSearchResponse;
import com.duelivotni.joborobot.web.client.JoboClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JoboService {
    private static final String KEY_USER = "user";
    private static final String TOKEN_KEY = "Bearer";

    private final JoboClient client;
    private final UserSessionInRedis userSession;

    public VacanciesSearchResponse getVacancies(VacanciesSearchRequest request) {
        try {
            Map user = ((Map) userSession.get(KEY_USER));
            var token = (String) user.get(TOKEN_KEY);
            return client.fetchVacancies(request, token).getBody();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
