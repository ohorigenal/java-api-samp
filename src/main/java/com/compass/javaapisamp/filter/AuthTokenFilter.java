package com.compass.javaapisamp.filter;

import com.compass.javaapisamp.model.dto.ErrorResponse;
import com.compass.javaapisamp.model.exception.Errors;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class AuthTokenFilter extends OncePerRequestFilter {

    private final String AUTH_TOKEN_HEADER = "Auth-Token";
    private final String FIXED_TOKEN = "auth-token";
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(AUTH_TOKEN_HEADER);
        if(!FIXED_TOKEN.equals(token)) {
            // filterでの例外はControllerAdviceでキャッチされないためここでレスポンスを作る
            response.getWriter().write(mapper.writeValueAsString(
                    new ErrorResponse(Errors.UNAUTHORIZED.getMessage()))
            );
            response.setStatus(Errors.UNAUTHORIZED.getStatus().value());
            return;
        }
        filterChain.doFilter(request, response);
    }
}
