package io.dorbae.gb.bookretrieval.controller;

import io.dorbae.gb.bookretrieval.dto.MemberDto;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/*
 *****************************************************************
 *
 * MemberControllerTest
 *
 * @description MemberControllerTest
 *
 *
 *****************************************************************
 *
 * @version 1.0.0    2019/11/17 11:56     dorbae	최초 생성
 * @since 2019/11/17 11:56
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MemberControllerTest {
    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;
    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    private MockHttpServletRequest req;
    private MockHttpServletResponse res;

    @Autowired
    private MemberController memberController;

    @Before
    public void setUp() {
        this.req = new MockHttpServletRequest();
        this.res = new MockHttpServletResponse();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void test1SignUpPage() throws Exception {
        this.req.setMethod("GET");
        this.req.setRequestURI("/auth/signup");

        Object handler = this.handlerMapping.getHandler(this.req).getHandler();
        this.handlerAdapter.handle(this.req, this.res, handler);
        assertEquals(this.res.getStatus(), 200);
    }

    @Test
    public void test2SignUp() throws Exception {
        MemberDto memberDto = new MemberDto();
        memberDto.setAccount("test");
        memberDto.setPassword("1234");
        memberDto.setName("테스터");

        String path = this.memberController.signUp(memberDto, null);

        assertEquals("redirect:/auth/login", path);
    }

    @Test
    public void test3SignInPage() throws Exception {
        req.setMethod("GET");
        req.setRequestURI("/auth/login");

        Object handler = this.handlerMapping.getHandler(this.req).getHandler();
        this.handlerAdapter.handle(this.req, this.res, handler);
        assertEquals(res.getStatus(), 200);
    }

    @Test
    public void test4LogoutPage() throws Exception {
        req.setMethod("GET");
        req.setRequestURI("/auth/logout/result");

        Object handler = this.handlerMapping.getHandler(this.req).getHandler();
        this.handlerAdapter.handle(this.req, this.res, handler);
        assertEquals(res.getStatus(), 200);
    }
}