package io.dorbae.gb.bookretrieval.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/*
 *****************************************************************
 *
 * WebControllerTest
 *
 * @description WebControllerTest
 *
 *
 *****************************************************************
 *
 * @version 1.0.0    2019/11/13 15:51     dorbae	최초 생성
 * @since 2019/11/13 15:51
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class WebControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void loadMainPage() {
        //when
        String body = this.restTemplate.getForObject("/", String.class);

        //then
        assertNotNull(body);
    }
}