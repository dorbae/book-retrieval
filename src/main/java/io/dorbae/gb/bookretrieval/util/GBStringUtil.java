package io.dorbae.gb.bookretrieval.util;

/*
 *****************************************************************
 *
 * GBStringUtil
 *
 * @description GBStringUtil
 *
 *
 *****************************************************************
 *
 * @version 1.0.0    2019/11/17 17:58     dorbae	최초 생성
 * @since 2019/11/17 17:58
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
public class GBStringUtil {
    /**
     * Null -> Empty
     * @param str
     * @return
     */
    public static final String null2Empty(String str) {
        return str == null ? "" : str;
    }
}
