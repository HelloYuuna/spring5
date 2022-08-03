package net.softsociety.spring5.dao;

import net.softsociety.spring5.domain.Member;
import org.apache.ibatis.annotations.Mapper;

/**
 * projectName     :spring5
 * fileName        :MemberDAO
 * author          :yuuna
 * since           :2022/07/21
 */
@Mapper
public interface MemberDAO {

    int insertUser(Member member);

    //    boolean idChk(String serchId);
    Member selectOne(String serchId);

    int updateMember(Member member);
}