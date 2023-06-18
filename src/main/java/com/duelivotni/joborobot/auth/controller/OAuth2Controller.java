package com.duelivotni.joborobot.auth.controller;

import com.duelivotni.joborobot.auth.factory.OAuth2ServiceFactory;
import com.duelivotni.joborobot.auth.session.UserSessionInRedis;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.revoke.TokenTypeHint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OAuth2Controller {
    private final OAuth2ServiceFactory oAuth2ServiceFactory;
    private final ObjectMapper objectMapper;
    private final UserSessionInRedis userSession;
    private final HttpServletResponse httpServletResponse;

    private static final String KEY_USER = "user";
    private static final String KEY_STATE = "state";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_ACCESS_TOKEN = "accessToken";
    private static final String KEY_SERVICE_ID = "serviceId";

    /**
     * @param serviceId
     * @return authorization url
     *
     * Defines service for corresponding provider (e.g. Headhunter)
     * Returns its auth url
     */
    @GetMapping("/oauth2/authorization/{serviceId}")
    public RedirectView oauth2Login(@PathVariable String serviceId) {
        String state = UUID.randomUUID().toString();
        userSession.put(KEY_STATE , state , Duration.of(60 , ChronoUnit.SECONDS));
        return new RedirectView(oAuth2ServiceFactory.getService(serviceId).getAuthorizationUrl(state));
    }

    /**
     * @param serviceId
     * @param code
     * @param state
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws IOException
     *
     * Prevents SCRF attacks by comparing states -> returns 401 if no match
     * Exchanges auth code for access token
     * Saves token, service id, username into Redis for fetching and using in future requests
     */
    @GetMapping("/login/oauth2/code/{serviceId}")
    public RedirectView oauth2Code(@PathVariable String serviceId , String code , String state) throws InterruptedException, ExecutionException, IOException {
        if (!Objects.equals(state , userSession.get(KEY_STATE))) {
            log.warn("Failed to get by KEY_STATE from user session. State = {}", state);
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        } else {
            OAuth20Service oAuth20Service = oAuth2ServiceFactory.getService(serviceId);
            OAuth2AccessToken accessToken = oAuth20Service.getAccessToken(code);
            OAuth2ServiceFactory.OAuth2Api oAuth2Api = (OAuth2ServiceFactory.OAuth2Api) oAuth20Service.getApi();
            final OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET , oAuth2Api.getUserInfoEndpoint());
            oAuth20Service.signRequest(accessToken , oAuthRequest);
            Map<String, String> map = objectMapper.readValue(oAuth20Service.execute(oAuthRequest).getBody() , Map.class);
            map.put(KEY_ACCESS_TOKEN , accessToken.getAccessToken());
            map.put(KEY_SERVICE_ID , serviceId);
            map.put(KEY_USERNAME , map.get(oAuth2Api.getUserNameAttribute()));
            int expiresIn = Optional.ofNullable(accessToken.getExpiresIn()).orElse(3600);
            log.info("Putting into redis: {}, {} expires in {}", KEY_USER, map, expiresIn);
            userSession.put(KEY_USER , map , Duration.of(expiresIn , ChronoUnit.SECONDS));
        }

        return new RedirectView("/");
    }

    /**
     * @param request
     * @return
     *
     * Returns UserInfo from Redis -> Map of token, service id, username
     */
    @GetMapping("/user")
    public Map user(HttpServletRequest request) {
        Map user = ((Map) userSession.get(KEY_USER));
        log.info("Fetched User by key={} from redis User Session. User = {}", KEY_USER, user);
        return Objects.isNull(user) ? null : Collections.singletonMap(KEY_USERNAME , user.get(KEY_USERNAME));
    }

    /**
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws IOException
     *
     * Revokes token
     * Invalidates session in Redis
     */
    @PostMapping("/logout")
    public void logout() throws InterruptedException, ExecutionException, IOException {
        Map<String, String> user = (Map) userSession.get(KEY_USER);
        if (Objects.nonNull(user)) {
            String serviceId = user.get(KEY_SERVICE_ID);
            OAuth20Service oAuth20Service = oAuth2ServiceFactory.getService(serviceId);
            if (Objects.equals("facebook" , serviceId) || Objects.equals("headhunter" , serviceId)) {
                revokePermission(oAuth20Service , user);
            } else if (!oAuth20Service.getApi().getRevokeTokenEndpoint().isEmpty()) {
                oAuth20Service.revokeToken(user.get(KEY_ACCESS_TOKEN) , TokenTypeHint.ACCESS_TOKEN);
            }
            userSession.invalidate();
        }
    }

    private void revokePermission(OAuth20Service oAuth20Service , Map<String, String> user) throws InterruptedException, ExecutionException, IOException {
        OAuth2ServiceFactory.OAuth2Api oAuth2Api = (OAuth2ServiceFactory.OAuth2Api) oAuth20Service.getApi();
        String endPoint = oAuth2Api.getRevokePermissionEndpoint().replace("{user-id}" , user.get("id"));
        final OAuthRequest oAuthRequest = new OAuthRequest(Verb.DELETE , endPoint);
        oAuth20Service.signRequest(user.get(KEY_ACCESS_TOKEN) , oAuthRequest);
        oAuth20Service.execute(oAuthRequest);
    }
}
