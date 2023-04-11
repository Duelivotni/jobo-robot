package com.duelivotni.joborobot.auth.controller;

import com.duelivotni.joborobot.auth.client.HeadHunterOAuthClient;
import com.duelivotni.joborobot.auth.token.HeadHunterTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final HeadHunterOAuthClient headHunterOAuthClient;

    @Value("${spring.security.oauth2.client.registration.headhunter.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.headhunter.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.headhunter.redirect-uri}")
    private String redirectUri;

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }

    @GetMapping("/error")
    public String error(HttpServletRequest request) {
        String message = (String) request.getSession().getAttribute("error.message");
        request.getSession().removeAttribute("error.message");
        return message;
    }

    @GetMapping("/login/oauth2/code/headhunter")
    public String callback(
            @RequestParam("code") String code,
            @RequestParam("state") String state,
            OAuth2AuthenticationToken authenticationToken
    ) {
        log.info("Callback endpoint requested");
        HeadHunterTokenResponse response = headHunterOAuthClient.getToken(
                "authorization_code",
                code,
                clientId,
                clientSecret,
                redirectUri
        );
        String accessToken = response.getAccess_token();
        // Use the access token to make API calls to hh.ru
        log.info("Successfully obtained access token: {}", accessToken);
        return "Successfully obtained access token: " + accessToken;
    }
}
