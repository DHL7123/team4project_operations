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

    // 약관 동의 폼
    @GetMapping("/terms")
    public String showTermsForm() {
        return "terms";
    }
    // 약관 동의 처리
    @PostMapping("/terms")
    public String acceptTerms() {
        return "redirect:/register";
    }
    // 회원가입 폼
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
    // 회원가입 처리 메소드
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        if (userService.isUserIdTaken(user.getUserId())) {
            model.addAttribute("error", "이미 존재하는 아이디입니다.");
            return "register";
        }
        userService.registerUser(user);
        return "redirect:/login";
    }

    // 중복 아이디 체크 메소드
    @GetMapping("/check-username")
    @ResponseBody
    public Map<String, Object> checkUsername(@RequestParam String userId) {
        boolean isTaken = userService.isUserIdTaken(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("available", !isTaken);
        response.put("message", isTaken ? "이미 존재하는 아이디입니다." : "사용 가능한 아이디입니다.");
        return response;
    }

    // 로그인 폼
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // 마이페이지 폼
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
    // 로그아웃 메소드
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "redirect:/login";
    }
}
