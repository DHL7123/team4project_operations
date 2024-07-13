package com.evo.evoproject.domain.board;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class Reply {
    private int replyNo;
    private int boardNo;
    private int userNo;
    private String replyContent;
    private Timestamp replyTimestamp;

}
