package com.evo.evoproject.repository.user;

import com.evo.evoproject.domain.user.User;
import org.apache.ibatis.annotations.Mapper;



@Mapper
public interface UserRepository {

    void insertUser(User user);


    User findByUserId(String userId);
}
