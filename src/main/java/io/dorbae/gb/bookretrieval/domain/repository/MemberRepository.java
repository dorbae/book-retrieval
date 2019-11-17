package io.dorbae.gb.bookretrieval.domain.repository;

import io.dorbae.gb.bookretrieval.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
 *****************************************************************
 *
 * MemberRepository
 *
 * @description MemberRepository
 *
 *
 *****************************************************************
 *
 * @version 1.1.0    2019/11/14 08:18     dorbae	UserRepository -> MemberRepository 클래스명 변경
 * @version 1.0.0    2019/11/11 18:33     dorbae	최초 생성
 * @since 2019/11/11 18:33
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByAccount(String account);
}
