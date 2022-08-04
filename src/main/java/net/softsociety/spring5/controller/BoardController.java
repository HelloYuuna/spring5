package net.softsociety.spring5.controller;


import lombok.extern.slf4j.Slf4j;
import net.softsociety.spring5.domain.Board;
import net.softsociety.spring5.domain.Reply;
import net.softsociety.spring5.service.BoardService;
import net.softsociety.spring5.util.FileService;
import net.softsociety.spring5.util.PageNavigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * projectName     :spring5
 * fileName        :BoardController
 * author          :yuuna
 * since           :2022/07/27
 */

@Controller
@Slf4j
@RequestMapping("/board")
public class BoardController {

    // 게시판 페이지 당 출력 개수
    @Value("${user.board.page}")
    int countPerPage;                              // 10

    // 게시판 목록의 페이지 이동 링크 수
    @Value("${user.board.group}")
    int pagePerGroup;                             // 5
    
    // 첨부파일 업로드 로컬위치
    @Value("${spring.servlet.multipart.location}")
    String uploadPath;

    @Autowired
    private BoardService service;

    /**
     * 게시판 목록
     * @return list
     */
    @GetMapping("/list")
    public String boardList(Model model
            , @RequestParam(value = "page", defaultValue = "1") int page
            , String type
            , String searchword) {

        // read param value
        log.debug("페이지당 글 수: {}, 페이지당 링크 수:{}, 페이지: {}, 검색대상: {}, 검색어: {}",
                countPerPage, pagePerGroup, page, type, searchword);

        PageNavigator navi = service.getPageNavigator(pagePerGroup, countPerPage, page, type, searchword);

        //DB 글 읽어오기
        List<Board> boardList = service.selectBoardList(navi, type, searchword);
        log.debug("게시글 리스트: {}", boardList);

        model.addAttribute("navi", navi);
        model.addAttribute("boardList", boardList);
        model.addAttribute("type", type);
        model.addAttribute("searchword", searchword);

        return "/boardView/boardList";
    }

    /**
     * 게시글 쓰기페이지로 이동
     * @param model board 객체를 생성해서 넘김
     * @return boardPosting
     */
    @GetMapping("/boardPosting")
    public String boardPositng(Model model) {

        model.addAttribute("board", new Board());

        return "/boardView/boardPosting";
    }

    /**
     * 게시글 작성 POST
     * @param board 작성한 게시글을 담는 객체
     * @return 게시글 리스트
     */
    @PostMapping("/list")
    public String boardList(Board board,
                            @AuthenticationPrincipal UserDetails user,
                            MultipartFile upload) {
        // 현재 세션에 저장된 아이디
        String id = user.getUsername();
        log.debug("세션에 저장된 아이디: " + id);

        // 세션에서 받아온 아이디를 board 객체에 저장
        board.setMemberid(id);

        // 세션에 저장된 파일을 가져옴
        if (upload != null && !upload.isEmpty()) {
            String savedfile = FileService.saveFile(upload, uploadPath);
            board.setOriginalfile(upload.getOriginalFilename());
            board.setSavedfile(savedfile);
        }

        log.debug("작성한 게시글 : {}", board);

        int res = service.insertBoard(board);
        if ( res < 1) {
            log.debug("DB저장 실패");
        }

        log.debug("저장 성공!");

        return "redirect:/board/list";
    }

    /**
     * 게시글과 댓글 읽기
     * @param boardnum 해당 게시글의 번호
     * @param model board객체 reply객체 new Reply객체(댓글작성용)
     * @return 해당 게시글
     */
    @GetMapping("read")
    public String read(@RequestParam(value = "boardnum",defaultValue = "0") int boardnum
                                                                             , Model model) {
        log.debug("가져온 글번호: " + boardnum);

        Board board = service.selectBoard(boardnum);
        log.debug("가져온 게시글 정보: {}", board);

        List<Reply> replies = service.replyList(boardnum);
        
        log.debug("댓글 목록: " + replies);

        model.addAttribute("board", board);
        model.addAttribute("replies", replies);
        model.addAttribute("reply", new Reply());

        return "/boardView/boardRead";
    }

    /**
     * 첨부 파일 다운로드
     * @param boardnum 읽을 게시글 번호
     * @param response getOutputStream
     * @return null
     */
    @GetMapping("/download")
    public String fileDownload(int boardnum, HttpServletResponse response) {

        log.debug("게시글번호: " + boardnum);

        // 파일 읽기
//        Board board = service.read(boardnum);
        Board board = service.selectBoard(boardnum);

        //원래의 파일명
//        String originalfile = new String(board.getOriginalfile());
        String originalfile = board.getOriginalfile();

        log.debug("{}", board);

        try {
            response.setHeader("Content-Disposition", " attachment;filename="+ URLEncoder.encode(originalfile, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //저장된 파일 경로 읽어오기 D:/download/upload/파일명
        String fullPath = uploadPath + "/" + board.getSavedfile();

        //서버의 파일을 읽을 입력 스트림과 클라이언트에게 전달할 출력스트림
        FileInputStream filein;
        ServletOutputStream fileout;

        try {
            filein = new FileInputStream(fullPath);
            fileout = response.getOutputStream();

            //Spring의 파일 관련 유틸 이용하여 출력
            FileCopyUtils.copy(filein, fileout);

            filein.close();
            fileout.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 게시글 삭제
     * 아이디와 글번호 두개를 조건으로 게시글 삭제
     * @param boardnum 글번호
     * @param user 로그인한 유저 아이디 정보
     * @return 게시글 리스트
     */
    @GetMapping("/delete")
    public String deleteBoard(int boardnum
                                , @AuthenticationPrincipal UserDetails user) {
        log.debug("넘어온 글번호: {}", boardnum);
        
        /* 
         * 글과 파일 함께 삭제
         * get방식으로 인해 다른 유저에게 영향을 받을 수 있으므로
         * 글 작성자 id와 세션 id 와 일치하는지 확인 후 삭제 진행
         */
        
        // 1. 해당 글 번호의 게시글 조회 -> 결과가 없으면 리다이렉트
        Board board = service.selectBoard(boardnum);
        if(board == null) {
            log.debug("조회된 결과가 없음");
            return "redirect:/";
        }

        log.debug("board: " + board);

        // 2. 첨부된 파일명 확인
        String savedFile = board.getSavedfile();
        String fullPath = uploadPath + "/" + savedFile;

        log.debug("fullPath: " + fullPath);

        // 3. 로그인한 아이디 확인
        String getId = user.getUsername();
        if (!board.getMemberid().equals(getId)) {
            log.debug("아이디가 일치하지 않음");
            return "redirect:/";
        }

        log.debug("아이디일치 (삭제가능)");

        // 4. 전달된 번호의 글 삭제
        int res = service.deleteBoard(board);
        if (res < 1) {
            log.debug("삭제실패");
            return "redirect:/";
        }
        
        log.debug("글삭제 성공");

        // 5. 글이 삭제되고 첨부파일이 있는 경우에 파일도 삭제\
        if ( savedFile != null ) {
        FileService.deleteFile(fullPath);
            log.debug("파일삭제");

        } else {
            log.debug("파일이 존재하지 않음");
        }
        
        return "redirect:/board/list";
    }

    /**
     * 수정 폼 이동
     * @param boardnum 글번호
     * @param model 수정폼에 뿌릴 번호
     * @return 수정 폼
     */
    @GetMapping("update")
    public String updateBoard(int boardnum, Model model) {
        // 전달받은 번호의 글정보 조회
        log.debug("글번호: " + boardnum);

        Board board = service.selectBoard(boardnum);
        log.debug("가져온 게시글 객체정보: " + board);

        model.addAttribute(board);
        // 모델로 저장해서 html로 뿌림
        return "/boardView/boardUpdateForm";
    }

    /**
     * 게시글 수정
     * 로그인아이디와 게시작성 아이디가 같다면 수정진행
     * @param board 수정하려는 게시글 객체
     * @param upload 업로드 input name
     * @param user 세션에 저장된 로그인 아이디
     * @return read
     */
    @PostMapping("update")
    public String update(Board board,
                         MultipartFile upload
                            , @AuthenticationPrincipal UserDetails user) {
        // 아이디 확인
        String getID = user.getUsername();
        Board regiBoard = service.selectBoard(board.getBoardnum());
        String regiID = regiBoard.getMemberid();
        
        // 로그인한 아이디와 게시자의 아이디 확인
        if (!getID.equals(regiID)) {
            log.debug("아이디 불일치, 수정불가");
            return "redirect:/";
        }

        log.debug("아이디같음 (수정가능)");

        log.debug("넘어온 보드객체: " + board);
        board.setMemberid(getID);                   //로그인 아이디 보드 객체에 넣기

        /*
         * 수정한 제목과 본문, 파일명(있다면) DB에서 수정
         */
        String uploadName = upload.getOriginalFilename();
        String originalName = board.getOriginalfile();
        log.debug("오리지널{}, 새업로드{}",originalName,uploadName);

        // 업로드파일이 비어있지 않고
        if (!upload.isEmpty()) {
            // 기존에 등록된 파일명과 업로드한 파일명이 불일치하다면?
            if ( !originalName.equals(uploadName) ) {
                String fullPath = uploadPath + "/" + originalName;
                FileService.deleteFile(fullPath);
                log.debug("기존 파일삭제 완료");
            }
            
            // 새로운 파일 내용 board 객체에 저장
            String savedfile = FileService.saveFile(upload, uploadPath);
            board.setOriginalfile(upload.getOriginalFilename());
            board.setSavedfile(savedfile);
        }

        log.debug("수정될 게시글 : {}", board);

        int res = service.updateBoard(board);
        if (res < 1) {
            log.debug("수정실패");

        } else {
            log.debug("수정 성공");
        }

        int boardnum = board.getBoardnum();
        return "redirect:/board/read?boardnum=" + boardnum;
    }

    /**
     * 댓글 작성
     * @param reply 댓글 객체
     * @param user 로그인한 유저
     * @return 포스팅 + 게시글번호
     */
    @PostMapping("replyWrite")
    public String replyWrite(Reply reply, @AuthenticationPrincipal UserDetails user) {
        // 세션에서 로그인 한 아이디 받아서 reply 객체에 저장
        reply.setMemberid(user.getUsername());
        log.debug("리플객체: " + reply);
        
        int res = service.insertReply(reply);
        if (res < 1) {
            log.debug("등록 실패");

        } else {
            log.debug("등록 성공");
        }

        return "redirect:/board/read?boardnum=" + reply.getBoardnum();
    }

    @GetMapping("/rDelete")
    public String replyDelete(int boardnum, int replynum) {
        log.debug("게시글번호{}, 댓글번호{}", boardnum, replynum);

        int res = service.deleteReply(replynum);
        if (res < 1) {
            log.debug("삭제 실패");

        } else {
            log.debug("삭제 성공");
        }

        return "redirect:/board/read?boardnum=" + boardnum;
    }
}