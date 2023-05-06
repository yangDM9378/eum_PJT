package com.eumpyo.eum.Controller;

import com.eumpyo.eum.api.controller.GroupController;
import com.eumpyo.eum.api.request.GroupAddReq;
import com.eumpyo.eum.api.service.GroupService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class GroupControllerTest {

    private final String baseUrl = "/groups/";

    @Mock
    GroupService groupService;

    @InjectMocks
    GroupController groupController;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUpMockMvc() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(groupController)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    /**
     * PathVariable이 잘못된 타입이 왔을 때 에러 발생
     */
    @Test
    @DisplayName("그룹 조회 (성공)")
    void groupDetailsvalidParameter() throws Exception {
        // given
        String inputParameter = "1";
        groupService.addGroup(null, GroupAddReq.builder()
                .name("그룹이름")
                .description("그룹설명")
                .build());

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .get(baseUrl + inputParameter)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").exists())
                .andDo(print());
    }

    /**
     * PathVariable이 잘못된 타입이 왔을 때 에러 발생
     */
    @Test
    @DisplayName("그룹 조회 (실패) by 잘못된 Parameters")
    void groupDetailsInValidParameter() throws Exception {
        // given
        String inputParameter = "감자전";

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .get(baseUrl + inputParameter)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        System.out.println();
        resultActions.andExpect(status().isBadRequest());
    }
}
