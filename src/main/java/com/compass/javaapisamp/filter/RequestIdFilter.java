package com.compass.javaapisamp.filter;

import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
@Order(0)
public class RequestIdFilter extends OncePerRequestFilter {

    private static final String ID_KEY = "ID";
    private static final String X_HEADER = "X-Request-ID";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestId = request.getHeader(X_HEADER);
        if(!StringUtils.hasText(requestId)) {
            requestId = UUID.randomUUID().toString();
        }
        try {
            MDC.put(ID_KEY, requestId);
            response.addHeader(X_HEADER, requestId);
            filterChain.doFilter(request, response);
        }finally {
            MDC.remove(ID_KEY);
        }
    }
}
