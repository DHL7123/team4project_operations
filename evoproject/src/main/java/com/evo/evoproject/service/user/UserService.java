package com.evo.evoproject.service.user;

import com.evo.evoproject.domain.user.User;
import com.evo.evoproject.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isUsernameTaken(String userId) {
        return userRepository.findByUserId(userId) != null;
    }

    public void registerUser(User user) {
        user.setUserPw(passwordEncoder.encode(user.getUserPw()));
        userRepository.insertUser(user);
    }

    public User findUserByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }
    public User findUserByUserEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail);
    }

    public void updateUserPassword(User user, String newPassword) {
        user.setUserPw(passwordEncoder.encode(newPassword));
        userRepository.updateUserPassword(user);
    }
    public boolean checkPassword(User user, String currentPassword) {
        return passwordEncoder.matches(currentPassword, user.getUserPw());
    }

    public void updateUserDetails(User user) {
        userRepository.updateUserDetails(user);
    }


}
