package com.evo.evoproject.mapper.user;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface TermsMapper {
    void getLatestTerms(Map<String, Object> params);
}
