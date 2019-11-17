package io.dorbae.gb.bookretrieval.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/*
 *****************************************************************
 *
 * Member
 *
 * @description Member Entity
 *
 *
 *****************************************************************
 *
 * @version 1.1.0    2019/11/14 08:18     dorbae	User -> MemberEntity 클래스명 변경
 * @version 1.0.0    2019/11/11 18:12     dorbae	최초 생성
 * @since 2019/11/11 18:12
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
// JPA에서 Entity 클래스를 생성 허용 & 기본 생성자 사용 방지
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
@Table(name = "member")
public class Member extends BaseTime implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String account;
    private String password;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member memberEntity = (Member) o;
        return Objects.equals(account, memberEntity.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account);
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
