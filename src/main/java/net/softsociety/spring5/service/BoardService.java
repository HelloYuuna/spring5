package net.softsociety.spring5.service;

import net.softsociety.spring5.domain.Board;
import net.softsociety.spring5.domain.Reply;
import net.softsociety.spring5.util.PageNavigator;

import java.util.List;

/**
 * projectName     :spring5
 * fileName        :BoardService
 * author          :yuuna
 * since           :2022/07/27
 */
public interface BoardService {
    // 작성한 게시글 등록
    int insertBoard(Board board);

    List<Board> selectBoardList(PageNavigator navi, String type, String searchword);

    Board selectBoard(int boardnum);

    PageNavigator getPageNavigator(int pagePerGroup, int countPerPage, int page, String type, String searchword);

    // 게시글 삭제
    int deleteBoard(Board board);
    
    // 게시글 수정
    int updateBoard(Board board);
    
    // 댓글 등록
    int insertReply(Reply reply);
    
    // 댓글 읽기
    List<Reply> replyList(int boardnum);
    
    // 댓글 삭제
    int deleteReply(int replynum);
}