package com.evo.evoproject.domain.board;

import lombok.Data;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
public class Board {
    private Integer  boardNo;
    private int userNo;
    private Integer orderNo;
    private Integer imageId;
    private Integer categoryId;
    private String boardTitle;
    private String boardContent;
    private Timestamp boardTimestamp;
    private char isAnswered ='N';
    private String imageUrl; // 이미지 URL 필드 추가

    public String getAnsweredStatus() {
        return isAnswered == 'Y' ? "답변완료" : "답변대기";
    }
}