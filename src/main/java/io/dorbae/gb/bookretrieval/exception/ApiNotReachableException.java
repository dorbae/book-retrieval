package io.dorbae.gb.bookretrieval.exception;

import java.io.IOException;

/*
 *****************************************************************
 *
 * ApiNotReachableException
 *
 * @description API 서비스 접근 에러
 *
 *
 *****************************************************************
 *
 * @version 1.0.0    2019/11/17 5:04     dorbae	최초 생성
 * @since 2019/11/17 5:04
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
public class ApiNotReachableException extends IOException {
    public ApiNotReachableException() {
    }

    public ApiNotReachableException(String s) {
        super(s);
    }

    public ApiNotReachableException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ApiNotReachableException(Throwable throwable) {
        super(throwable);
    }
}
