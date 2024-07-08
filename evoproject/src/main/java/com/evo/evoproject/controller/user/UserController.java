package com.evo.evoproject.controller.user;

import com.evo.evoproject.domain.user.User;
import com.evo.evoproject.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    // 회원가입 폼
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
    // 회원가입 처리 메소드
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        if (userService.findUserByUserId(user.getUserId()) != null) {
            model.addAttribute("error", "이미 존재하는 아이디 입니다");
            return "register"; // 중복된 아이디일 경우, 다시 회원가입 페이지로 이동
        }
        userService.registerUser(user);
        return "redirect:/login"; // 회원가입 성공 시 로그인 페이지로 리다이렉트
    }
    // 로그인 폼
    @GetMapping("/login")
    public String showLoginForm(Model model, @RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout) {
        if (error != null) {
            model.addAttribute("error", "아이디 또는 비밀번호가 잘못되었습니다.");
        }
        if (logout != null) {
            model.addAttribute("message", "성공적으로 로그아웃되었습니다.");
        }
        model.addAttribute("user", new User());
        return "login";
    }
    // 마이페이지 폼
    @GetMapping("/mypage")
    public String myPage(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        if (userId != null) {
            User user = userService.findUserByUserId(userId);
            model.addAttribute("user", user);
        }
        return "mypage";
    }
    // 로그아웃 메소드
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "redirect:/login";
    }
}
