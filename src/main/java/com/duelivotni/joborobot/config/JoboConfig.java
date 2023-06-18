package com.duelivotni.joborobot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JoboConfig {

    @Value("${api.http.connection-timeout}")
    public Integer connectionTimeOut;

    @Value("${api.http.read-timeout}")
    public Integer readTimeOut;

    @Value("${api.headhunter.urls.searchVacanciesUrl}")
    public String searchVacanciesUrl;

    public Integer getConnectionTimeOut() {
        return connectionTimeOut;
    }

    public void setConnectionTimeOut(Integer connectionTimeOut) {
        this.connectionTimeOut = connectionTimeOut;
    }

    public Integer getReadTimeOut() {
        return readTimeOut;
    }

    public void setReadTimeOut(Integer readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    public String getSearchVacanciesUrl() {
        return searchVacanciesUrl;
    }

    public void setSearchVacanciesUrl(String searchVacanciesUrl) {
        this.searchVacanciesUrl = searchVacanciesUrl;
    }
}
