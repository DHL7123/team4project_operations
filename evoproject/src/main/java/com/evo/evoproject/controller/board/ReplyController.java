package com.evo.evoproject.controller.board;

import com.evo.evoproject.domain.board.Board;
import com.evo.evoproject.domain.board.Reply;
import com.evo.evoproject.domain.user.User;
import com.evo.evoproject.service.board.ReplyService;
import com.evo.evoproject.service.board.BoardService;
import com.evo.evoproject.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/replies")
public class ReplyController {

    private final ReplyService replyService;
    private final BoardService boardService;
    private final UserService userService;

    @Autowired
    public ReplyController(ReplyService replyService, BoardService boardService, UserService userService) {
        this.replyService = replyService;
        this.boardService = boardService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public String createReply(@ModelAttribute Reply reply, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        User user = userService.findUserByUserId(userId);

        // 관리자만 댓글 작성 가능
        if (user == null || user.getIsAdmin() != 'Y') {
            return "redirect:/boards";
        }

        reply.setUserNo(user.getUserNo());
        replyService.createReply(reply);

        // 답변 상태 변경
        Board board = boardService.getBoardById(reply.getBoardNo());
        board.setIsAnswered('Y');
        boardService.updateBoard(board);

        return "redirect:/admin/boards";
    }

    @GetMapping("/board/{boardNo}")
    public String listReplies(@PathVariable int boardNo, Model model) {
        List<Reply> replies = replyService.getRepliesByBoardNo(boardNo);
        model.addAttribute("replies", replies);
        return "reply/list";
    }
}
