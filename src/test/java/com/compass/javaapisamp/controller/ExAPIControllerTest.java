package com.compass.javaapisamp.controller;

import com.compass.javaapisamp.helper.TestHelper;
import com.compass.javaapisamp.model.dto.ExAPIResponse;
import com.compass.javaapisamp.model.exception.APIException;
import com.compass.javaapisamp.model.exception.Errors;
import com.compass.javaapisamp.service.ExAPIService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ExAPIControllerTest {

    @MockBean
    private ExAPIService exAPIService;

    @Autowired
    private MockMvc mockMvc;

    /*
     * EXAPI
     */
    @Test
    void getExAPIDataSuccessTest() throws Exception {
        String resJson = TestHelper.readClassPathResource("exapi/success_response.json");

        ExAPIResponse res = new ExAPIResponse(
            List.of(
                new ExAPIResponse.MetaWeather(
                    "Sunny",
                    "2020-01-01",
                    12.0D,
                    776.0D,
                    75)
            ),
            "Tokyo",
            "Asia/Tokyo"
        );

        Mockito.when(exAPIService.getExWeather()).thenReturn(Mono.just(res));

        MvcResult mvcResult = mockMvc.perform(
                get("/get/apidata")
                    .header("Auth-Token", "auth-token"))
            .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
            .andExpect(header().exists("X-Request-ID"))
            .andExpect(status().isOk())
            .andExpect(content().json(resJson));
    }

    @Test
    void getExAPIDataErrorTest() throws Exception {
        String resJson = TestHelper.readClassPathResource("common/server_error_response.json");

        Mockito.when(exAPIService.getExWeather())
            .thenReturn(Mono.error(new APIException(Errors.INTERNAL_SERVER_ERROR)));

        MvcResult mvcResult = mockMvc.perform(
                get("/get/apidata")
                    .header("Auth-Token", "auth-token"))
            .andExpect(header().exists("X-Request-ID"))
            .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
            .andExpect(status().isInternalServerError())
            .andExpect(content().json(resJson));
    }

    @Test
    void getExAPIDataFilterTest() throws Exception {
        String resJson = TestHelper.readClassPathResource("common/unauthorized_error_response.json");

        mockMvc.perform(get("/get/apidata")
                .header("Auth-Token", "invalid-auth-token")
                .header("X-Request-ID", "AAA"))
            .andExpect(status().isUnauthorized())
            .andExpect(content().json(resJson))
            .andExpect(header().string("X-Request-ID", "AAA"));
    }
}
