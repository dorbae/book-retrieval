package io.dorbae.gb.bookretrieval.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
 *****************************************************************
 *
 * KeywordControllerTest
 *
 * @description KeywordControllerTest
 *
 *
 *****************************************************************
 *
 * @version 1.0.0    2019/11/17 13:33     dorbae	최초 생성
 * @since 2019/11/17 13:33
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KeywordControllerTest {

    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;
    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    private MockHttpServletRequest req;
    private MockHttpServletResponse res;

    @Before
    public void setup() {
        this.req = new MockHttpServletRequest();
        this.res = new MockHttpServletResponse();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getTop10KeywordsPage() throws Exception {
        this.req.setMethod("GET");
        this.req.setRequestURI("/keywords/top");

        Object handler = this.handlerMapping.getHandler(this.req).getHandler();
        this.handlerAdapter.handle(this.req, this.res, handler);
        assertEquals(this.res.getStatus(), 200);
    }
}