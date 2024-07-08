package com.evo.evoproject.service.user;

import com.evo.evoproject.domain.user.User;
import com.evo.evoproject.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(User user) {
        user.setUserPw(passwordEncoder.encode(user.getUserPw())); // 비밀번호 암호화
        user.setIsAdmin('N'); // is_admin 필드를 'N'으로 설정
        userRepository.insertUser(user);
    }

    public User findUserByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

}

