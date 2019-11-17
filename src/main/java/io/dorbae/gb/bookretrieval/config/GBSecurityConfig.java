package io.dorbae.gb.bookretrieval.config;

import io.dorbae.gb.bookretrieval.domain.UserRole;
import io.dorbae.gb.bookretrieval.handler.GBLoginSuccessHandler;
import io.dorbae.gb.bookretrieval.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 *****************************************************************
 *
 * GBSecurityConfig
 *
 * @description Spring 전반적인 보안 설정
 *
 *
 *****************************************************************
 *
 * @version 1.0.1    2019/11/15 5:03     dorbae	로그아웃 시, 메인 화면으로 이동하도록 수정
 * @version 1.0.0    2019/11/14 4:06     dorbae	최초 생성
 * @since 2019/11/14 4:06
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
@Configuration
@EnableWebSecurity
public class GBSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(GBSecurityConfig.class);
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;
    @Autowired
    private MemberService memberService;

    /**
     * 비밀번호 인코더 설정
     * BCrypt 알고리즘은 60자 문자열 생성
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 보안 설정
     * FilterChainProxy 생성 필터
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        // static 디렉터리의 하위 리소스 디렉터리는 인증 Skip
        web.ignoring().antMatchers("/css/**", "/js/**", "img/**");
    }

    /**
     * 보안 설정
     * HTTP 요청에 대한 웹 기반 보안
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                // For H2 Console
                .headers().frameOptions().disable().and()
                // HttpServletRequest에 따라 접근(access)을 제한
                .authorizeRequests()
                //
                // 페이지 권한 설정
                //
                .antMatchers("/", "/home", "/index", "/members", "/auth/**", "/user/**", "/login", "/logout", "/h2-console/**", "/keywords/top", "/test/**").permitAll()
                .antMatchers("/admin/**").hasAnyRole(UserRole.ADMIN.name())
                .antMatchers("/user/**").hasAnyRole(UserRole.MEMBER.name())
                // $END:페이지 권한 설정
                // 모든 요청에 대해, 인증된 사용자만 접근하도록 설정
                .anyRequest().authenticated();

        //
        // 로그인 설정
        //
        // Form 기반으로 인증. 로그인 정보는 기본적으로 HttpSession 이용
        http.formLogin()
                // Custom Form 설정 가능. /login은 SpringSecurity 기본 폼
                .loginPage("/auth/login")
                .defaultSuccessUrl("/")
                // 별도 로그인 폼에서 유저명 Input ID. Default: username
//                .usernameParameter("username")
                .permitAll();
        // $END:로그인 설정

        //
        // 로그아웃 설정
        //
        //
        http.logout()
                // 로그아웃 URI 설정
                .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"))
                // 세션 초기화
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutSuccessUrl("/")
                // 로그아웃 시, 특정 쿠키 삭제
//                .deleteCookies("key")
                .permitAll();
        // $END: 로그아웃 설정

        // 403 에러 핸들러
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }

    /**
     * Spring Security에서 모든 인증 담당하는 AuthenticationManager 생성을 위한 Builder 설정
     *
     * @param auth
     * @throws Exception
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        try {
            auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Default ADMIN 계정, 일반 유저 생성
     *
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password("password").roles(UserRole.MEMBER.name())
                .and()
                .withUser("admin").password("manager").roles(UserRole.ADMIN.name());
    }

    /**
     * 인증 성공 핸들러 설정
     *
     * @return
     */
    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new GBLoginSuccessHandler("/");
    }
}
