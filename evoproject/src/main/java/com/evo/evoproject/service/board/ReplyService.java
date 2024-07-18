package com.evo.evoproject.service.board;

import com.evo.evoproject.domain.board.Reply;
import com.evo.evoproject.mapper.board.ReplyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplyService {

    private final ReplyMapper replyMapper;

    @Autowired
    public ReplyService(ReplyMapper replyMapper) {
        this.replyMapper = replyMapper;
    }

    public void createReply(Reply reply) {
        replyMapper.insertReply(reply);
    }

    public List<Reply> getRepliesByBoardNo(int boardNo) {
        return replyMapper.findRepliesByBoardNo(boardNo);
    }

    public void deleteReply(int replyNo) {
        replyMapper.deleteReply(replyNo);
    }


    public Reply getReplyById(int replyNo) {
        return replyMapper.findReplyById(replyNo);
    }
}
