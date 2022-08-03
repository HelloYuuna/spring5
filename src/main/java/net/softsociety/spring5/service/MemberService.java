package net.softsociety.spring5.service;

import net.softsociety.spring5.domain.Member;

/**
 * projectName     :spring5
 * fileName        :MemberService
 * author          :yuuna
 * since           :2022/07/21
 */

public interface MemberService {

    int insertUser(Member member);

    boolean idChk(String serchId);

    // 아이디로 멤버 정보 가져오기
    Member getMemberinfo(String id);

    // 개인정보 수정
    int updateMember(Member member);

}