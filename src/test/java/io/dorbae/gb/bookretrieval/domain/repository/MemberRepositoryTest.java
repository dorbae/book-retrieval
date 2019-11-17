package io.dorbae.gb.bookretrieval.domain.repository;

import io.dorbae.gb.bookretrieval.domain.entity.Member;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/*
 *****************************************************************
 *
 * MemberRepositoryTest
 *
 * @description MemberRepositoryTest
 *
 *
 *****************************************************************
 *
 * @version 1.0.0    2019/11/11 18:35     dorbae	최초 생성
 * @since 2019/11/11 18:35
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {

    private static final String TEST_NAME = "장성배";

    @Autowired
    MemberRepository memberRepository;

    private String tempAccount;

    /**
     * 테스트 유저 ID 생성 기존 ID 존재할 경우, 테스트 종지
     */
    @Before
    public void createTempId() {
        this.tempAccount = RandomStringUtils.random(15, "dorbaeio");
        assertFalse(this.memberRepository.findByAccount(this.tempAccount).isPresent());
    }

    /**
     * 테스트 유저 삭제
     */
    @After
    public void cleanup() {
        this.memberRepository.deleteAll();
    }


    /**
     * 테스트 유저 생성 -> 생성 확인
     */
    @Test
    public void userRegister() {
        //
        // Given Step
        //
        this.memberRepository.save(Member.builder()
                .account(this.tempAccount)
                .password("0101010101016")
                .name(TEST_NAME)
                .build());

        //
        // When Step
        //
        java.util.Optional<Member> foundUser = this.memberRepository.findByAccount(this.tempAccount);

        //
        // Then Step
        //
        assertTrue(foundUser.isPresent());
        assertEquals(foundUser.get().getName(), TEST_NAME);
    }

    @Test
    public void userRegisterWithTime() {
        //
        // Given Step
        //
        LocalDateTime now = LocalDateTime.now();

        this.memberRepository.save(Member.builder()
                .account(this.tempAccount)
                .name(TEST_NAME)
                .password("0101010101016")
                .build());

        //
        // When Step
        //
        java.util.Optional<Member> foundUser = this.memberRepository.findByAccount(this.tempAccount);

        //
        // Then Step
        //
        assertTrue(foundUser.isPresent());
        Member matchMember = foundUser.get();
        assertEquals(matchMember.getName(), TEST_NAME);
//        assertTrue(matchMember.getCreatedDate().isAfter(now));
//        assertTrue(matchMember.getModifiedDate().isAfter(now));
    }
}
