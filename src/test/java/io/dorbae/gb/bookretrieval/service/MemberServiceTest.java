package io.dorbae.gb.bookretrieval.service;

import io.dorbae.gb.bookretrieval.domain.repository.MemberRepository;
import io.dorbae.gb.bookretrieval.dto.MemberDto;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/*
 *****************************************************************
 *
 * MemberServiceTest
 *
 * @description MemberServiceTest
 *
 *
 *****************************************************************
 *
 * @version 1.0.0    2019/11/17 11:31     dorbae	최초 생성
 * @since 2019/11/17 11:31
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
        this.memberRepository.deleteAll();
    }

    @Test
    public void test1SignUpMember() {
        MemberDto memberDto = new MemberDto();
        memberDto.setAccount("test");
        memberDto.setPassword("1234");
        long id = this.memberService.signUpMember(memberDto);
        System.out.println(id);
    }
}