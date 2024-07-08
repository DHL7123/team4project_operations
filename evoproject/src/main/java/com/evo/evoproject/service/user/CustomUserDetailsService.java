package com.evo.evoproject.service.user;

import com.evo.evoproject.domain.user.User;
import com.evo.evoproject.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 사용자 ID를 이용해 사용자 정보를 로드하는 메소드
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(userId);
        // 사용자가 존재하지 않을 경우 예외를 발생시킴
        if (user == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
        // Spring Security의 User 객체를 생성하여 반환
        return org.springframework.security.core.userdetails.User.withUsername(user.getUserId())
                .password(user.getUserPw())
                .authorities("USER")
                .build();
    }
}
