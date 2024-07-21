package com.evo.evoproject.service.board;

import com.evo.evoproject.domain.board.Board;
import com.evo.evoproject.mapper.board.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@Service
public class BoardService {

    private final BoardMapper boardMapper;
    private final NaverImageUploadService imageUploadService;

    @Autowired
    public BoardService(BoardMapper boardMapper, NaverImageUploadService imageUploadService) {
        this.boardMapper = boardMapper;
        this.imageUploadService = imageUploadService;
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


    @Transactional
    public void createBoardWithImage(Board board, MultipartFile image) throws IOException {
        // 게시글을 먼저 생성하고 생성된 boardNo를 반환받음
        board.setBoardTimestamp(new Timestamp(System.currentTimeMillis()));
        boardMapper.insertBoard(board);
        if (board.getBoardNo() == null || board.getBoardNo() == 0) {
            throw new IllegalStateException("Failed to generate boardNo");
        }

        // 이미지가 있으면 업로드 후 게시글 업데이트
        if (!image.isEmpty()) {
            String imageUrl = imageUploadService.uploadImage(image);
            board.setImageUrl(imageUrl);
            boardMapper.updateBoard(board);

            // 이미지 정보를 데이터베이스에 저장
            imageUploadService.saveBoardImage(board.getBoardNo(), imageUrl);
        }
    }

    @Transactional
    public void updateBoardWithImage(Board board, MultipartFile image) throws IOException {
        // boardNo가 제대로 설정되었는지 확인
        if (board.getBoardNo() == null || board.getBoardNo() == 0) {
            throw new IllegalStateException("Board number is missing");
        }

        // 이미지가 있으면 업로드 후 게시글 업데이트
        if (!image.isEmpty()) {
            String imageUrl = imageUploadService.uploadImage(image);
            board.setImageUrl(imageUrl);

            // 이미지 정보를 데이터베이스에 저장
            imageUploadService.saveBoardImage(board.getBoardNo(), imageUrl);
        }

        boardMapper.updateBoard(board);
    }
    public List<Board> getAllBoardsWithUser(int offset, int limit) {
        return boardMapper.findAllBoardsWithUser(offset, limit);
    }
    public List<Board> getBoardsByCategory(int userNo, int category, int offset, int limit) {
        return boardMapper.findBoardsByCategory(userNo, category, offset, limit);
    }

    public int getUserBoardCountByCategory(int userNo, int category) {
        return boardMapper.countBoardsByCategory(userNo, category);
    }

    public List<Board> getAllBoardsByCategory(int category, int offset, int limit) {
        return boardMapper.findAllBoardsByCategory(category, offset, limit);
    }

    public int getTotalBoardCountByCategory(int category) {
        return boardMapper.countAllBoardsByCategory(category);
    }


}
