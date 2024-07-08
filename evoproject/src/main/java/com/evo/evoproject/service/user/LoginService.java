package com.evo.evoproject.service.user;

import com.evo.evoproject.domain.user.User;
import com.evo.evoproject.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User authenticate(String userId, String userPw) {
        User user = userRepository.findByUserId(userId);
        if (user != null && passwordEncoder.matches(userPw, user.getUserPw())) {
            return user;
        }
        return null;
    }
}