package com.evo.evoproject.controller.admin;

import com.evo.evoproject.domain.board.Board;
import com.evo.evoproject.domain.board.Reply;
import com.evo.evoproject.domain.user.User;
import com.evo.evoproject.service.board.BoardService;
import com.evo.evoproject.service.board.ReplyService;
import com.evo.evoproject.service.board.NaverImageUploadService;
import com.evo.evoproject.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin/boards")
public class AdminBoardController {

    private final BoardService boardService;
    private final UserService userService;
    private final ReplyService replyService;
    private final NaverImageUploadService imageUploadService;

    @Autowired
    public AdminBoardController(BoardService boardService, UserService userService, ReplyService replyService, NaverImageUploadService imageUploadService) {
        this.boardService = boardService;
        this.userService = userService;
        this.replyService = replyService;
        this.imageUploadService = imageUploadService;
    }

    @GetMapping
    public String listAdminBoards(HttpSession session, Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "0") int category) {
        String userId = (String) session.getAttribute("userId");

        User user = userService.findUserByUserId(userId);


        if (user == null || user.getIsAdmin() != 'Y') {
            return "redirect:/login";
        }

        int offset = page * 10;
        List<Board> boards;
        int totalBoards;

        if (category == 0) {
            boards = boardService.getAllBoardsWithUser(offset, 10);
            totalBoards = boardService.getTotalBoardCount();
        } else {
            boards = boardService.getAllBoardsByCategory(category, offset, 10);
            totalBoards = boardService.getTotalBoardCountByCategory(category);
        }

        int totalPages = (int) Math.ceil((double) totalBoards / 10);


        model.addAttribute("boards", boards);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentCategory", category);
        return "admin/manageBoard";
    }



    @GetMapping("/delete/{boardNo}")
    public String deleteBoard(@PathVariable int boardNo, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        User user = userService.findUserByUserId(userId);

        if (user == null || user.getIsAdmin() != 'Y') {
            return "redirect:/login";
        }

        boardService.deleteBoard(boardNo);
        return "redirect:/admin/boards";
    }

    @GetMapping("/view/{boardNo}")
    public String viewBoard(@PathVariable int boardNo, HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        User user = userService.findUserByUserId(userId);

        if (user == null || user.getIsAdmin() != 'Y') {
            return "redirect:/login";
        }

        Board board = boardService.getBoardById(boardNo);
        List<Reply> replies = replyService.getRepliesByBoardNo(boardNo);
        model.addAttribute("board", board);
        model.addAttribute("replies", replies);
        return "admin/manageView";
    }

    @GetMapping("/deleteReply/{replyNo}")
    public String deleteReply(@PathVariable int replyNo, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        User user = userService.findUserByUserId(userId);

        Reply reply = replyService.getReplyById(replyNo);
        if (reply == null) {
            return "redirect:/admin/boards?error=replyNotFound";
        }

        // 관리자만 댓글 삭제 가능
        if (user.getIsAdmin() != 'Y') {
            return "redirect:/admin/boards/view/" + reply.getBoardNo();
        }

        replyService.deleteReply(replyNo);

        Board board = boardService.getBoardById(reply.getBoardNo());
        board.setIsAnswered('N');
        boardService.updateBoard(board);

        return "redirect:/admin/boards/view/" + reply.getBoardNo();
    }
}
