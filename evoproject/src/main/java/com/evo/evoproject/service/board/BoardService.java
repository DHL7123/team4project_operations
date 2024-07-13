package com.evo.evoproject.service.board;

import com.evo.evoproject.domain.board.Board;
import com.evo.evoproject.Mapper.board.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    private final BoardMapper boardMapper;

    @Autowired
    public BoardService(BoardMapper boardMapper) {
        this.boardMapper = boardMapper;
    }

    public void createBoard(Board board) {
        boardMapper.insertBoard(board);
    }

    public Board getBoardById(int boardNo) {
        return boardMapper.findBoardById(boardNo);
    }

    public List<Board> getBoardsByUserNo(int userNo, int offset, int limit) {
        return boardMapper.findBoardsByUserNo(userNo, offset, limit);
    }
    public List<Board> getAllBoards(int offset, int limit) {
        return boardMapper.findAllBoards(offset, limit);
    }

    public void updateBoard(Board board) {boardMapper.updateBoard(board);}

    public void deleteBoard(int boardNo) {
        boardMapper.deleteBoard(boardNo);
    }

    public int getTotalBoardCount() {
        return boardMapper.countAllBoards();
    }

    public int getUserBoardCount(int userNo) {
        return boardMapper.countBoardsByUserNo(userNo);
    }


}
