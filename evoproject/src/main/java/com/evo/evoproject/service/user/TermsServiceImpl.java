package com.evo.evoproject.service.user;

import com.evo.evoproject.mapper.user.TermsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Service
public class TermsServiceImpl implements TermsService {

    @Autowired
    private TermsMapper termsMapper;

    @Override
    public Map<String, String> getLatestTerms(String termsType) {
        Map<String, Object> params = new HashMap<>();
        params.put("termsType", termsType);
        params.put("content", ""); // 초기화된 OUT 파라미터
        params.put("version", null); // 초기화된 OUT 파라미터

        termsMapper.getLatestTerms(params);

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("content", (String) params.get("content"));

        java.sql.Date sqlDate = (java.sql.Date) params.get("version");
        if (sqlDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            resultMap.put("version", sdf.format(sqlDate));
        } else {
            resultMap.put("version", null);
        }

        return resultMap;
    }
}
