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
    // 새로운 사용자를 등록하는 메소드
    public void registerUser(User user) {
        // 비밀번호 암호화
        user.setUserPw(passwordEncoder.encode(user.getUserPw()));
        // is_admin 필드를 'N'으로 설정
        user.setIsAdmin('N');
        // 사용자 정보를 데이터베이스에 삽입
        userRepository.insertUser(user);
    }

    public User findUserByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

}

