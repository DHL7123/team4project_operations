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
    private final UserService userService;
    private final ReplyService replyService;
    private final NaverImageUploadService imageUploadService;

    @Autowired
    public BoardController(BoardService boardService, UserService userService , ReplyService replyService , NaverImageUploadService imageUploadService) {
        this.boardService = boardService;
        this.userService = userService;
        this.replyService = replyService;
        this.imageUploadService = imageUploadService;
    }

    @GetMapping
    public String listBoards(HttpSession session, Model model, @RequestParam(defaultValue = "0") int page) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        User user = userService.findUserByUserId(userId);
        int userNo = user.getUserNo();
        int offset = page * 10;
        List<Board> boards;
        int totalBoards;

        if (user.getIsAdmin() == 'Y') {
            boards = boardService.getAllBoards(offset, 10);
            totalBoards = boardService.getTotalBoardCount();
        } else {
            boards = boardService.getBoardsByUserNo(userNo, offset, 10);
            totalBoards = boardService.getUserBoardCount(userNo);
        }

        int totalPages = (int) Math.ceil((double) totalBoards / 10);

        model.addAttribute("boards", boards);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
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
            // 트랜잭션 내에서 게시글과 이미지를 함께 처리
            boardService.createBoardWithImage(board, image);
        } catch (IOException e) {
            e.printStackTrace();
            // 필요시 오류 처리를 추가할 수 있습니다.
        }

        return "redirect:/boards";
    }

    // 게시글 수정 폼 페이지
    @GetMapping("/edit/{boardNo}")
    public String showEditForm(@PathVariable int boardNo, HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        Board board = boardService.getBoardById(boardNo);
        User user = userService.findUserByUserId(userId);


        // 관리자이거나 본인의 게시글만 수정 가능
        if (user.getIsAdmin() != 'Y' && board.getUserNo() != user.getUserNo()) {
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

        if (user.getIsAdmin() != 'Y' && board.getUserNo() != user.getUserNo()) {
            return "redirect:/boards";
        }

        try {
            // 트랜잭션 내에서 게시글과 이미지를 함께 처리
            boardService.updateBoardWithImage(board, image);
        } catch (IOException e) {
            e.printStackTrace();
            // 필요시 오류 처리를 추가할 수 있습니다.
        }

        return "redirect:/boards";
    }

    @GetMapping("/delete/{boardNo}")
    public String deleteBoard(@PathVariable int boardNo, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        Board board = boardService.getBoardById(boardNo);
        User user = userService.findUserByUserId(userId);

        if (user.getIsAdmin() != 'Y' && board.getUserNo() != user.getUserNo()) {
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

        // 작성자인지 확인
        boolean isAuthor = board.getUserNo() == user.getUserNo();

        List<Reply> replies = replyService.getRepliesByBoardNo(boardNo);
        model.addAttribute("board", board);
        model.addAttribute("replies", replies);
        model.addAttribute("isAuthor", isAuthor);
        return "board/view";
    }
}
