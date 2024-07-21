package com.evo.evoproject.controller.board;

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
import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;
    private final ReplyService replyService;
    private final UserService userService;
    private final NaverImageUploadService imageUploadService;

    @Autowired
    public BoardController(BoardService boardService, ReplyService replyService, UserService userService, NaverImageUploadService imageUploadService) {
        this.boardService = boardService;
        this.replyService = replyService;
        this.userService = userService;
        this.imageUploadService = imageUploadService;
    }

    @GetMapping
    public String listBoards(HttpSession session, Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "0") int category) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        User user = userService.findUserByUserId(userId);
        int userNo = user.getUserNo();
        int offset = page * 10;
        List<Board> boards;
        int totalBoards;

        if (category == 0) {
            boards = boardService.getBoardsByUserNo(userNo, offset, 10);
            totalBoards = boardService.getUserBoardCount(userNo);
        } else {
            boards = boardService.getBoardsByCategory(userNo, category, offset, 10);
            totalBoards = boardService.getUserBoardCountByCategory(userNo, category);
        }

        int totalPages = (int) Math.ceil((double) totalBoards / 10);

        model.addAttribute("boards", boards);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentCategory", category);
        return "board/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("board", new Board());
        model.addAttribute("action", "create");
        return "board/form";
    }

    @PostMapping("/create")
    public String createBoard(@ModelAttribute Board board, @RequestParam("image") MultipartFile image, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        User user = userService.findUserByUserId(userId);
        board.setUserNo(user.getUserNo());

        try {
            boardService.createBoardWithImage(board, image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/boards";
    }

    @GetMapping("/edit/{boardNo}")
    public String showEditForm(@PathVariable int boardNo, HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        Board board = boardService.getBoardById(boardNo);
        User user = userService.findUserByUserId(userId);

        if (board.getUserNo() != user.getUserNo()) {
            return "redirect:/boards";
        }

        model.addAttribute("board", board);
        model.addAttribute("action", "edit");
        return "board/form";
    }

    @PostMapping("/edit/{boardNo}")
    public String updateBoard(@PathVariable int boardNo, @ModelAttribute Board board, @RequestParam("image") MultipartFile image, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        User user = userService.findUserByUserId(userId);

        if (board.getUserNo() != user.getUserNo()) {
            return "redirect:/boards";
        }

        try {
            boardService.updateBoardWithImage(board, image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/boards/view/" + boardNo;
    }

    @GetMapping("/delete/{boardNo}")
    public String deleteBoard(@PathVariable int boardNo, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        Board board = boardService.getBoardById(boardNo);
        User user = userService.findUserByUserId(userId);

        if (board.getUserNo() != user.getUserNo()) {
            return "redirect:/boards";
        }

        boardService.deleteBoard(boardNo);
        return "redirect:/boards";
    }

    @GetMapping("/view/{boardNo}")
    public String viewBoard(@PathVariable int boardNo, HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        Board board = boardService.getBoardById(boardNo);
        User user = userService.findUserByUserId(userId);

        boolean isAuthor = board.getUserNo() == user.getUserNo();
        List<Reply> replies = replyService.getRepliesByBoardNo(boardNo);

        model.addAttribute("board", board);
        model.addAttribute("isAuthor", isAuthor);
        model.addAttribute("replies", replies);
        return "board/view";
    }
}
