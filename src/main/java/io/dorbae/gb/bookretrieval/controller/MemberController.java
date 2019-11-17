package io.dorbae.gb.bookretrieval.controller;

import io.dorbae.gb.bookretrieval.GBConstant;
import io.dorbae.gb.bookretrieval.dto.MemberDto;
import io.dorbae.gb.bookretrieval.service.MemberService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

/*
 *****************************************************************
 *
 * MemberController
 *
 * @description 로그인/가입 관련 Controller
 *
 *
 *****************************************************************
 *
 * @version 1.2.0    2019/11/17 03:19     dorbae	계정 중복 오류 클라이언트 전달
 * @version 1.1.0    2019/11/15 15:23     dorbae	계정 중복 오류 수정
 * @version 1.0.0    2019/11/14 05:25     dorbae	최초 생성
 * @since 2019/11/14 5:25
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
@Controller
public class MemberController {
    private static final Logger LOG = LoggerFactory.getLogger(MemberController.class);
    @Autowired
    private MemberService memberService;

    /**
     * 회원가입 페이지
     *
     * @return
     */
    @GetMapping("/auth/signup")
    public String signUpPage() {
        return "auth/signup";
    }

    /**
     * 회원가입 처리
     *
     * @param memberDto
     * @return
     */
    @PostMapping("/members")
    public String signUp(MemberDto memberDto, Model model) {
        if (memberDto == null || StringUtils.isAnyEmpty(memberDto.getAccount(), memberDto.getPassword(), memberDto.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        String account = memberDto.getAccount();
        // 계정 이미 존재할 경우
        if (this.memberService.getMember(account) != null) {
            LOG.info("{} member is already exists.", account);
            model.addAttribute("customerr", "12");
            return "auth/signup";
        }

        this.memberService.signUpMember(memberDto);
        return "redirect:auth/login";
    }

    /**
     * 로그인 페이지
     *
     * @param req
     * @return
     */
    @GetMapping("/auth/login")
    public String signInPage(HttpServletRequest req) {
        // 이전 경로 저장
        String referrer = req.getHeader(HttpHeaders.REFERER);
        if (referrer != null) {
            req.getSession().setAttribute(GBConstant.SESSION_ATTR_NAME, referrer);
        }
        return "auth/login";
    }

    /**
     * 로그아웃 결과 페이지
     */
    @GetMapping("/auth/logout/result")
    public String logoutPage() {
        return "auth/logout";
    }
}
