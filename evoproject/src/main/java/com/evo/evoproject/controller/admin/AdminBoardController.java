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
    public String listBoards(HttpSession session, Model model, @RequestParam(defaultValue = "0") int page) {
        String userId = (String) session.getAttribute("userId");
        User user = userService.findUserByUserId(userId);

        if (user == null || user.getIsAdmin() != 'Y') {
            return "redirect:/login";
        }

        int offset = page * 10;
        List<Board> boards = boardService.getAllBoards(offset, 10);
        int totalBoards = boardService.getTotalBoardCount();
        int totalPages = (int) Math.ceil((double) totalBoards / 10);

        model.addAttribute("boards", boards);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
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
//    @GetMapping("/edit/{boardNo}")
//    public String showEditForm(@PathVariable int boardNo, HttpSession session, Model model) {
//        String userId = (String) session.getAttribute("userId");
//        User user = userService.findUserByUserId(userId);
//
//        if (user == null || user.getIsAdmin() != 'Y') {
//            return "redirect:/login";
//        }
//
//        Board board = boardService.getBoardById(boardNo);
//        model.addAttribute("board", board);
//        model.addAttribute("action", "edit");
//        return "admin/board_form";
//    }
//
//    @PostMapping("/edit/{boardNo}")
//    public String updateBoard(@PathVariable int boardNo, @ModelAttribute Board board, @RequestParam("image") MultipartFile image, HttpSession session) {
//        String userId = (String) session.getAttribute("userId");
//        User user = userService.findUserByUserId(userId);
//
//        if (user == null || user.getIsAdmin() != 'Y') {
//            return "redirect:/login";
//        }
//
//        try {
//            boardService.updateBoardWithImage(board, image);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return "redirect:/admin/boards";
//    }
//@GetMapping("/create")
//public String showCreateForm(Model model) {
//    model.addAttribute("board", new Board());
//    model.addAttribute("action", "create");
//    return "admin/board_form";
//}
//
//    @PostMapping("/create")
//    public String createBoard(@ModelAttribute Board board, @RequestParam("image") MultipartFile image, HttpSession session) {
//        String userId = (String) session.getAttribute("userId");
//        User user = userService.findUserByUserId(userId);
//
//        if (user == null || user.getIsAdmin() != 'Y') {
//            return "redirect:/login";
//        }
//
//        board.setUserNo(user.getUserNo());
//
//        try {
//            boardService.createBoardWithImage(board, image);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return "redirect:/admin/boards";
//    }
}
