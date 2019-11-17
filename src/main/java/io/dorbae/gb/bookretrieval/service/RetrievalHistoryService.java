package io.dorbae.gb.bookretrieval.service;

import io.dorbae.gb.bookretrieval.domain.entity.Member;
import io.dorbae.gb.bookretrieval.domain.entity.RetrievalHistory;
import io.dorbae.gb.bookretrieval.domain.repository.RetrievalHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/*
 *****************************************************************
 *
 * RetrievalService
 *
 * @description RetrievalService
 *
 *
 *****************************************************************
 *
 * @version 1.0.0    2019/11/12 18:46     dorbae	최초 생성
 * @since 2019/11/12 18:46
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
@Service
public class RetrievalHistoryService {

    @Autowired
    private RetrievalHistoryRepository retrievalHistoryRepository;

    @Transactional
    public void save(RetrievalHistory entity) {
        this.retrievalHistoryRepository.save(entity);
    }

    /**
     * 유저가 검색한 검색 이력 가져오기
     * 최신 순으로 최대 10개로 제한
     * <p>
     * // TODO: 과제 4. 내 검색 히스토리
     *
     * @param member
     * @return
     */
    public List<RetrievalHistory> findByMember(Member member) {
        // 생성일시 기준으로 역순. 10개로 제한
        return this.retrievalHistoryRepository.findByAccount(member.getAccount(), PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "CREATED_DATE"))).getContent();
    }
}
