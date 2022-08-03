package net.softsociety.spring5.service;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.spring5.dao.BoardDAO;
import net.softsociety.spring5.domain.Board;
import net.softsociety.spring5.util.PageNavigator;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * projectName     :spring5
 * fileName        :BoardServiceImpl
 * author          :yuuna
 * since           :2022/07/27
 */

@Service
@Slf4j
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardDAO dao;

    /**
     * 게시글 등록
     * @param board id title textarea
     * @return 등록 결과 0 or 1
     */
    @Override
    public int insertBoard(Board board) {
        return dao.insertBoard(board);
    }

    @Override
    public List<Board> selectBoardList(PageNavigator navi, String type, String searchword) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("searchword", searchword);

        RowBounds rowBounds = new RowBounds(navi.getStartRecord(), navi.getCountPerPage());
//        List<Board> boardList = dao.selectBoardList(map, rowBounds);

        return dao.selectBoardList(map, rowBounds);
    }

    @Override
    public Board selectBoard(int boardnum) {

        // 히트카운트 업
        int res = dao.updateBoardCNT(boardnum);

        if ( res < 1 ) {
            log.debug("처리 실패~");
        }

        return dao.selectBoard(boardnum);
    }

    @Override
    public PageNavigator getPageNavigator(int pagePerGroup, int countPerPage, int page, String type, String searchword) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("searchword", searchword);

        int total = dao.count(map);
        return new PageNavigator(pagePerGroup, countPerPage, page, total);
    }

    @Override
    public int deleteBoard(Board board) {
        return dao.deleteBoard(board);
    }

    @Override
    public int updateBoard(Board board) {
        return dao.updateBoard(board);
    }
}