package io.dorbae.gb.bookretrieval.service;

import io.dorbae.gb.bookretrieval.domain.UserRole;
import io.dorbae.gb.bookretrieval.domain.entity.Member;
import io.dorbae.gb.bookretrieval.domain.repository.MemberRepository;
import io.dorbae.gb.bookretrieval.dto.MemberDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
 *****************************************************************
 *
 * MemberService
 *
 * @description MemberService
 *
 *
 *****************************************************************
 *
 * @version 1.0.0    2019/11/14 9:15     dorbae	최초 생성
 * @since 2019/11/14 9:15
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
@Service
public class MemberService implements UserDetailsService {
//    private static final Logger LOG = LoggerFactory.getLogger(MemberService.class);

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     * <p>
     * // TODO: 과제 1-1. 회원가입
     *
     * @param memberDto
     * @return
     */
    @Transactional
    public Long signUpMember(MemberDto memberDto) {
        // 비밀번호 암호화
        // TODO: 과제 1-3. 계정 비밀번호 암호화 저장
        memberDto.setPassword(this.passwordEncoder.encode(memberDto.getPassword()));
        return this.memberRepository.save(memberDto.toEntity()).getId();
    }

    /**
     * 계정명으로 Member 가져오기
     *
     * @param account
     * @return
     */
    @Transactional
    public Member getMember(String account) {
        Optional<Member> member = this.memberRepository.findByAccount(account);
        if (member.isPresent()) {
            return member.get();
        } else {
            return null;
        }
    }

    /**
     * 로그인 연동
     * <p>
     * // TODO: 과제 1-2. 로그인(Spring Security 연동)
     *
     * @param account 로그인 시, 시도한 ID
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        Optional<Member> memberEntityWrapper = memberRepository.findByAccount(account);
        Member memberEntity = memberEntityWrapper.orElseThrow(() -> new UsernameNotFoundException(account));

        // 권한 설정
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (("admin").equals(account)) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getCode()));
        } else {
            authorities.add(new SimpleGrantedAuthority(UserRole.MEMBER.getCode()));
        }

        return new User(memberEntity.getAccount(), memberEntity.getPassword(), authorities);
    }
}
