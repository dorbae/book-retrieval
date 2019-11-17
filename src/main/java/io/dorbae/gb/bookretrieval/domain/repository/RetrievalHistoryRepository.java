package io.dorbae.gb.bookretrieval.domain.repository;

import io.dorbae.gb.bookretrieval.domain.entity.Member;
import io.dorbae.gb.bookretrieval.domain.entity.RetrievalHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/*
 *****************************************************************
 *
 * RetrievalHistoryRepository
 *
 * @description RetrievalHistoryRepository
 *
 *
 *****************************************************************
 *
 * @version 1.0.0    2019/11/16 23:01     dorbae	Join 오류 수정
 * @version 1.0.0    2019/11/12 18:01     dorbae	최초 생성
 * @since 2019/11/12 18:01
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
public interface RetrievalHistoryRepository extends JpaRepository<RetrievalHistory, Long> {
    /**
     * 로그인 유저 해당 검색 이력 가져오기
     * @param account 사용자 계정명
     * @param pageable 페이징 -> Sort/Limit 활용
     * @return
     */
    @Query(value = "SELECT * FROM RETRIEVAL_HISTORY s WHERE s.account=?1", nativeQuery = true)
    Page<RetrievalHistory> findByAccount(String account, Pageable pageable);
}
