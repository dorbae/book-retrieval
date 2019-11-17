package io.dorbae.gb.bookretrieval.domain;

/*
 *****************************************************************
 *
 * BookTarget
 *
 * @description BookTarget
 *
 *
 *****************************************************************
 *
 * @version 1.0.0    2019/11/12 17:26     dorbae	최초 생성
 * @since 2019/11/12 17:26
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
public enum BookTarget {
    TITLE("title", "제목에서 검색"), ISBN("isbn", "ISBN에서 검색"), PUBLISHER("publisher", "출판사에서 검색"), PERSON(
            "person", "인명에서 검색"), ANY("", "전체");
    private String code;
    private String description;

    private BookTarget(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }
}
