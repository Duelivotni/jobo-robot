package com.duelivotni.joborobot.web.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Scanner;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

@Component
@Slf4j
public class HttpErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        return (httpResponse.getStatusCode().series() == CLIENT_ERROR || httpResponse.getStatusCode().series() == SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        String bodyAsString = errorBodytoString(httpResponse.getBody());
        log.error("Jobo client ERROR: {}", bodyAsString);
        throw new IOException(httpResponse.getStatusCode() + bodyAsString);
    }

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse httpResponse) throws IOException {
        String bodyAsString = errorBodytoString(httpResponse.getBody());
        log.error("Jobo client ERROR. " +
                        "URL: {}, HttpMethod: {}, ResponseBody: {}",
                url, method, bodyAsString);
        throw new IOException(httpResponse.getStatusCode() + bodyAsString);
    }

    private String errorBodytoString(InputStream inputStream) {
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
