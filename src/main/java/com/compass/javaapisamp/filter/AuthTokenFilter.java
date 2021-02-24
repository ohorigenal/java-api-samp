package com.compass.javaapisamp.filter;

import com.compass.javaapisamp.model.exception.APIException;
import com.compass.javaapisamp.model.exception.Errors;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private static final String AUTH_TOKEN_HEADER = "Auth-Token";
    private static final String FIXED_TOKEN = "auth-token";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(AUTH_TOKEN_HEADER);
        if(StringUtils.isEmpty(token) || FIXED_TOKEN.equals(token)) {
            throw new APIException(Errors.UNAUTHORIZED);
        }
        filterChain.doFilter(request, response);
    }
}
