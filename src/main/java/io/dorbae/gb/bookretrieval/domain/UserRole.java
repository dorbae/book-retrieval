package io.dorbae.gb.bookretrieval.domain;

/*
 *****************************************************************
 *
 * UserRole
 *
 * @description Spring Security User 권한 종류
 *
 *
 *****************************************************************
 *
 * @version 1.0.0    2019/11/14 5:08     dorbae	최초 생성
 * @since 2019/11/14 5:08
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
public enum UserRole {
    ADMIN("ROLE_ADMIN"), MEMBER("ROLE_MEMBER");

    private String code;

    private UserRole(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

}
