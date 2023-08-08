package com.duelivotni.joborobot.web.client;

import com.duelivotni.joborobot.config.JoboConfig;
import com.duelivotni.joborobot.models.request.VacanciesSearchRequest;
import com.duelivotni.joborobot.models.response.VacanciesSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JoboClient {

    private final RestTemplate restTemplate;
    private final JoboConfig joboConfig;

    //TODO pass bearer token && test api query

    public ResponseEntity<VacanciesSearchResponse> fetchVacancies(VacanciesSearchRequest request, String bearer) throws IOException {
        String url = joboConfig.getSearchVacanciesUrl();
        HttpHeaders headers = prepAuthJsonHeader(bearer);
//        headers.set("Content-type", MediaType.APPLICATION_JSON_VALUE);
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(request, headers), VacanciesSearchResponse.class);
    }

//    public ResponseEntity<VacanciesSearchResponse> otherMethod(VacanciesSearchRequest request) throws IOException {
//        String url = joboConfig.getSearchVacanciesUrl()
//                .replace("{siteId}", request.getCredentials().getSiteId())
//                .replace("{paymentId}", request.getPaymentId());
//        HttpHeaders headers = prepAuthJsonHeader(request.getCredentials().getBearerToken());
//        headers.set("Content-type", MediaType.APPLICATION_JSON_VALUE);
//        return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request, headers), VacanciesSearchResponse.class);
//    }

    private HttpHeaders prepAuthJsonHeader(String BearerToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + BearerToken);
//        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.set("HH-User-Agent", "jobo-robot (souji07031988@gmail.com)");
        return headers;
    }
}
