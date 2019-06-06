package org.multimodule.api.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = Logger.getLogger(JwtAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                         AuthenticationException ex) throws IOException {
        logger.error("요청 사용자는 인증되지 않았습니다. 인증 진입점(authentication entry point)에서 라우팅합니다.");
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
    }
}