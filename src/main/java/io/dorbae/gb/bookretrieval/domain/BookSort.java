package io.dorbae.gb.bookretrieval.domain;

/*
 *****************************************************************
 *
 * BookSort
 *
 * @description BookSort
 *
 *
 *****************************************************************
 *
 * @version 1.0.0    2019/11/12 17:24     dorbae	최초 생성
 * @since 2019/11/12 17:24
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
public enum BookSort {
    ACCUURANCY("accuracy"), LASTEST("latest");

    private String code;
    private BookSort(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
