package com.compass.javaapisamp.filter;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Controllerでfilter含めてMockMvcでテストは可能であるが、単体で細かくテストする
public class AuthTokenFilterTest {

    @Test
    void normalTest() throws Exception {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        MockFilterChain mockFilterChain = new MockFilterChain();

        mockRequest.addHeader("Auth-Token", "auth-token");
        new AuthTokenFilter().doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        // レスポンスにセットされないこと
        assertEquals(200, mockResponse.getStatus());
        assertEquals("", mockResponse.getContentAsString());
    }

    @Test
    void errorTest() throws Exception {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        MockFilterChain mockFilterChain = new MockFilterChain();

        mockRequest.addHeader("Auth-Token", "invalid-token");
        new AuthTokenFilter().doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        assertEquals(401, mockResponse.getStatus());
        JSONAssert.assertEquals("{\"message\":\"unauthorized error. \"}", mockResponse.getContentAsString(), true);
    }
}
