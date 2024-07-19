package com.evo.evoproject.controller.user;

import com.evo.evoproject.config.SecurityConfig;
import com.evo.evoproject.config.MybatisConfig;
import com.evo.evoproject.domain.user.User;
import com.evo.evoproject.service.user.TermsService;
import com.evo.evoproject.service.user.UserService;
import com.evo.evoproject.service.cart.CartService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@WebMvcTest(UserController.class)
@ContextConfiguration(classes = {SecurityConfig.class, MybatisConfig.class})
@ComponentScan(basePackages = "com.evo.evoproject")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private TermsService termsService;

    @MockBean
    private CartService cartService;

    @MockBean
    private JavaMailSender mailSender; // 추가: FindController에 필요한 JavaMailSender 모의 빈

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("올바른 약관 페이지 요청 테스트")
    void testShowTermsForm() throws Exception {
        log.info("약관 페이지 요청을 설정합니다.");
        when(termsService.getLatestTerms("service_term")).thenReturn(Map.of("content", "Service Terms"));
        when(termsService.getLatestTerms("privacy_policy")).thenReturn(Map.of("content", "Privacy Policy"));
        when(termsService.getLatestTerms("data_delegation_consent")).thenReturn(Map.of("content", "Data Delegation Consent"));
        when(termsService.getLatestTerms("marketing_consent")).thenReturn(Map.of("content", "Marketing Consent"));

        log.info("GET 요청을 통해 /terms 페이지를 호출합니다.");
        mockMvc.perform(get("/terms"))
                .andExpect(status().isOk())
                .andExpect(view().name("terms"))
                .andExpect(model().attributeExists("serviceTerms", "privacyPolicy", "dataDelegationConsent", "marketingConsent"));

        log.info("약관 페이지 요청이 성공적으로 수행되었습니다.");
    }

    @Test
    @DisplayName("약관 수락 요청 테스트")
    void testAcceptTerms() throws Exception {
        log.info("POST 요청을 통해 /terms 페이지를 호출하여 약관을 수락합니다.");
        mockMvc.perform(post("/terms"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"));

        log.info("약관 수락 요청이 성공적으로 수행되었습니다.");
    }

    @Test
    @DisplayName("회원가입 페이지 요청 테스트")
    void testShowRegisterForm() throws Exception {
        log.info("GET 요청을 통해 /register 페이지를 호출하여 회원가입 페이지를 요청합니다.");
        mockMvc.perform(get("/register"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/terms"));

        log.info("회원가입 페이지 요청이 성공적으로 수행되었습니다.");
    }

    @Test
    @DisplayName("회원가입 요청 테스트")
    void testRegisterUser() throws Exception {
        log.info("POST 요청을 통해 /register 페이지를 호출하여 회원가입을 요청합니다.");
        when(userService.isUserIdTaken("testuser")).thenReturn(false);

        mockMvc.perform(post("/register")
                        .param("userId", "testuser")
                        .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        log.info("회원가입 요청이 성공적으로 수행되었습니다.");
    }

    @Test
    @DisplayName("아이디 중복 확인 요청 테스트")
    void testCheckUsername() throws Exception {
        log.info("GET 요청을 통해 /check-username 페이지를 호출하여 아이디 중복 확인을 요청합니다.");
        when(userService.isUserIdTaken("xofid1")).thenReturn(false);

        mockMvc.perform(get("/check-username")
                        .param("userId", "xofid1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.message").value("사용 가능한 아이디입니다."));

        log.info("아이디 중복 확인 요청이 성공적으로 수행되었습니다.");
    }

    @Test
    @DisplayName("아이디 중복 확인 요청 테스트 - 중복된 아이디 있음")
    void testCheckUsernameDuplicate() throws Exception {
        log.info("GET 요청을 통해 /check-username 페이지를 호출하여 중복된 아이디 확인을 요청합니다.");
        when(userService.isUserIdTaken("xofid")).thenReturn(true);

        mockMvc.perform(get("/check-username")
                        .param("userId", "xofid1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(false))
                .andExpect(jsonPath("$.message").value("이미 존재하는 아이디입니다."));

        log.info("중복된 아이디 확인 요청이 성공적으로 수행되었습니다.");
    }

    @Test
    @DisplayName("로그인 페이지 요청 테스트")
    void testShowLoginForm() throws Exception {
        log.info("GET 요청을 통해 /login 페이지를 호출하여 로그인 페이지를 요청합니다.");
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));

        log.info("로그인 페이지 요청이 성공적으로 수행되었습니다.");
    }

    @Test
    @DisplayName("로그인하지 않은 상태에서 마이페이지 요청 테스트")
    void testMyPageWithoutLogin() throws Exception {
        log.info("GET 요청을 통해 /mypage 페이지를 호출하여 로그인하지 않은 상태에서 마이페이지를 요청합니다.");
        mockMvc.perform(get("/mypage"))
                .andExpect(status().is3xxRedirection()) // 로그인 페이지로 리디렉션 예상
                .andExpect(redirectedUrlPattern("/login")); // 로그인 페이지로 리디렉션 확인

        log.info("로그인하지 않은 상태에서 마이페이지 요청이 성공적으로 수행되었습니다.");
    }

    @Test
    @DisplayName("마이페이지 요청 테스트")
    void testMyPage() throws Exception {
        log.info("GET 요청을 통해 /mypage 페이지를 호출하여 마이페이지를 요청합니다.");
        when(userService.findUserByUserId("testuser")).thenReturn(new User());

        mockMvc.perform(get("/mypage").sessionAttr("userId", "testuser"))
                .andExpect(status().isOk())
                .andExpect(view().name("mypage"))
                .andExpect(model().attributeExists("user"));

        log.info("마이페이지 요청이 성공적으로 수행되었습니다.");
    }

    @Test
    @DisplayName("로그아웃 요청 테스트")
    void testLogout() throws Exception {
        log.info("GET 요청을 통해 /logout 페이지를 호출하여 로그아웃을 요청합니다.");
        mockMvc.perform(get("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/")); // 실제 리디렉션 URL에 맞게 수정

        log.info("로그아웃 요청이 성공적으로 수행되었습니다.");
    }

    @Test
    @DisplayName("로그아웃 요청 테스트(로그인하지 않은 상태)")
    void testLogoutWithoutLogin() throws Exception {
        log.info("GET 요청을 통해 /logout 페이지를 호출하여 로그인하지 않은 상태에서 로그아웃을 요청합니다.");
        mockMvc.perform(get("/logout"))
                .andExpect(status().isUnauthorized()); // 로그인하지 않은 상태에서 로그아웃 시도 시 401 상태 코드 기대

        log.info("로그인하지 않은 상태에서 로그아웃 요청이 성공적으로 수행되었습니다.");
    }

    @Test
    @DisplayName("회원탈퇴 페이지 요청 테스트")
    void testWithdrawalForm() throws Exception {
        log.info("GET 요청을 통해 /withdrawal 페이지를 호출하여 회원탈퇴 페이지를 요청합니다.");
        when(userService.findUserByUserId("testuser")).thenReturn(new User());

        mockMvc.perform(get("/withdrawal").sessionAttr("userId", "testuser"))
                .andExpect(status().isOk())
                .andExpect(view().name("withdrawal"))
                .andExpect(model().attributeExists("user"));

        log.info("회원탈퇴 페이지 요청이 성공적으로 수행되었습니다.");
    }

    @Test
    @DisplayName("회원탈퇴 요청 테스트")
    void testDeleteUser() throws Exception {
        log.info("POST 요청을 통해 /delete-user 페이지를 호출하여 회원탈퇴를 요청합니다.");
        User user = new User();
        user.setUserId("testuser");
        when(userService.findUserByUserId("testuser")).thenReturn(user);
        when(userService.checkPassword(user, "password")).thenReturn(true);

        mockMvc.perform(post("/delete-user")
                        .param("userId", "testuser")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("회원탈퇴가 완료되었습니다."));

        log.info("회원탈퇴 요청이 성공적으로 수행되었습니다.");
    }

    @Test
    @DisplayName("잘못된 비밀번호로 회원탈퇴 요청 테스트")
    void testDeleteUserWrongPassword() throws Exception {
        log.info("POST 요청을 통해 /delete-user 페이지를 호출하여 잘못된 비밀번호로 회원탈퇴를 요청합니다.");
        User user = new User();
        user.setUserId("testuser");

        when(userService.findUserByUserId("testuser")).thenReturn(user);
        when(userService.checkPassword(user, "wrongpassword")).thenReturn(false);

        mockMvc.perform(post("/delete-user")
                        .param("userId", "testuser")
                        .param("password", "wrongpassword"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("비밀번호가 일치하지 않습니다."));

        log.info("잘못된 비밀번호로 회원탈퇴 요청이 성공적으로 수행되었습니다.");
    }
}
