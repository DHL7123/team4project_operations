package com.evo.evoproject.controller.user;

import com.evo.evoproject.domain.user.User;
import com.evo.evoproject.service.cart.CartService;
import com.evo.evoproject.service.user.UserService;
import com.evo.evoproject.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@Slf4j
@WebMvcTest(FindController.class)
class FindControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JavaMailSender mailSender;

    @InjectMocks
    private FindController findController;

    @MockBean
    private CartService cartService;

    private static ExcelUtil excelUtil;

    @BeforeAll
    public static void setupAll() {
        excelUtil = new ExcelUtil();
    }
    @AfterAll
    public static void tearDownAll() throws IOException {
        excelUtil.saveExcelFile();
    }

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(findController)
                .alwaysDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .build();
    }

    @Test
    @DisplayName("ID 찾기 요청 테스트 - 성공")
    void testFindIdSuccess() throws Exception {
        log.info("ID 찾기 요청 테스트 - 성공: 테스트 시작");
        User user = new User();
        user.setUserName("testuser");
        user.setUserEmail("test@example.com");
        user.setUserId("testid");

        when(userService.findUserByUserEmail("test@example.com")).thenReturn(user);

        MvcResult result = mockMvc.perform(post("/find-id")
                        .param("userName", "testuse")
                        .param("userEmail", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("testid"))
                .andReturn();

        log.info("응답 내용: {}", result.getResponse().getContentAsString());
        log.info("ID 찾기 요청 테스트 - 성공: 테스트 종료");

        excelUtil.addTestResult("TC001", "ID 찾기 요청 테스트 - 성공", "FindController", "testFindIdSuccess",
                "userName=testuser, userEmail=test@example.com", "testid", result.getResponse().getContentAsString(), "성공", "");
    }

    @Test
    @DisplayName("ID 찾기 요청 테스트 - 실패")
    void testFindIdFailure() throws Exception {
        log.info("ID 찾기 요청 테스트 - 실패: 테스트 시작");
        when(userService.findUserByUserEmail("test@example.com")).thenReturn(null);

        MvcResult result = mockMvc.perform(post("/find-id")
                        .param("userName", "testuser")
                        .param("userEmail", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("등록된 이메일이 없습니다."))
                .andReturn();

        log.info("응답 내용: {}", result.getResponse().getContentAsString());
        log.info("ID 찾기 요청 테스트 - 실패: 테스트 종료");

        excelUtil.addTestResult("TC002", "ID 찾기 요청 테스트 - 실패", "FindController", "testFindIdFailure",
                "userName=testuser, userEmail=test@example.com", "등록된 이메일이 없습니다.", result.getResponse().getContentAsString(), "실패", "");
    }

    @Test
    @DisplayName("비밀번호 찾기 요청 테스트 - 성공")
    void testFindPasswordSuccess() throws Exception {
        log.info("비밀번호 찾기 요청 테스트 - 성공: 테스트 시작");
        User user = new User();
        user.setUserName("testuser");
        user.setUserEmail("test@example.com");
        user.setUserId("testid");

        when(userService.findUserByUserEmail("test@example.com")).thenReturn(user);

        doNothing().when(userService).updateUserPassword(any(User.class), anyString());
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        MvcResult result = mockMvc.perform(post("/find-password")
                        .param("userName", "testuser")
                        .param("userEmail", "test@example.com")
                        .param("userId", "testid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("임시 비밀번호가 이메일로 발송되었습니다."))
                .andReturn();

        log.info("응답 내용: {}", result.getResponse().getContentAsString());
        log.info("비밀번호 찾기 요청 테스트 - 성공: 테스트 종료");

        excelUtil.addTestResult("TC003", "비밀번호 찾기 요청 테스트 - 성공", "FindController", "testFindPasswordSuccess",
                "userName=testuser, userEmail=test@example.com, userId=testid", "임시 비밀번호가 이메일로 발송되었습니다.", result.getResponse().getContentAsString(), "성공", "");
    }

    @Test
    @DisplayName("비밀번호 찾기 요청 테스트 - 실패")
    void testFindPasswordFailure() throws Exception {
        log.info("비밀번호 찾기 요청 테스트 - 실패: 테스트 시작");
        when(userService.findUserByUserEmail("test@example.com")).thenReturn(null);

        MvcResult result = mockMvc.perform(post("/find-password")
                        .param("userName", "testuser")
                        .param("userEmail", "test@example.com")
                        .param("userId", "testid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("등록된 이메일이 없습니다."))
                .andReturn();

        log.info("응답 내용: {}", result.getResponse().getContentAsString());
        log.info("비밀번호 찾기 요청 테스트 - 실패: 테스트 종료");

        excelUtil.addTestResult("TC004", "비밀번호 찾기 요청 테스트 - 실패", "FindController", "testFindPasswordFailure",
                "userName=testuser, userEmail=test@example.com, userId=testid", "등록된 이메일이 없습니다.", result.getResponse().getContentAsString(), "실패", "");
    }

    @Test
    @DisplayName("비밀번호 변경 요청 테스트 - 성공")
    void testChangePasswordSuccess() throws Exception {
        log.info("비밀번호 변경 요청 테스트 - 성공: 테스트 시작");
        User user = new User();
        user.setUserId("testid");

        when(userService.findUserByUserId("testid")).thenReturn(user);
        when(userService.checkPassword(user, "currentPassword")).thenReturn(true);

        doNothing().when(userService).updateUserPassword(any(User.class), anyString());

        MvcResult result = mockMvc.perform(post("/change-password")
                        .param("userId", "testid")
                        .param("currentPassword", "currentPassword")
                        .param("newPassword", "newPassword"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("비밀번호가 성공적으로 변경되었습니다."))
                .andReturn();

        log.info("응답 내용: {}", result.getResponse().getContentAsString());
        log.info("비밀번호 변경 요청 테스트 - 성공: 테스트 종료");

        excelUtil.addTestResult("TC005", "비밀번호 변경 요청 테스트 - 성공", "FindController", "testChangePasswordSuccess",
                "userId=testid, currentPassword=currentPassword, newPassword=newPassword", "비밀번호가 성공적으로 변경되었습니다.", result.getResponse().getContentAsString(), "성공", "");
    }

    @Test
    @DisplayName("비밀번호 변경 요청 테스트 - 실패")
    void testChangePasswordFailure() throws Exception {
        log.info("비밀번호 변경 요청 테스트 - 실패: 테스트 시작");
        User user = new User();
        user.setUserId("testid");

        when(userService.findUserByUserId("testid")).thenReturn(user);
        when(userService.checkPassword(user, "currentPassword")).thenReturn(false);

        MvcResult result = mockMvc.perform(post("/change-password")
                        .param("userId", "testid")
                        .param("currentPassword", "currentPassword")
                        .param("newPassword", "newPassword"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("현재 비밀번호가 일치하지 않습니다."))
                .andReturn();

        log.info("응답 내용: {}", result.getResponse().getContentAsString());
        log.info("비밀번호 변경 요청 테스트 - 실패: 테스트 종료");

        excelUtil.addTestResult("TC006", "비밀번호 변경 요청 테스트 - 실패", "FindController", "testChangePasswordFailure",
                "userId=testid, currentPassword=currentPassword, newPassword=newPassword", "현재 비밀번호가 일치하지 않습니다.", result.getResponse().getContentAsString(), "실패", "");
    }

    @Test
    @DisplayName("사용자 정보 업데이트 요청 테스트 - 성공")
    void testUpdateUserSuccess() throws Exception {
        log.info("사용자 정보 업데이트 요청 테스트 - 성공: 테스트 시작");
        User user = new User();
        user.setUserId("testid");

        when(userService.findUserByUserId("testid")).thenReturn(user);
        doNothing().when(userService).updateUserDetails(any(User.class));

        MvcResult result = mockMvc.perform(post("/update-user")
                        .param("userId", "testid")
                        .param("userEmail", "new@example.com")
                        .param("userAddress1", "address1")
                        .param("userAddress2", "address2")
                        .param("userPhone", "1234567890")
                        .param("userMarketing", "Y"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("사용자 정보가 성공적으로 수정되었습니다."))
                .andReturn();

        log.info("응답 내용: {}", result.getResponse().getContentAsString());
        log.info("사용자 정보 업데이트 요청 테스트 - 성공: 테스트 종료");

        excelUtil.addTestResult("TC007", "사용자 정보 업데이트 요청 테스트 - 성공", "FindController", "testUpdateUserSuccess",
                "userId=testid, userEmail=new@example.com, userAddress1=address1, userAddress2=address2, userPhone=1234567890, userMarketing=Y", "사용자 정보가 성공적으로 수정되었습니다.", result.getResponse().getContentAsString(), "성공", "");
    }

    @Test
    @DisplayName("사용자 정보 업데이트 요청 테스트 - 실패")
    void testUpdateUserFailure() throws Exception {
        log.info("사용자 정보 업데이트 요청 테스트 - 실패: 테스트 시작");
        when(userService.findUserByUserId("testid")).thenReturn(null);

        MvcResult result = mockMvc.perform(post("/update-user")
                        .param("userId", "testid")
                        .param("userEmail", "new@example.com")
                        .param("userAddress1", "address1")
                        .param("userAddress2", "address2")
                        .param("userPhone", "1234567890")
                        .param("userMarketing", "Y"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("사용자를 찾을 수 없습니다."))
                .andReturn();

        log.info("응답 내용: {}", result.getResponse().getContentAsString());
        log.info("사용자 정보 업데이트 요청 테스트 - 실패: 테스트 종료");

        excelUtil.addTestResult("TC008", "사용자 정보 업데이트 요청 테스트 - 실패", "FindController", "testUpdateUserFailure",
                "userId=testid, userEmail=new@example.com, userAddress1=address1, userAddress2=address2, userPhone=1234567890, userMarketing=Y", "사용자를 찾을 수 없습니다.", result.getResponse().getContentAsString(), "실패", "");
    }
}