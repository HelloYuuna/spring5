package net.softsociety.spring5.controller;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.spring5.domain.Member;
import net.softsociety.spring5.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * projectName     :spring5
 * fileName        :MemberController
 * author          :yuuna
 * since           :2022/07/21
 */

@Slf4j
@RequestMapping("/member")
@Controller
public class MemberController {

    @Autowired
    private MemberService service;

    /* 회원가입 폼 */
    @GetMapping("/join")
    public String intoJoin(Model model) {
        model.addAttribute("member", new Member());

        return "join";
    }

    /* 회원가입 폼 > 상위로 이동 */
    @PostMapping("/join")
    public String result(Member member) {
        log.debug("입력받은 값: " + member);

        int res = service.insertUser(member);
        if ( res != 1 ) {
            log.debug("데이터 입력 실패");
        }

        return "redirect:/";
    }

    /* id체크 창 */
    @GetMapping("/idChk")
    public String idChk() {

        return "/idChk";
    }

    /* id체크 창 > 아이디 중복체크 결과 */
    @PostMapping("/idChk")
    public String idChk(String serchId, Model model) {
        log.debug("검색 ID : {}", serchId);

        // ID search
        boolean result = service.idChk(serchId);
        if(!result) {
            log.debug("찾기 실패");
        }

        log.debug("idChk: {}", result);
        model.addAttribute("searchId", serchId);
        model.addAttribute("result", result);

        return "/idChk";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    /**
     * 개인정보 수정페이지 이동
     * @param user 세션에 저장된 유저 아이디
     * @param model html로 세션에 저장한 아이디 객체전달
     * @return 개인정보 수정페이지
     */
    @GetMapping("/mypage")
    public String gotoMypage(Model model, @AuthenticationPrincipal UserDetails user) {
        // 세션에서 로그인한 유저 아이디를 가져옴
        String id = user.getUsername();
        log.debug("세션에 저장된 아이디: {}", id);

        Member member = service.getMemberinfo(id);
        log.debug("id로 찾아낸 객체값: {}", member);

        /* 검색한 결과를 모델에 저장 > html 이동
         * 회원이 입력하지 않았을 때에도 Null값을 저장하지 않도록
         * 수정 폼에 애초부터 기존의 저장한 객체의 값을 넘김
         */
        model.addAttribute("member", member);

        return "/mypageForm";
    }

    /**
     * 개인정보 수정 DB 저장
     * @param member 회원객체
     * @return 메인화면
     */
    @PostMapping("/mypage")
    public String profileUpdate(Member member, @AuthenticationPrincipal UserDetails user) {
        // 수정폼에 입력한 값을 전달받음
        log.debug("넘어온 수정된 개인정보: {}", member);

        // 로그인한 아이디 읽어서 전달받은 Member 객체에 추가
        String id = user.getUsername();
        log.debug("세션에서 받아온 id: {}", id);
        member.setMemberid(id);

        /*
         * 객체를 DB에 저장
         * 멤버아이디가 같은 데이터에 저장
         * 비밀번호가 비어있다면 수정하지않음
         * 수정항목: 비밀번호 이름 이메일 전화번호 주소
         */
        int res = service.updateMember(member);
        if (res > 1) {
            log.debug("수정실패");
            return "redirect:/";
        }

        log.debug("수정후: " + member);

        return "redirect:/";
    }
}