package com.nc13.springBoard.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;


@Data
public class UserDTO implements UserDetails {
    private int id;
    private String username;
    private String password;
    private String nickname;
    private String role;    //관리자 유저 직책설정 등등
    private List<GrantedAuthority> authorities;//  리더 라이터 온리 권한설정

    @Override
    public List<GrantedAuthority> getAuthorities() {
        authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));
        return authorities;
    }


}
