package com.compass.javaapisamp.filter;

import io.micrometer.core.instrument.util.StringUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RequestIdFilterTest {

    @Test
    void existRequestIdTest() throws Exception {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        MockFilterChain mockFilterChain = new MockFilterChain();

        mockRequest.addHeader("X-Request-ID", "123");
        new RequestIdFilter().doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        // MDCはremoveされる
        assertEquals(null, MDC.get("ID"));
        assertEquals("123", mockResponse.getHeader("X-Request-ID"));
    }

    @Test
    void generateRequestIdTest() throws Exception {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        MockFilterChain mockFilterChain = new MockFilterChain();

        mockRequest.addHeader("X-Request-ID", "");
        new RequestIdFilter().doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        // MDCはremoveされる
        assertEquals(null, MDC.get("ID"));
        assertTrue(StringUtils.isNotEmpty(mockResponse.getHeader("X-Request-ID")));
    }
}
