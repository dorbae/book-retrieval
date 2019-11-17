package io.dorbae.gb.bookretrieval.dto;

import io.dorbae.gb.bookretrieval.domain.entity.BaseTime;
import io.dorbae.gb.bookretrieval.domain.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/*
 *****************************************************************
 *
 * MemberDto
 *
 * @description MemberDto
 *
 *
 *****************************************************************
 *
 * @version 1.0.0    2019/11/14 8:26     dorbae	최초 생성
 * @since 2019/11/14 8:26
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class MemberDto extends BaseTime {
    private Long id;
    private String account;
    private String password;
    private String name;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public Member toEntity() {
        return Member.builder()
                .id(this.id)
                .account(this.account)
                .password(this.password)
                .name(this.name)
                .build();
    }
}