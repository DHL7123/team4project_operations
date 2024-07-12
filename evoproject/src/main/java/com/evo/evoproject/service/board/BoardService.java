package com.evo.evoproject.service.board;

import com.evo.evoproject.domain.board.Board;
import com.evo.evoproject.repository.board.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public void createBoard(Board board) {
        boardRepository.insertBoard(board);
    }

    public Board getBoardById(int boardNo) {
        return boardRepository.findBoardById(boardNo);
    }

    public List<Board> getBoardsByUserNo(int userNo, int offset, int limit) {
        return boardRepository.findBoardsByUserNo(userNo, offset, limit);
    }

    public List<Board> getAllBoards(int offset, int limit) {
        return boardRepository.findAllBoards(offset, limit);
    }

    public void updateBoard(Board board) {
        boardRepository.updateBoard(board);
    }

    public void deleteBoard(int boardNo) {
        boardRepository.deleteBoard(boardNo);
    }

    public int getTotalBoardCount() {
        return boardRepository.countAllBoards();
    }

    public int getUserBoardCount(int userNo) {
        return boardRepository.countBoardsByUserNo(userNo);
    }


}
