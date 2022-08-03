package net.softsociety.spring5.service;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.spring5.dao.MemberDAO;
import net.softsociety.spring5.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * projectName     :spring5
 * fileName        :MemberServiceImpl
 * author          :yuuna
 * since           :2022/07/21
 */

@Service
public class MemberServiceImpl implements MemberService {

    /*
     * DAO 와 비밀번호 암호화 주입
     */
    @Autowired
    private MemberDAO dao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 딘빙행 비밀번호 암호화 필수
     * WebSecurityConfig 의 passwordEncoder 구현
     * @param member 멤버 정보가 담긴 객체
     * @return 1 or 0
     */

    @Override
    public int insertUser(Member member) {
        /*
         * 비밀번호 암호화
         * Member 객체의 encodedPW 를 가져와서
         * 기존의 memberpw 에 암호화하여 넣음
         */
        String encodedPW = passwordEncoder.encode(member.getMemberpw());
        member.setMemberpw(encodedPW);

        return dao.insertUser(member);
    }

    /**
     * 아이디 존재 여부 체크
     * 해당 아이디를 가지는 member 객체가 null값이라면
     * return true
     * @param serchId idChk 페이지에서 받은 아이디
     * @return 불리언
     */
    @Override
    public boolean idChk(String serchId) {
        Member member = dao.selectOne(serchId);
//        return member == null? true : false;
        return member == null;
    }

    @Override
    public Member getMemberinfo(String id) {
        return dao.selectOne(id);
    }

    /**
     * 개인정보 수정
     * 새로 입력받은 비밀번호는 암호화 필수
     * @param member 아이디값을 가지고 있는 객체
     * @return 처리된 결과값 int
     */
    @Override
    public int updateMember(Member member) {
        String updatePW = member.getMemberpw();
        System.out.println("수정하려는 비번: " + updatePW);

        // 수정하려는 비번이 값이 있을때 암호화 진행
        if (updatePW != null && updatePW.length() != 0) {
            // 비밀번호 암호화
            String encodedPW = passwordEncoder.encode(updatePW);
            member.setMemberpw(encodedPW);
        }

        return dao.updateMember(member);
    }
}