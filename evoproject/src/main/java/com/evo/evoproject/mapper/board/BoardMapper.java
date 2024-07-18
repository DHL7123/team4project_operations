package com.evo.evoproject.mapper.board;

import com.evo.evoproject.domain.board.Board;
import com.evo.evoproject.domain.board.BoardImage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface BoardMapper {

    void insertBoard(Board board);

    Board findBoardById(@Param("boardNo") int boardNo);

    List<Board> findBoardsByUserNo(@Param("userNo") int userNo, @Param("offset") int offset, @Param("limit") int limit);

    List<Board> findAllBoards(@Param("offset") int offset, @Param("limit") int limit);

    void updateBoard(Board board);

    void deleteBoard(@Param("boardNo") int boardNo);

    int countAllBoards();

    int countBoardsByUserNo(@Param("userNo") int userNo);

    void insertBoardImage(BoardImage boardImage);

    List<Integer> findImageIdsByBoardNo(int boardNo);


}
