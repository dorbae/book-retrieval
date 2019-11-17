package io.dorbae.gb.bookretrieval.controller;

import io.dorbae.gb.bookretrieval.domain.entity.KeywordStatistic;
import io.dorbae.gb.bookretrieval.domain.entity.Member;
import io.dorbae.gb.bookretrieval.domain.entity.RetrievalHistory;
import io.dorbae.gb.bookretrieval.dto.BookDetailDto;
import io.dorbae.gb.bookretrieval.dto.BookPagingDto;
import io.dorbae.gb.bookretrieval.exception.ApiNotReachableException;
import io.dorbae.gb.bookretrieval.service.BookService;
import io.dorbae.gb.bookretrieval.service.KeywordStatisticService;
import io.dorbae.gb.bookretrieval.service.MemberService;
import io.dorbae.gb.bookretrieval.service.RetrievalHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

/*
 *****************************************************************
 *
 * BookController
 *
 * @description BookController
 *
 *
 *****************************************************************
 *
 * @version 1.3.0    2019/11/17 02:21     dorbae	페이징 처리 오류 수정
 * @version 1.2.0    2019/11/17 01:46     dorbae	페이징 처리 Thymeleaf -> 서버
 * @version 1.1.0    2019/11/16 00:32     dorbae	ISBN 검색 수정
 * @version 1.0.0    2019/11/12 00:32     dorbae	최초 생성
 * @since 2019/11/12 0:32
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
@Controller
@RequestMapping("/books")
public class BookController {

    private static final Logger LOG = LoggerFactory.getLogger(BookController.class);
    @Autowired
    private BookService bookService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private RetrievalHistoryService retrievalHistoryService;
    @Autowired
    private KeywordStatisticService keywordStatisticService;

    /**
     * 책 키워드 검색
     *
     * @param req
     * @param keyword
     * @param target
     * @param sort
     * @param page
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String retrieveBooks(HttpServletRequest req,
                                @RequestParam(name = "keyword") String keyword,
                                @RequestParam(name = "target", defaultValue = "") String target,
                                @RequestParam(name = "sort", defaultValue = "") String sort,
                                @RequestParam(name = "page", defaultValue = "1") String page, Model model) throws ResponseStatusException, Exception {


        String account = req.getUserPrincipal().getName();

        // .0 들어오는 문제 해결
        int pageInt = (int) Float.parseFloat(page);

        LOG.debug("{} is retrieving books...[keyword={}, target={}, sort={}, page={}]", account, keyword, target, sort, pageInt);

        Member member = this.memberService.getMember(account);
        if (member == null) {
            LOG.warn("Can't find {}.", account);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }

        try {
            // 유저별 검색 히스토리 저장
            this.retrievalHistoryService.save(RetrievalHistory.builder().keyword(keyword).member(member).build());
            // 검색 키워드 조회 건수 업데이트
            KeywordStatistic keywordStatistic = KeywordStatistic.builder().keyword(keyword).build();
            this.keywordStatisticService.countUp(keywordStatistic);
        } catch (Exception e) {
            // 도서 검색은 가능하도록 에러 누르기
            LOG.error("Failed to update retrieval history.", e);
        }

        // 도서 검색
        BookPagingDto result = this.bookService.retrievalBooks(keyword, target, sort, pageInt);
        if (result != null) {
            //
            // 페이징 처리 변수
            //
            // 시작 페이지
            int startPage = ((pageInt - 1) / 10) * 10 + 1;
            // 끝 페이지. 10개 단위로 마지막 페이지 설정. 최대 허용 개수 넘어가면, 해당 개수로 설정
            int lastPage = result.getTotalCount() / 10;
            int maximumPage = result.getPageableCount() / 10;
//            LOG.debug("lastPage={}, maximumPage={}, pageable={}", lastPage, maximumPage, result.getPageableCount());
            lastPage = maximumPage < lastPage ? maximumPage : lastPage;
            lastPage = lastPage < 1 ? 1 : lastPage;
            // 화면 상 페이징 마지막 페이지
            int endPage = startPage + 9 < lastPage ? startPage + 9 : lastPage;
            // $END:페이징 처리 변수

            model.addAttribute("pageableCount", result.getPageableCount());     // 호출 페이지에 따라서 바뀜
            model.addAttribute("totalCount", result.getTotalCount());
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            model.addAttribute("lastPage", lastPage);
            model.addAttribute("books", result.getBooks());
            model.addAttribute("keyword", keyword);
            model.addAttribute("target", target);
            model.addAttribute("sort", sort);
            model.addAttribute("page", pageInt);
        } else {
            LOG.warn("There were nothing books which were retrieved.");
        }

        return "fragments/booklist";
    }

    /**
     * ISBN 으로 특정 책 검색하기
     *
     * @param req
     * @param isbn
     * @return
     */
    @RequestMapping(value = "/{isbn}", method = RequestMethod.GET)
//    @ResponseBody
    public String retrieveBooks(HttpServletRequest req, @PathVariable(name = "isbn") String isbn, Model model) throws ApiNotReachableException, ResponseStatusException {

        String account = req.getUserPrincipal().getName();

        LOG.debug("{} is retrieving a specific book... [ISBN={}]", account, isbn);

        Member member = this.memberService.getMember(account);
        if (member == null) {
            LOG.warn("Can't find {}.", account);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }

        // 도서 검색
        BookDetailDto result = this.bookService.retrievalBookByIsbn(isbn);
        if (result != null) {
            LOG.debug("Got book info.[{}]", result.toString());
            model.addAttribute("book", result);
            return "fragments/bookdetail";
        } else {
            return "error/booknotfound";
        }
    }
}
