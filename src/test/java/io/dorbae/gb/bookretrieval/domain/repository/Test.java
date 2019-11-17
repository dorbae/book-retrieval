package io.dorbae.gb.bookretrieval.domain.repository;

import io.dorbae.gb.bookretrieval.dto.BookDetailDto;
import io.dorbae.gb.bookretrieval.dto.BookPagingDto;
import io.dorbae.gb.bookretrieval.dto.BookSimpleDto;
import org.junit.platform.commons.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.ArrayList;
import java.util.List;

/*
 *****************************************************************
 *
 * Test
 *
 * @description Test
 *
 *
 *****************************************************************
 *
 * @version 1.0.0    2019/11/17 16:21     dorbae	최초 생성
 * @since 2019/11/17 16:21
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
public class Test {
    private static final Logger LOG = LoggerFactory.getLogger(Test.class);

    private static final String NAVER_API_CLIENT_ID = "j8kM8nPlkbHdW23aVh8J";  // TODO: 환경변수로 바꿔야 함
    private static final String NAVER_API_CLIENT_SECRET = "k1JX7Qq_p1";  // TODO: 환경변수로 바꿔야 함
    private static final String NAVER_API_BOOK_SEARCH_URL = "https://openapi.naver.com/v1/search/book.xml";

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

    private static final String XML_DATA = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<rss version=\"2.0\">\n" +
            "    <channel>\n" +
            "        <title>Naver Open API - book ::'java'</title>\n" +
            "        <link>https://search.naver.com</link>\n" +
            "        <description>Naver Search Result</description>\n" +
            "        <lastBuildDate>Sun, 17 Nov 2019 15:58:15 +0900</lastBuildDate>\n" +
            "        <total>13406</total>\n" +
            "        <start>1</start>\n" +
            "        <display>10</display>\n" +
            "        <item>\n" +
            "            <title>이펙티브 자바 (Effective &lt;b&gt;Java&lt;/b&gt;)</title>\n" +
            "            <link>http://book.naver.com/bookdb/book_detail.php?bid=14097515</link>\n" +
            "            <image>https://bookthumb-phinf.pstatic.net/cover/140/975/14097515.jpg?type=m1&amp;udate=20181023</image>\n" +
            "            <author>조슈아 블로크</author>\n" +
            "            <price>36000</price>\n" +
            "            <discount>32400</discount>\n" +
            "            <publisher>인사이트</publisher>\n" +
            "            <pubdate>20181101</pubdate>\n" +
            "            <isbn>8966262287 9788966262281</isbn>\n" +
            "            <description>자바 플랫폼 모범 사례 완벽 가이드 - &lt;b&gt;JAVA&lt;/b&gt; 7, 8, 9 대응자바 6 출시 직후 출간된 『이펙티브 자바 2판』 이후로 자바는 커다란\n" +
            "                변화를 겪었다. 그래서... 포함한 타입 추론\n" +
            "                - @SAFEVARARGS 애너테이션\n" +
            "                - TRY-WITH-RESOURCES 문\n" +
            "                - OPTIONAL T 인터페이스, &lt;b&gt;JAVA&lt;/b&gt;.TIME, 컬렉션의 편의 팩터리 메서드 등의 새로운 라이브러리 기능\n" +
            "            </description>\n" +
            "        </item>\n" +
            "        <item>\n" +
            "            <title>&lt;b&gt;Java&lt;/b&gt;의 정석 (최신 &lt;b&gt;Java&lt;/b&gt; 8.0 포함)</title>\n" +
            "            <link>http://book.naver.com/bookdb/book_detail.php?bid=10191151</link>\n" +
            "            <image>https://bookthumb-phinf.pstatic.net/cover/101/911/10191151.jpg?type=m1&amp;udate=20190204</image>\n" +
            "            <author>남궁성</author>\n" +
            "            <price>30000</price>\n" +
            "            <discount>27000</discount>\n" +
            "            <publisher>도우출판</publisher>\n" +
            "            <pubdate>20160127</pubdate>\n" +
            "            <isbn>8994492038 9788994492032</isbn>\n" +
            "            <description>자바의 기초부터 실전활용까지 모두 담다!자바의 기초부터 객제지향개념을 넘어 실전활용까지 수록한『&lt;b&gt;JAVA&lt;/b&gt;의 정석』. 저자의 오랜 실무경험과\n" +
            "                강의한... 더불어 기존의 경력자들을 위해 자바 최신기능인 람다와 스트림과 그 밖의 자바의 최신버젼 &lt;b&gt;JAVA&lt;/b&gt;8의 새로운 기능까지 자세하게 설명하고 있다.\n" +
            "            </description>\n" +
            "        </item>\n" +
            "        <item>\n" +
            "            <title>명품 &lt;b&gt;JAVA&lt;/b&gt; Programming (귀로 배우는 자바가 아니라, 눈으로 몸으로 배우는 자바강좌)</title>\n" +
            "            <link>http://book.naver.com/bookdb/book_detail.php?bid=13650995</link>\n" +
            "            <image>https://bookthumb-phinf.pstatic.net/cover/136/509/13650995.jpg?type=m1&amp;udate=20180619</image>\n" +
            "            <author>황기태|김효수</author>\n" +
            "            <price>33000</price>\n" +
            "            <discount>31350</discount>\n" +
            "            <publisher>생능출판사</publisher>\n" +
            "            <pubdate>20180601</pubdate>\n" +
            "            <isbn>897050947X 9788970509471</isbn>\n" +
            "            <description>자바(&lt;b&gt;JAVA&lt;/b&gt;)는 그 이전 시대에 있었던 프로그래밍 언어에서 한 차원 진화된 개념으로 개발된 가히 혁명적... &lt;b&gt;JAVA&lt;/b&gt;\n" +
            "                8, 9에서 인터페이스 정의가 바뀌었기 때문에 5장 인터페이스 부분을 수정하였다.\n" +
            "                3. 6.7절 WRAPPER 클래스 부분을 갱신하였다. &lt;b&gt;JAVA&lt;/b&gt; 9부터 생성자를 이용하여 WRAPPER 객체를 생성하는 방법이...\n" +
            "            </description>\n" +
            "        </item>\n" +
            "        <item>\n" +
            "            <title>이것이 자바다 (신용권의 &lt;b&gt;Java&lt;/b&gt; 프로그래밍 정복)</title>\n" +
            "            <link>http://book.naver.com/bookdb/book_detail.php?bid=8589375</link>\n" +
            "            <image>https://bookthumb-phinf.pstatic.net/cover/085/893/08589375.jpg?type=m1&amp;udate=20190327</image>\n" +
            "            <author>신용권</author>\n" +
            "            <price>30000</price>\n" +
            "            <discount>27000</discount>\n" +
            "            <publisher>한빛미디어</publisher>\n" +
            "            <pubdate>20150105</pubdate>\n" +
            "            <isbn>8968481474 9788968481475</isbn>\n" +
            "            <description>『이것이 자바다』은 15년 이상 자바 언어를 교육해온 자바 전문강사의 노하우를 아낌 없이 담아낸 자바 입문서이다. 자바 입문자를 배려한 친절한 설명과 배려로 1장에 풀인원\n" +
            "                설치 방법을 제공하여 쉽게 학습환경을 구축할 수 있다. 또한 중급 개발자로 나아가기 위한 람다식(14장), JAVAFX(17장), NIO(18...\n" +
            "            </description>\n" +
            "        </item>\n" +
            "        <item>\n" +
            "            <title>윤성우의 열혈 &lt;b&gt;Java&lt;/b&gt; 프로그래밍</title>\n" +
            "            <link>http://book.naver.com/bookdb/book_detail.php?bid=12236206</link>\n" +
            "            <image>https://bookthumb-phinf.pstatic.net/cover/122/362/12236206.jpg?type=m1&amp;udate=20181129</image>\n" +
            "            <author>윤성우</author>\n" +
            "            <price>30000</price>\n" +
            "            <discount>27000</discount>\n" +
            "            <publisher>오렌지미디어</publisher>\n" +
            "            <pubdate>20170705</pubdate>\n" +
            "            <isbn>8996094072 9788996094074</isbn>\n" +
            "            <description>『윤성우의 열혈 &lt;b&gt;JAVA&lt;/b&gt; 프로그래밍』은 최신 내용을 바탕으로 새롭게 집필된 자바책이다. 자바 8이 발표되면서 초보자들에게 어려울 수 있는\n" +
            "                문법적 요소가 상당수 포함이 되었으나 저자 특유의 쉽고 명확한 설명을 통해서 어렵지 않게 해당 내용들을 설명한 책이다. 본서의 설명에는 독자들을 고려한 과하지...\n" +
            "            </description>\n" +
            "        </item>\n" +
            "        <item>\n" +
            "            <title>초보자를 위한 JavaScript 200제</title>\n" +
            "            <link>http://book.naver.com/bookdb/book_detail.php?bid=14602049</link>\n" +
            "            <image>https://bookthumb-phinf.pstatic.net/cover/146/020/14602049.jpg?type=m1&amp;udate=20190918</image>\n" +
            "            <author>고재도|노지연</author>\n" +
            "            <price>25000</price>\n" +
            "            <discount>22500</discount>\n" +
            "            <publisher>정보문화사</publisher>\n" +
            "            <pubdate>20190310</pubdate>\n" +
            "            <isbn>8956748241 9788956748245</isbn>\n" +
            "            <description>JAVASCRIPT로 무엇이든 해낼 수 있다!\n" +
            "\n" +
            "                발 빠르게 진화되면서 세계에서 가장 인기 있는 언어가ß 된 자바스크립트를 설치부터 활용까지 예제별로 친절하게 안내하는 책이다. 본서는 총 5개의 파트(입문, 초급, 중급, 활용, 실무)로\n" +
            "                구성되어 있으며 200개의 예제를 학습하면서 자바스크립트를 익힐 수 있다....\n" +
            "            </description>\n" +
            "        </item>\n" +
            "        <item>\n" +
            "            <title>모던 웹을 위한 JavaScript + jQuery 입문 (ECMAScript 5/6, jQuery 3.X 대응)</title>\n" +
            "            <link>http://book.naver.com/bookdb/book_detail.php?bid=11990519</link>\n" +
            "            <image>https://bookthumb-phinf.pstatic.net/cover/119/905/11990519.jpg?type=m1&amp;udate=20190710</image>\n" +
            "            <author>윤인성</author>\n" +
            "            <price>32000</price>\n" +
            "            <discount>28800</discount>\n" +
            "            <publisher>한빛미디어</publisher>\n" +
            "            <pubdate>20170501</pubdate>\n" +
            "            <isbn>8968483558 9788968483554</isbn>\n" +
            "            <description>최신 버전을 적용한 개정판!『모던 웹을 위한 JAVASCRIPT + JQUERY 입문』은 클라이언트 자바스크립트와 관련된 거의 모든 내용을 다루며, 표준 자바스크립트\n" +
            "                프레임워크로 채택된 JQUERY도 함께 다룬다. 거기서 한걸음 더 나아가 현대적 웹 애플리케이션을 개발하기 위한 AJAX와 JQUERY...\n" +
            "            </description>\n" +
            "        </item>\n" +
            "        <item>\n" +
            "            <title>명품 HTML5 + CSS3 + Javascript 웹 프로그래밍</title>\n" +
            "            <link>http://book.naver.com/bookdb/book_detail.php?bid=11575805</link>\n" +
            "            <image>https://bookthumb-phinf.pstatic.net/cover/115/758/11575805.jpg?type=m1&amp;udate=20180110</image>\n" +
            "            <author>황기태</author>\n" +
            "            <price>28000</price>\n" +
            "            <discount>26320</discount>\n" +
            "            <publisher>생능출판사</publisher>\n" +
            "            <pubdate>20170116</pubdate>\n" +
            "            <isbn>8970508880 9788970508887</isbn>\n" +
            "            <description>웹 프로그래밍을 가장 쉽게 익힐 수 있는 책&amp;#x0D;&amp;#x0D;웹 페이지 제작은 HTML 태그를 이용하여 페이지를 만들고, CSS3로 모양을 꾸미고,\n" +
            "                자바스크립트로 사용자 인터페이스나 응용프로그램을 작성하는 과정으로 이루어진다. 이 세 가지 지식이 모두 필요하므로 웹 프로그래밍은 쉬운 것 같으면서도...\n" +
            "            </description>\n" +
            "        </item>\n" +
            "        <item>\n" +
            "            <title>초보자를 위한 &lt;b&gt;Java&lt;/b&gt; 200제</title>\n" +
            "            <link>http://book.naver.com/bookdb/book_detail.php?bid=13746617</link>\n" +
            "            <image>https://bookthumb-phinf.pstatic.net/cover/137/466/13746617.jpg?type=m1&amp;udate=20180705</image>\n" +
            "            <author>조효은</author>\n" +
            "            <price>28000</price>\n" +
            "            <discount>25200</discount>\n" +
            "            <publisher>정보문화사</publisher>\n" +
            "            <pubdate>20180705</pubdate>\n" +
            "            <isbn>8956747857 9788956747859</isbn>\n" +
            "            <description>활용되는 &lt;b&gt;Java&lt;/b&gt; 예제 200개를 엄선하여 수록하였습니다. 빌보드 차트 만들기, 카드 게임 만들기, 인사 관리(HRM)용 어플리케이션\n" +
            "                만들기, 채팅 만들기 등의 예제를 통해 자연스럽게 &lt;b&gt;Java&lt;/b&gt; 프로그래밍에 익숙해지고 흥미를 느낄 수 있도록 집필하였으며, 예제를 하나하나 실습하다 보면\n" +
            "                &lt;b&gt;Java&lt;/b&gt; 프로그래밍...\n" +
            "            </description>\n" +
            "        </item>\n" +
            "        <item>\n" +
            "            <title>HTML5,CSS3,JavaScript,jQuery Mobile 중심의 Front-End 웹프로그래밍</title>\n" +
            "            <link>http://book.naver.com/bookdb/book_detail.php?bid=15407631</link>\n" +
            "            <image>https://bookthumb-phinf.pstatic.net/cover/154/076/15407631.jpg?type=m1&amp;udate=20191001</image>\n" +
            "            <author>김형수</author>\n" +
            "            <price>22000</price>\n" +
            "            <discount>19800</discount>\n" +
            "            <publisher>마지원</publisher>\n" +
            "            <pubdate>20191023</pubdate>\n" +
            "            <isbn>1188127535 9791188127535</isbn>\n" +
            "            <description>▶ 이 책은 FRONT-END 웹프로그래밍을 다룬 이론서입니다.</description>\n" +
            "        </item>\n" +
            "    </channel>\n" +
            "</rss>";

    public static void main(String[] args) {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new InputSource(new StringReader(XML_DATA)));
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("channel");
            if (nodeList == null || nodeList.getLength() < 1) {
                return;
            }

            BookPagingDto bookPagingDto = new BookPagingDto();
            bookPagingDto.setPageableCount(100);
            List<BookSimpleDto> books = new ArrayList<>();
            bookPagingDto.setBooks(books);
            BookSimpleDto bookSimpleDto = null;
            nodeList = nodeList.item(0).getChildNodes();
            int size = nodeList.getLength(), itemChildrenCount = 0, jj = 0;
            String nodeName = null;
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
                            bookSimpleDto.setAuthors(StringUtils.nullSafeToString(node.getFirstChild().getNodeValue()).replace('|', ','));
                        } else if (NAVER_API_XML_KEY_ITEMS_DESCRIPTION.equals(nodeName)) {
                            bookSimpleDto.setIsbn(node.getFirstChild().getNodeValue());
                        } else if (NAVER_API_XML_KEY_ITEMS_IMAGE.equals(nodeName)) {
                            bookSimpleDto.setThumbnail(node.getFirstChild().getNodeValue());
                        } else if (NAVER_API_XML_KEY_ITEMS_PUBLISHER.equals(nodeName)) {
                            bookSimpleDto.setPublisher(node.getFirstChild().getNodeValue());
                        } else if (NAVER_API_XML_KEY_ITEMS_TITLE.equals(nodeName)) {
                            bookSimpleDto.setTitle(node.getFirstChild().getNodeValue());
                        } else if (NAVER_API_XML_KEY_ITEMS_DISCOUNT.equals(nodeName)) {
                            nodeName = node.getFirstChild().getNodeValue();
                            bookSimpleDto.setSalesPrice(nodeName == null ? 0 : Integer.parseInt(nodeName));
                        }
                    }

                    books.add(bookSimpleDto);

                } else if (NAVER_API_XML_KEY_TOTAL.equals(nodeName)) {
                    bookPagingDto.setTotalCount(Integer.parseInt(node.getFirstChild().getNodeValue()));
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            LOG.error("Failed to parse xml book data", e);
        }

    }
//
//    public static void main(String[] args) {
//        try {
//            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
//            Document doc = docBuilder.parse(new InputSource(new StringReader(XML_DATA)));
//            doc.getDocumentElement().normalize();
//            NodeList nodeList = doc.getElementsByTagName("channel");
//            if (nodeList == null || nodeList.getLength() < 1) {
//                return;
//            }
//
//            BookPagingDto bookPagingDto = new BookPagingDto();
//            bookPagingDto.setPageableCount(100);
//            List<BookDetailDto>
//            BookDetailDto bookDetailDto = null;
//            nodeList = nodeList.item(0).getChildNodes();
//            int size = nodeList.getLength(), itemChildrenCount = 0, jj = 0;
//            String nodeName = null;
//            Node node = null;
//            NodeList itemNodeList = null;
//            for (int ll = 0; ll < size; ll++) {
//                node = nodeList.item(ll);
//                nodeName = node.getNodeName();
//                if (NAVER_API_XML_KEY_ITEM.equals(nodeName)) {
//                    bookDetailDto = new BookDetailDto();
//                    itemNodeList = node.getChildNodes();
//                    itemChildrenCount = itemNodeList.getLength();
//                    for (jj = 0; jj < itemChildrenCount; jj++) {
//                        node = itemNodeList.item(jj);
//                        nodeName = node.getNodeName();
//                        if (NAVER_API_XML_KEY_ITEMS_AUTHOR.equals(nodeName)) {
//                            bookDetailDto.setAuthors(StringUtils.nullSafeToString(node.getFirstChild().getNodeValue()).replace('|', ','));
//                        } else if (NAVER_API_XML_KEY_ITEMS_DESCRIPTION.equals(nodeName)) {
//                            bookDetailDto.setContents(node.getFirstChild().getNodeValue());
//                        } else if (NAVER_API_XML_KEY_ITEMS_ISBN.equals(nodeName)) {
//                            bookDetailDto.setIsbn(node.getFirstChild().getNodeValue());
//                        } else if (NAVER_API_XML_KEY_ITEMS_IMAGE.equals(nodeName)) {
//                            bookDetailDto.setThumbnail(node.getFirstChild().getNodeValue());
//                        } else if (NAVER_API_XML_KEY_ITEMS_PUBLISHER.equals(nodeName)) {
//                            bookDetailDto.setPublisher(node.getFirstChild().getNodeValue());
//                        } else if (NAVER_API_XML_KEY_ITEMS_TITLE.equals(nodeName)) {
//                            bookDetailDto.setTitle(node.getFirstChild().getNodeValue());
//                        } else if (NAVER_API_XML_KEY_ITEMS_PRICE.equals(nodeName)) {
//                            nodeName = node.getFirstChild().getNodeValue();
//                            bookDetailDto.setPrice(nodeName == null ? 0 : Integer.parseInt(nodeName));
//                        } else if (NAVER_API_XML_KEY_ITEMS_DISCOUNT.equals(nodeName)) {
//                            nodeName = node.getFirstChild().getNodeValue();
//                            bookDetailDto.setSalesPrice(nodeName == null ? 0 : Integer.parseInt(nodeName));
//                        }
//                    }
//
//                    bo
//
//                } else if (NAVER_API_XML_KEY_TOTAL.equals(nodeName)) {
//                    bookPagingDto.setTotalCount(Integer.parseInt(node.getFirstChild().getNodeValue()));
//                    LOG.debug("totalCount={}", bookPagingDto.getTotalCount());
//                }
//            }
//
//
////            nodeList.
////                    bookPagingDto.setTotalCount(Integer.parseInt((nodeList.item(NAVER_API_XML_KEY_TOTAL).getNodeValue();
//
//            LOG.info("End");
//        } catch (ParserConfigurationException | SAXException | IOException e) {
//            LOG.error("Failed to parse xml book data", e);
////            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to parse book data.");
//        }
//
//    }
}
