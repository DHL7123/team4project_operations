package com.evo.evoproject.service.user;

import com.evo.evoproject.domain.user.User;
import com.evo.evoproject.mapper.user.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isUserIdTaken(String userId) {
        return userMapper.findByUserId(userId) != null;
    }

    public void registerUser(User user) {
        user.setUserPw(passwordEncoder.encode(user.getUserPw()));
        userMapper.insertUser(user);
    }

    public User findUserByUserId(String userId) {
        return userMapper.findByUserId(userId);
    }

    public User findUserByUserEmail(String userEmail) {
        return userMapper.findByUserEmail(userEmail);
    }

    public void updateUserPassword(User user, String newPassword) {
        user.setUserPw(passwordEncoder.encode(newPassword));
        userMapper.updateUserPassword(user);
    }

    public boolean checkPassword(User user, String currentPassword) {
        return passwordEncoder.matches(currentPassword, user.getUserPw());
    }

    public void updateUserDetails(User user) {
        userMapper.updateUserDetails(user);
    }

    @Transactional
    public void deleteUser(String userId) {
        userMapper.deleteUser(userId);
    }





}
