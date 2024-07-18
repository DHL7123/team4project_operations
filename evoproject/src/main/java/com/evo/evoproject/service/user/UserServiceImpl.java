package com.evo.evoproject.service.user;

import com.evo.evoproject.domain.user.User;
import com.evo.evoproject.Mapper.user.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    /**
     * 주어진 사용자 ID로 사용자를 로드하는 메소드.
     * Spring Security의 UserDetailsService 인터페이스를 구현.
     *
     * @param userId 사용자 ID
     * @return UserDetails 사용자 세부 정보
     * @throws UsernameNotFoundException 사용자를 찾을 수 없는 경우 예외 발생
     */

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userMapper.findByUserId(userId);
        if (user == null) {
            // 사용자 ID로 사용자를 찾을 수 없는 경우 예외 발생
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
        // Spring Security의 User 객체를 반환
        String role = user.getIsAdmin() == 'Y' ? "ADMIN" : "USER";
        return org.springframework.security.core.userdetails.User.withUsername(user.getUserId())
                .password(user.getUserPw())
                .authorities(Collections.singletonList(() -> role))
                .build();
    }
}
