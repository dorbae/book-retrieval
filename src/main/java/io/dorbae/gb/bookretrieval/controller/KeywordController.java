package io.dorbae.gb.bookretrieval.controller;

import io.dorbae.gb.bookretrieval.domain.entity.KeywordStatistic;
import io.dorbae.gb.bookretrieval.domain.entity.Member;
import io.dorbae.gb.bookretrieval.domain.entity.RetrievalHistory;
import io.dorbae.gb.bookretrieval.service.KeywordStatisticService;
import io.dorbae.gb.bookretrieval.service.MemberService;
import io.dorbae.gb.bookretrieval.service.RetrievalHistoryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/*
 *****************************************************************
 *
 * KeywordController
 *
 * @description KeywordController
 *
 *
 *****************************************************************
 *
 * @version 1.0.0    2019/11/15 2:32     dorbae	최초 생성
 * @since 2019/11/15 2:32
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
@Controller
@RequestMapping("/keywords")
public class KeywordController {
    private static final Logger LOG = LoggerFactory.getLogger(KeywordController.class);

    @Autowired
    private MemberService memberService;
    @Autowired
    private RetrievalHistoryService retrievalHistoryService;
    @Autowired
    private KeywordStatisticService keywordStatisticService;

    /**
     * 인기 검색어 10개
     *
     * @param model
     * @return
     */
    @GetMapping(value = "/top")
    public String getTop10KeywordsPage(Model model) {
        List<KeywordStatistic> topRankedKeywords = this.keywordStatisticService.getTopRankedKeywords(10);
//        LOG.debug("topKeyword.size={}", topRankedKeywords.size());
        model.addAttribute("keywords", topRankedKeywords);
        return "fragments/topkeyword";
    }

    /**
     * 최근 검색어 10개
     *
     * @return
     */
    @GetMapping(value = "/recent")
    public String getRetrievalHistory(HttpServletRequest req, Model model) throws ResponseStatusException {
        String account = req.getUserPrincipal().getName();
        if (StringUtils.isEmpty(account)) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }

        Member member = this.memberService.getMember(account);
        List<RetrievalHistory> keywordHistory = this.retrievalHistoryService.findByMember(member);
        model.addAttribute("keywords", keywordHistory);
        return "fragments/recentretrieval";
    }
}
