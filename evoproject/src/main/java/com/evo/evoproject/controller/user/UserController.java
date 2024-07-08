package com.evo.evoproject.controller.user;

import com.evo.evoproject.domain.user.User;
import com.evo.evoproject.service.user.UserService;
import com.evo.evoproject.service.user.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private LoginService loginService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(User user, Model model) {
        if (userService.findUserByUserId(user.getUserId()) != null) {
            model.addAttribute("error", "이미 존재하는 아이디 입니다");
            return "register";
        }
        userService.registerUser(user);
        log.info("User registered successfully: {}", user.getUserId());
        return "redirect:/register?success";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(User user, Model model, HttpSession session) {
        log.info("Attempting to log in user: {}", user.getUserId());
        User loggedInUser = loginService.authenticate(user.getUserId(), user.getUserPw());
        if (loggedInUser != null) {
            session.setAttribute("loggedInUser", loggedInUser);
            log.info("User logged in successfully: {}", user.getUserId());
            return "redirect:/home"; // 로그인 성공 후 홈 페이지로 리디렉션
        } else {
            log.warn("Invalid username or password for user: {}", user.getUserId());
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }
}
