package net.softsociety.spring5.dao;

import net.softsociety.spring5.domain.Board;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * projectName     :spring5
 * fileName        :BoardDAO
 * author          :yuuna
 * since           :2022/07/27
 */

@Mapper
public interface BoardDAO {
    // 작성한 게시글 등록
    int insertBoard(Board board);

    // 게시글 갯수
    int count(Map<String, Object> map);

    // 게시판 전체 목록
    List<Board> selectBoardList(Map<String, Object> map, RowBounds rowBounds);

    // 게시판 읽기
    Board selectBoard(int boardnum);

    // 히트 카운트
    int updateBoardCNT(int boardnum);
    
    // 게시글 지우기
    int deleteBoard(Board board);
    
    // 게시글 수정
    int updateBoard(Board board);
}