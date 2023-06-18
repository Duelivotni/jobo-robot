package com.duelivotni.joborobot.web.client;

import com.google.common.io.ByteStreams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class HttpLoggingInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        log.info("request method: {}, request URI: {}, request headers: {}, \nrequest body: {}",
                request.getMethod(), request.getURI(), request.getHeaders(), body);
        ClientHttpResponse response = execution.execute(request, body);
        log.info("response status code: {}, response headers: {}, \nresponse body: {}",
                response.getStatusCode(), response.getHeaders(), new String(ByteStreams.toByteArray(response.getBody()), StandardCharsets.UTF_8));
        return response;
    }
}
