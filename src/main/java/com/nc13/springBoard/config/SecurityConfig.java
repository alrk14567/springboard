package com.nc13.springBoard.config;

import com.nc13.springBoard.service.UserAuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, UserAuthService userAuthService) throws Exception {
        // 주로 필터체인이라고 붙는 애들은 전부다 요청을 보냈을때 얘를 거치고 나서 그다음에 응답이 가는 형태가 될거임
        httpSecurity
                // Cross Site Request Forgery 방지
                .csrf(AbstractHttpConfigurer::disable)
                // 쿠키란? 웹브라우저에서 정보를 저장하는 메모리 그러한 정보를 탈취해서 나 본인이야 내 정보 다 내놔
                // 하면 다 내놓게 되는
                // how? 되게 그럴듯한 사이트를 만든다 -> 오타난줄 모르고 로그인을 하고 그 사이트에서 대리로 네이버에 로그인 한다.
                // 정보를 탈취한다.

                //URL 별 권한 설정
                .authorizeHttpRequests((authorize) -> authorize
                        // WEB_INF 폴더 안의 Views 안의 모든 JSP파일은 누구든 접근 가능
                        .requestMatchers("/WEB-INF/**").permitAll()      // 별이 하나면 그 폴더 안의 직속 파일만 해당 별이 두개면 그안에있는 모든 하위폴더도 포함
                        //images 폴더와 해당 폴더 안의 모든 파일은 누구든 접근 가능
                        .requestMatchers("/images/**").permitAll()
                        // localhost:8080/user/.... 이거나 localhost:8080/ 은 누구든 접근 가능
                        .requestMatchers("/", "/user/*").permitAll()
                        // /board./.../...,/reply/../... 은 로그안 사용자만 접근 가능
                        // /board/write는 ADMIN역할을 가진 사용자만 접근 가능
                        .requestMatchers("/board/write").hasAnyAuthority("ADMIN")// Authority 로 할때는 롤 어드민 어드민 둘다 가능 하지만 롤로 했을 경우엔 Role_admin은 안댐
                        // 위의 경우가 아닌 모든 URL은 로그인한 사용자만 접근가능ㅁㅁ
                        .anyRequest().authenticated())
                // 커스텀 폼 로그인 설정
                .formLogin((formLogIn) -> formLogIn
                        // 로그인에서 사용할 페이지 설정
                        .loginPage("/")
                        //로그인 페이지에서 username을 어떤 name 어트리뷰트로 넘겨줄지 설정
                        .usernameParameter("username")
                        // 로그인페이지에서 password를 어떤 name 어트리뷰트로 넘겨줄지 설정
                        .passwordParameter("password")
                        // 로그인 성공 시 이동할 페이지
                        .defaultSuccessUrl("/board/showAll/1")
                        // 로그인 처리 URL
                        .loginProcessingUrl("/user/auth")
                // 내가만든 userAuthService 등록
                ).userDetailsService(userAuthService);


        return httpSecurity.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
