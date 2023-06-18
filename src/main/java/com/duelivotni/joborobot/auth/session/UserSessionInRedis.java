package com.duelivotni.joborobot.auth.session;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserSessionInRedis {
    private final RedisTemplate<String, Object> redisTemplate;
    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;

    public void put(String key, Object value, Duration timeout) {
        String sessionKey = buildSessionKey(key);
        log.info("Putting into redis User Session Key={}, Value={}, Timeout={}", sessionKey, value, timeout);
        redisTemplate.opsForValue().set(sessionKey, value, timeout);
    }

    public Object get(String key) {
        String sessionKey = buildSessionKey(key);
        log.info("Fetching from redis User Session by Session Key: {}", sessionKey);
        return redisTemplate.opsForValue().get(sessionKey);
    }

    public void invalidate() {
        Set<String> keys = redisTemplate.keys(httpServletRequest.getSession().getId().concat("*"));
        for (String key : keys) {
            redisTemplate.expire(key, Duration.ZERO);
        }

        Cookie sessionCookie = WebUtils.getCookie(httpServletRequest, "JSESSIONID");
        sessionCookie.setMaxAge(0);
        httpServletResponse.addCookie(sessionCookie);

        httpServletRequest.getSession().invalidate();
    }

    private String buildSessionKey(String key) {
        return String.format("%s-%s", httpServletRequest.getSession().getId(), key);
    }
}
