package net.softsociety.spring5.domain;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * projectName     :spring5
 * fileName        :Member
 * author          :yuuna
 * since           :2022/07/21
 */

@Data
public class Member implements UserDetails {
    private static final long serialVersionUID = 3846784129371940719L;
    private String memberid;
    private String memberpw;
    private String membername;
    private String email;
    private String phone;
    private String address;
    private boolean enabled;                // 1 or 0
    private String rolename;            // ROLE_USER or ROLE_ADMIN

    /*
     * UserDetails Override Methods
     */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    // 비밀번호 설정
    @Override
    public String getPassword() {
        return this.memberpw;
    }


    // 유저 식별 가능한 정보
    @Override
    public String getUsername() {
        return this.memberid;
    }

    /*
     * ------------- 계정 상태 ---------------
     * return false > 로직필요
     */

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

    /**
     * enabled
     * 0 : false 비활성화
     * 1 : true 활성화
     * @return true/false
     */
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}