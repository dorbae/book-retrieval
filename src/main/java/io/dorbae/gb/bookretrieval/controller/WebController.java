package io.dorbae.gb.bookretrieval.controller;

import io.dorbae.gb.bookretrieval.domain.entity.KeywordStatistic;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

/*
 *****************************************************************
 *
 * WebController
 *
 * @description WebController
 *
 *
 *****************************************************************
 *
 * @version 1.0.0    2019/11/13 15:46     dorbae	최초 생성
 * @since 2019/11/13 15:46
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
@Controller
public class WebController {
    /**
     * 루트 디렉터리
     *
     * @return
     */
    @GetMapping("/")
    public String root() {
        return "index";
    }

    /**
     * 접근 권한 에러 처리
     *
     * @return
     */
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "error/access-denied";
    }

}
