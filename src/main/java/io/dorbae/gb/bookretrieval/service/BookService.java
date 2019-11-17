package io.dorbae.gb.bookretrieval.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dorbae.gb.bookretrieval.domain.BookTarget;
import io.dorbae.gb.bookretrieval.dto.BookDetailDto;
import io.dorbae.gb.bookretrieval.dto.BookPagingDto;
import io.dorbae.gb.bookretrieval.dto.BookSimpleDto;
import io.dorbae.gb.bookretrieval.exception.ApiNotReachableException;
import io.dorbae.gb.bookretrieval.util.GBStringUtil;
import io.dorbae.gb.bookretrieval.util.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 *****************************************************************
 *
 * BookService
 *
 * @description 도서 관련 서비스
 *
 *
 *****************************************************************
 *
 * @version 1.3.2    2019/11/17 19:18     dorbae	네이버 상세 페이지 오류 수정
 * @version 1.3.1    2019/11/17 19:11     dorbae	네이버 페이징 오류 수정
 * @version 1.3.0    2019/11/17 14:49     dorbae	ISBN 2개일 경우 발생하던 오류 수정
 * @version 1.2.0    2019/11/17 06:25     dorbae	KAKAO/NAVER API FAILOVER 구성
 * @version 1.1.0    2019/11/16 21:22     dorbae	책 세부정보 출판일 정보 추가. LocalDatetime 적용
 * @version 1.0.0    2019/11/12 00:33     dorbae	최초 생성
 * @since 2019/11/12 0:33
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
@Service
public class BookService {

    private static final Logger LOG = LoggerFactory.getLogger(BookService.class);

    private static final String KAKAO_API_REST_API_KEY = "9911845e5989bbb1b0a59f5799083967";  // TODO: 환경변수로 바꿔야 함
    private static final String KAKAO_API_BOOK_SEARCH_URL = "https://dapi.kakao.com/v3/search/book";
    private static final String NAVER_API_CLIENT_ID = "j8kM8nPlkbHdW23aVh8J";  // TODO: 환경변수로 바꿔야 함
    private static final String NAVER_API_CLIENT_SECRET = "k1JX7Qq_p1";  // TODO: 환경변수로 바꿔야 함
    private static final String NAVER_API_BOOK_SEARCH_BASIC_URL = "https://openapi.naver.com/v1/search/book.xml";
    private static final String NAVER_API_BOOK_SEARCH_DETAIL_URL = "https://openapi.naver.com/v1/search/book_adv.xml";
    private static final DateTimeFormatter ISO_DATETIME_ASIA_SEOUL_FORMATTER = DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.of("Asia/Seoul"));
    private static final DateTimeFormatter DATE_YYYYMMDD_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd").withZone(ZoneId.of("Asia/Seoul"));

    //
    // KAKAO API JSON Key list
    //
    private static final String KAKAO_API_JSON_KEY_DOCS = "documents";
    private static final String KAKAO_API_JSON_KEY_DOCS_AUTHORS = "authors";
    private static final String KAKAO_API_JSON_KEY_DOCS_CONTENTS = "contents";
    private static final String KAKAO_API_JSON_KEY_DOCS_DATETIME = "datetime";
    private static final String KAKAO_API_JSON_KEY_DOCS_ISBN = "isbn";
    private static final String KAKAO_API_JSON_KEY_DOCS_PUBLISHER = "publisher";
    private static final String KAKAO_API_JSON_KEY_DOCS_PRICE = "price";
    private static final String KAKAO_API_JSON_KEY_DOCS_SALE_PRICE = "sale_price";
    private static final String KAKAO_API_JSON_KEY_DOCS_STATUS = "status";
    private static final String KAKAO_API_JSON_KEY_DOCS_THUMNAIL = "thumbnail";
    private static final String KAKAO_API_JSON_KEY_DOCS_TITLE = "title";
    private static final String KAKAO_API_JSON_KEY_DOCS_TRANSLATORS = "translators";
    private static final String KAKAO_API_JSON_KEY_DOCS_URL = "url";
    private static final String KAKAO_API_JSON_KEY_META = "meta";
    private static final String KAKAO_API_JSON_KEY_META_IS_END = "is_end";
    private static final String KAKAO_API_JSON_KEY_META_PAGEABLE_COUNT = "pageable_count";
    private static final String KAKAO_API_JSON_KEY_META_TOTAL_COUNT = "total_count";

    //
    // NAVER API XML Tag List
    //
    private static final String NAVER_API_XML_KEY_TOTAL = "total";
    private static final String NAVER_API_XML_KEY_ITEMS = "items";
    private static final String NAVER_API_XML_KEY_ITEM = "item";
    private static final String NAVER_API_XML_KEY_ITEMS_TITLE = "title";
    private static final String NAVER_API_XML_KEY_ITEMS_IMAGE = "image";
    private static final String NAVER_API_XML_KEY_ITEMS_AUTHOR = "author"; // 공동 저자 구분자 |
    private static final String NAVER_API_XML_KEY_ITEMS_PRICE = "price";
    private static final String NAVER_API_XML_KEY_ITEMS_DISCOUNT = "discount";
    private static final String NAVER_API_XML_KEY_ITEMS_PUBLISHER = "publisher";
    private static final String NAVER_API_XML_KEY_ITEMS_PUBDATE = "pubdate";
    private static final String NAVER_API_XML_KEY_ITEMS_ISBN = "isbn";
    private static final String NAVER_API_XML_KEY_ITEMS_DESCRIPTION = "description";

    @Autowired
    private ObjectMapper mapper;

    /**
     * 키워드 검색
     * <p>
     * // TODO: 과제 2. 책 검색
     *
     * @param keyword 검색 키워드
     * @param target  검색 타입. 기본은 전체
     * @param sort    정렬. 기본은 정렬 X
     * @param page    조회 페이지. 기본 1
     * @return
     */
    public BookPagingDto retrievalBooks(String keyword, String target, String sort, int page) throws ResponseStatusException {
        String apiData = null;
        try {
            apiData = this.retrievalBooksFromKakao(keyword, target, sort, page);
        } catch (ApiNotReachableException e) {      // KAKAO API 장애
            try {
                apiData = this.retrievalBooksFromNaver(keyword, target, sort, page);
                return this.parseBooksFromNaver(apiData);
            } catch (Exception ex) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to access external API service.");
            }
        }
        return this.parseBooksFromKakao(apiData);
    }

    /**
     * 책 정보 가져오기
     * <p>
     * // TODO: 과제3. 검색된 책의 상세 조회
     *
     * @param isbn
     * @return JSON -> Map<String,Object>
     */
    public BookDetailDto retrievalBookByIsbn(String isbn) throws ResponseStatusException {
        String apiData = null;
        try {
            apiData = this.retrievalBooksFromKakao(isbn, BookTarget.ISBN.getCode(), null, 1);
        } catch (ApiNotReachableException e) {      // KAKAO API 장애
            try {
                apiData = this.retrievalBooksFromNaver(isbn, BookTarget.ISBN.getCode(), null, 1);
                return this.parseBookFromNaver(apiData);
            } catch (Exception ex) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to access external API service.");
            }
        }
        return this.parseBookFromKakao(apiData);
    }

    /////////////////////////////////////////////////////////////////
    //
    // KAKAO
    //
    /////////////////////////////////////////////////////////////////

    /**
     * KAKAO API에서 데이터 검색
     * https://developers.kakao.com/docs/restapi/search#%EC%B1%85-%EA%B2%80%EC%83%89
     *
     * @param target 검색 타입. 기본은 전체
     * @param sort   정렬. 기본은 정렬 X
     * @param page   조회 페이지. 기본 1
     * @return
     * @throws ApiNotReachableException 카카오 API 서비스 장애
     * @throws ResponseStatusException
     */
    private String retrievalBooksFromKakao(String keyword, String target, String sort, int page) throws ApiNotReachableException {
        //
        // KAKAO API
        //
        // Header
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "KakaoAK " + KAKAO_API_REST_API_KEY);

        // Query parameter
        Map<String, String> params = new HashMap<>();
        if (!StringUtils.isEmpty(target)) {
            params.put("target", target);
            // ISBN으로 검색
            if (BookTarget.ISBN.getCode().equals(target)) {
                // 국제 표준 도서번호(ISBN10 ISBN13) (ISBN10,ISBN13 중 하나 이상 존재
                String[] isbnArr = StringUtils.splitPreserveAllTokens(keyword, ' ');
                if (isbnArr.length > 1) {
                    keyword = isbnArr[1];    // ISBN13 우선도
                }
            }
        }
        params.put("query", keyword);

        if (!StringUtils.isEmpty(sort)) {
            params.put("sort", sort);
        }

        // Paging 처리
        if (page > 1) {
            params.put("page", Integer.toString(page));
        }

        try {
            return HttpUtil.getHttpGetAsString(KAKAO_API_BOOK_SEARCH_URL, headers, params);
        } catch (IOException e) {
            LOG.error("Failed to get HTTP data in KAKAO API", e);
            throw new ApiNotReachableException("Failed to access KAKAO API.", e);
        }
    }

    /**
     * KAKAO 책 리스트 API 데이터 파싱
     *
     * @param data
     * @return
     */
    private BookPagingDto parseBooksFromKakao(String data) {
        if (data == null) {
            return null;
        }

        Map<String, Object> jsonData = null;
        try {
            jsonData = mapper.readValue(data, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            LOG.error("Failed to parse book data as JSON", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to parse book data.");
        }

        Object meta = jsonData.get(KAKAO_API_JSON_KEY_META);
        if (meta == null) {
            return null;
        }

        //
        // JSON 데이터 페이징 객체로 변환
        //
        BookPagingDto bookPagingDto = new BookPagingDto();
        if (!(meta instanceof java.util.Map)) {    // java.util.LinkedHashMap
            return null;
        }
        Map metaMap = (Map) meta;
        bookPagingDto.setPageableCount((int) metaMap.get(KAKAO_API_JSON_KEY_META_PAGEABLE_COUNT));
        bookPagingDto.setTotalCount((int) metaMap.get(KAKAO_API_JSON_KEY_META_TOTAL_COUNT));


        Object documents = jsonData.get(KAKAO_API_JSON_KEY_DOCS);

        List<BookSimpleDto> books = new ArrayList<>();
        bookPagingDto.setBooks(books);
        // 책 결과 없을 경우
        if (documents == null || !(documents instanceof java.util.List)) {     // java.util.ArrayList
            return bookPagingDto;
        }

        // 책 정보 간소화 객체 리스트 변환
        ((List) documents).stream().forEach(book -> {
            Map bookInfo = (Map) book;
            BookSimpleDto simpleDto = BookSimpleDto.builder().title((String) bookInfo.get(KAKAO_API_JSON_KEY_DOCS_TITLE))
                    .authors((String) ((List) bookInfo.get(KAKAO_API_JSON_KEY_DOCS_AUTHORS)).stream().collect(Collectors.joining(",")))
                    .publisher((String) bookInfo.get(KAKAO_API_JSON_KEY_DOCS_PUBLISHER))
                    .salesPrice((int) bookInfo.get(KAKAO_API_JSON_KEY_DOCS_SALE_PRICE))
                    .thumbnail((String) bookInfo.get(KAKAO_API_JSON_KEY_DOCS_THUMNAIL))
                    .isbn((String) bookInfo.get(KAKAO_API_JSON_KEY_DOCS_ISBN))
                    .build();

            books.add(simpleDto);
        });

        return bookPagingDto;
    }

    /**
     * KAKAO 책 리스트 API 데이터 파싱
     *
     * @param data
     * @return
     */
    private BookDetailDto parseBookFromKakao(String data) {
        if (data == null) {
            return null;
        }

        Map<String, Object> jsonData = null;
        try {
            jsonData = mapper.readValue(data, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            LOG.error("Failed to parse book data as JSON", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to parse book data.");
        }

        Object meta = jsonData.get(KAKAO_API_JSON_KEY_META);
        if (meta == null) {
            return null;
        }

        try {
            //
            // JSON 데이터 객체로 변환
            //
            Object documents = jsonData.get(KAKAO_API_JSON_KEY_DOCS);
            if (!(documents instanceof java.util.List || ((List) documents).size() < 1)) {     // java.util.ArrayList
                return null;
            }

            // 책 정보
            Map bookInfo = null;
            try {
                bookInfo = (Map) ((List) documents).get(0);
            } catch (IndexOutOfBoundsException e) {
                LOG.debug("Got nothing from API.");
                return null;
            }

            BookDetailDto detailDto = BookDetailDto.builder().title((String) bookInfo.get(KAKAO_API_JSON_KEY_DOCS_TITLE))
                    .authors((String) ((List) bookInfo.get(KAKAO_API_JSON_KEY_DOCS_AUTHORS)).stream().collect(Collectors.joining(",")))
                    .publisher((String) bookInfo.get(KAKAO_API_JSON_KEY_DOCS_PUBLISHER))
                    .salesPrice((int) bookInfo.get(KAKAO_API_JSON_KEY_DOCS_SALE_PRICE))
                    .thumbnail((String) bookInfo.get(KAKAO_API_JSON_KEY_DOCS_THUMNAIL))
                    .isbn((String) bookInfo.get(KAKAO_API_JSON_KEY_DOCS_ISBN))
                    .contents((String) bookInfo.get(KAKAO_API_JSON_KEY_DOCS_CONTENTS))
                    .price((int) bookInfo.get(KAKAO_API_JSON_KEY_DOCS_PRICE))
                    .datetime(LocalDateTime.parse((String) bookInfo.get(KAKAO_API_JSON_KEY_DOCS_DATETIME), ISO_DATETIME_ASIA_SEOUL_FORMATTER))
                    .build();

            return detailDto;

        } catch (Exception e) {
            LOG.error("Failed to transform book info.", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to transform book info.");
        }
    }

    /////////////////////////////////////////////////////////////////
    //
    // NAVER
    //
    /////////////////////////////////////////////////////////////////

    /**
     * NAVER API에서 데이터 검색
     * https://developers.naver.com/docs/search/book/
     *
     * @param target 검색 타입. 기본은 전체
     * @param sort   정렬. 기본은 정렬 X
     * @param page   조회 페이지. 기본 1
     * @return
     * @throws ApiNotReachableException 네이버 API 서비스 장애
     * @throws ResponseStatusException
     */
    private String retrievalBooksFromNaver(String keyword, String target, String sort, int page) throws ApiNotReachableException {
        //
        // NAVER API
        //

        // 상세 검색은 URL 별도
//        String url = (StringUtils.isEmpty(target) ? NAVER_API_BOOK_SEARCH_BASIC_URL : NAVER_API_BOOK_SEARCH_DETAIL_URL);
        String url = ((target == null || target.trim().isEmpty()) ? NAVER_API_BOOK_SEARCH_BASIC_URL : NAVER_API_BOOK_SEARCH_DETAIL_URL);
        LOG.debug("url={}", url);

        // Header
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Naver-Client-Id", NAVER_API_CLIENT_ID);
        headers.put("X-Naver-Client-Secret", NAVER_API_CLIENT_SECRET);

        // Query parameter
        Map<String, String> params = new HashMap<>();
        if (BookTarget.ISBN.getCode().equals(target)) {        // 상세 검색
            // ISBN으로 검색
            // 국제 표준 도서번호(ISBN10 ISBN13) (ISBN10,ISBN13 중 하나 이상 존재
            String[] isbnArr = StringUtils.splitPreserveAllTokens(keyword, ' ');
            if (isbnArr.length > 1) {
                keyword = isbnArr[1];    // ISBN13 우선도
            }
            params.put("d_isbn", keyword);
        } else {
            params.put("query", keyword);                   // 전체 검색
        }

        // Paging 처리
        if (page > 1) {
            params.put("start", Integer.toString(page * 10));    // 책 인덱스 (한페이지당 10권)
        }

        try {
            return HttpUtil.getHttpGetAsString(url, headers, params);
        } catch (IOException e) {
            LOG.error("Failed to get HTTP data in NAVER API", e);
            throw new ApiNotReachableException("Failed to access NAVER API.");
        }
    }

    /**
     * NAVER API 북 리스트 데이터 파싱
     *
     * @param data
     * @return
     */
    private BookPagingDto parseBooksFromNaver(String data) throws Exception {
        if (data == null) {
            return null;
        }

        //
        // XML 데이터 페이징 객체로 변환
        //
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new InputSource(new StringReader(data)));
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("channel");
            if (nodeList == null || nodeList.getLength() < 1) {
                return null;
            }

            BookPagingDto bookPagingDto = new BookPagingDto();
            bookPagingDto.setPageableCount(1000);
            List<BookSimpleDto> books = new ArrayList<>();
            bookPagingDto.setBooks(books);
            BookSimpleDto bookSimpleDto = null;
            nodeList = nodeList.item(0).getChildNodes();
            int size = nodeList.getLength(), itemChildrenCount = 0, jj = 0;
            String nodeName, nodeValue = null;
            Node node = null;
            NodeList itemNodeList = null;
            for (int ll = 0; ll < size; ll++) {
                node = nodeList.item(ll);
                nodeName = node.getNodeName();
                if (NAVER_API_XML_KEY_ITEM.equals(nodeName)) {
                    bookSimpleDto = new BookSimpleDto();
                    itemNodeList = node.getChildNodes();
                    itemChildrenCount = itemNodeList.getLength();
                    for (jj = 0; jj < itemChildrenCount; jj++) {
                        node = itemNodeList.item(jj);
                        nodeName = node.getNodeName();
                        if (NAVER_API_XML_KEY_ITEMS_AUTHOR.equals(nodeName)) {
                            if (node.hasChildNodes()) {
                                bookSimpleDto.setAuthors(GBStringUtil.null2Empty(node.getFirstChild().getNodeValue()).replace('|', ','));
                            }
                        } else if (NAVER_API_XML_KEY_ITEMS_IMAGE.equals(nodeName)) {
                            bookSimpleDto.setThumbnail(node.hasChildNodes() ? node.getFirstChild().getNodeValue() : null);
                        } else if (NAVER_API_XML_KEY_ITEMS_PUBLISHER.equals(nodeName)) {
                            bookSimpleDto.setPublisher(node.hasChildNodes() ? node.getFirstChild().getNodeValue() : null);
                        } else if (NAVER_API_XML_KEY_ITEMS_TITLE.equals(nodeName)) {
                            bookSimpleDto.setTitle(node.hasChildNodes() ? node.getFirstChild().getNodeValue() : null);
                        } else if (NAVER_API_XML_KEY_ITEMS_ISBN.equals(nodeName)) {
                            bookSimpleDto.setIsbn(node.hasChildNodes() ? node.getFirstChild().getNodeValue() : null);
                        } else if (NAVER_API_XML_KEY_ITEMS_DISCOUNT.equals(nodeName)) {
                            if (node.hasChildNodes()) {
                                nodeValue = node.getFirstChild().getNodeValue();
                                bookSimpleDto.setSalesPrice(nodeValue == null ? 0 : Integer.parseInt(nodeValue));
                            }
                        }
                    }

                    books.add(bookSimpleDto);

                } else if (NAVER_API_XML_KEY_TOTAL.equals(nodeName)) {
                    bookPagingDto.setTotalCount(Integer.parseInt(node.getFirstChild().getNodeValue()));
                }
            }

            return bookPagingDto;

        } catch (ParserConfigurationException | SAXException | IOException e) {
            LOG.error("Failed to parse xml book data", e);
            throw e;
        }
        // $END:XML 데이터 페이징 객체로 변환
    }

    /**
     * 특정 도서 가져오기
     *
     * @param data
     * @return
     */
    private BookDetailDto parseBookFromNaver(String data) throws Exception {
        if (data == null) {
            return null;
        }

        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new InputSource(new StringReader(data)));
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("channel");
            if (nodeList == null || nodeList.getLength() < 1) {
                return null;
            }

            //
            // XML 데이터 파싱
            //
            BookDetailDto bookDetailDto = null;
            nodeList = nodeList.item(0).getChildNodes();
            int size = nodeList.getLength(), itemChildrenCount = 0, jj = 0;
            String nodeName, nodeValue = null;
            Node node = null;
            NodeList itemNodeList = null;
            for (int ll = 0; ll < size; ll++) {
                node = nodeList.item(ll);
                nodeName = node.getNodeName();
                if (NAVER_API_XML_KEY_ITEM.equals(nodeName)) {
                    bookDetailDto = new BookDetailDto();
                    itemNodeList = node.getChildNodes();
                    itemChildrenCount = itemNodeList.getLength();
                    for (jj = 0; jj < itemChildrenCount; jj++) {
                        node = itemNodeList.item(jj);
                        nodeName = node.getNodeName();
                        if (node.hasChildNodes()) {
                            if (NAVER_API_XML_KEY_ITEMS_AUTHOR.equals(nodeName)) {
                                bookDetailDto.setAuthors(GBStringUtil.null2Empty(node.getFirstChild().getNodeValue()).replace('|', ','));
                            } else if (NAVER_API_XML_KEY_ITEMS_DESCRIPTION.equals(nodeName)) {
                                bookDetailDto.setContents(node.getFirstChild().getNodeValue());
                            } else if (NAVER_API_XML_KEY_ITEMS_ISBN.equals(nodeName)) {
                                bookDetailDto.setIsbn(node.getFirstChild().getNodeValue());
                            } else if (NAVER_API_XML_KEY_ITEMS_IMAGE.equals(nodeName)) {
                                bookDetailDto.setThumbnail(node.getFirstChild().getNodeValue());
                            } else if (NAVER_API_XML_KEY_ITEMS_PUBLISHER.equals(nodeName)) {
                                bookDetailDto.setPublisher(node.getFirstChild().getNodeValue());
                            } else if (NAVER_API_XML_KEY_ITEMS_TITLE.equals(nodeName)) {
                                bookDetailDto.setTitle(node.getFirstChild().getNodeValue());
                            } else if (NAVER_API_XML_KEY_ITEMS_PRICE.equals(nodeName)) {
                                nodeValue = node.getFirstChild().getNodeValue();
                                bookDetailDto.setPrice(nodeValue == null ? 0 : Integer.parseInt(nodeValue));
                            } else if (NAVER_API_XML_KEY_ITEMS_DISCOUNT.equals(nodeName)) {
                                nodeValue = node.getFirstChild().getNodeValue();
                                bookDetailDto.setSalesPrice(nodeValue == null ? 0 : Integer.parseInt(nodeValue));
                            } else if (NAVER_API_XML_KEY_ITEMS_PUBDATE.equals(nodeName)) {
                                nodeValue = node.getFirstChild().getNodeValue();
                                try {
                                    if (nodeValue != null) {
                                        bookDetailDto.setDatetime(LocalDateTime.parse(nodeValue, DATE_YYYYMMDD_FORMATTER));
                                    }
                                } catch (Exception e) {
                                    LOG.warn("Failed to convert date from Naver API. {}", e.getMessage());
                                }
                            }
                        }
                    }
                }
            }
            // $END:XML 데이터 파싱

            return bookDetailDto;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            LOG.error("Failed to parse xml book data", e);
            throw e;
        }
    }
}
