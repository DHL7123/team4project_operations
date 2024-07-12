package com.evo.evoproject.controller.user;

import com.evo.evoproject.domain.user.User;
import com.evo.evoproject.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 약관 동의 폼을 보여주는 메소드.
     *
     * @return 약관 동의 페이지 뷰 이름
     */
    @GetMapping("/terms")
    public String showTermsForm() {
        return "terms";
    }

    /**
     * 약관 동의를 처리하는 메소드.
     *
     * @return 회원가입 페이지로 리다이렉트
     */
    @PostMapping("/terms")
    public String acceptTerms() {
        return "redirect:/register";
    }

    /**
     * 회원가입 폼을 보여주는 메소드.
     *
     * @param model 모델 객체
     * @return 회원가입 페이지 뷰 이름
     */
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    /**
     * 회원가입을 처리하는 메소드.
     *
     * @param user 회원가입할 사용자 정보
     * @param model 모델 객체
     * @return 성공 시 로그인 페이지로 리다이렉트, 실패 시 회원가입 페이지로 다시 이동
     */
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        if (userService.isUserIdTaken(user.getUserId())) {
            model.addAttribute("error", "이미 존재하는 아이디입니다.");
            return "register";
        }
        userService.registerUser(user);
        return "redirect:/login";
    }

    /**
     * 아이디 중복을 체크하는 메소드.
     *
     * @param userId 체크할 사용자 아이디
     * @return 중복 여부와 메시지를 담은 맵
     */
    @GetMapping("/check-username")
    @ResponseBody
    public Map<String, Object> checkUsername(@RequestParam String userId) {
        boolean isTaken = userService.isUserIdTaken(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("available", !isTaken);
        response.put("message", isTaken ? "이미 존재하는 아이디입니다." : "사용 가능한 아이디입니다.");
        return response;
    }

    /**
     * 로그인 폼을 보여주는 메소드.
     *
     * @return 로그인 페이지 뷰 이름
     */
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    /**
     * 마이페이지를 보여주는 메소드.
     *
     * @param session 세션 객체
     * @param model 모델 객체
     * @return 마이페이지 뷰 이름
     */
    @GetMapping("/mypage")
    public String myPage(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            // userId가 없을 경우 로그인 페이지로 리디렉션
            return "redirect:/login";
        }
        User user = userService.findUserByUserId(userId);
        model.addAttribute("user", user);
        return "mypage";
    }

    /**
     * 로그아웃을 처리하는 메소드.
     *
     * @param session 세션 객체
     * @return 로그인 페이지로 리다이렉트
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "redirect:/login";
    }
}
