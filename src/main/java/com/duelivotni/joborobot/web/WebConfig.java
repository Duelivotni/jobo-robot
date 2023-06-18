package com.duelivotni.joborobot.web;

import com.duelivotni.joborobot.config.JoboConfig;
import com.duelivotni.joborobot.web.client.HttpErrorHandler;
import com.duelivotni.joborobot.web.client.HttpLoggingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class WebConfig {

    private final JoboConfig joboConfig;

    @Bean
    public RestTemplate joboRestTemplate(
            HttpLoggingInterceptor httpLoggingInterceptor,
            HttpErrorHandler httpErrorHandler)
    {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(joboConfig.getConnectionTimeOut());
        requestFactory.setReadTimeout(joboConfig.getReadTimeOut());
        BufferingClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(requestFactory);
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setErrorHandler(httpErrorHandler);
        restTemplate.setInterceptors(Collections.singletonList(httpLoggingInterceptor));
        return restTemplate;
    }
}
