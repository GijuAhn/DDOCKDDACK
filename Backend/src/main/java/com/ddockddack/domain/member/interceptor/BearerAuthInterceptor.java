package com.ddockddack.domain.member.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ddockddack.domain.member.extractor.AuthorizationExtractor;
import com.ddockddack.domain.member.service.JwtTokenProvider;

@Component
public class BearerAuthInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(BearerAuthInterceptor.class);

    private AuthorizationExtractor authExtractor;
    private JwtTokenProvider jwtTokenProvider;

    public BearerAuthInterceptor(AuthorizationExtractor authExtractor, JwtTokenProvider jwtTokenProvider) {
        this.authExtractor = authExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }
        logger.debug(">>> interceptor.preHandle 호출");
        String token = authExtractor.extract(request, "Bearer"); //토큰 추출
        logger.debug("token : {}", token);
        if (!jwtTokenProvider.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰");
        }

        String userid = jwtTokenProvider.getSubject(token);
        request.setAttribute("userid", userid);
        return true;
    }
}