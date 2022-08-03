package net.softsociety.spring5.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * member 테이블을 참조하는 게시판 board dto
 * projectName     :spring5
 * fileName        :Board
 * author          :yuuna
 * since           :2022/07/27
 */

@Data
public class Board implements UserDetails {

    private int boardnum;               // 게시판 번호
    private String memberid;            // 글쓴이
    private String title;               // 게시판 제목
    private String textarea;            // 게시판 내용
    private String inputdate;           // 게시 날짜
    private int hits;                   // 조회수

    private String originalfile;        // 원 파일명
    private String savedfile;           // 저장된 파일명

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return memberid;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}