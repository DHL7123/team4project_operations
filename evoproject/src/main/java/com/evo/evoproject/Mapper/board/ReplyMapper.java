package com.evo.evoproject.Mapper.board;

import com.evo.evoproject.domain.board.Reply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ReplyMapper {

    void insertReply(Reply reply);

    List<Reply> findRepliesByBoardNo(@Param("boardNo") int boardNo);

    void deleteReply(@Param("replyNo") int replyNo);

    Reply findReplyById(int replyNo);
}
