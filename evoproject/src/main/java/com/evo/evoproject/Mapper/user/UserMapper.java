package com.evo.evoproject.Mapper.user;

import com.evo.evoproject.domain.user.User;
import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {

    void deleteUser(String userId);

    void insertUser(User user);

    User findByUserId(@Param("userId") String userId);

    User findByUserEmail(@Param("userEmail") String userEmail);

    void updateUserPassword(User user);

    void updateUserDetails(User user);
}
