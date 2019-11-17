package io.dorbae.gb.bookretrieval.service;

import io.dorbae.gb.bookretrieval.domain.entity.KeywordStatistic;
import io.dorbae.gb.bookretrieval.domain.repository.KeywordStatisticRepository;
import org.apache.groovy.util.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

/*
 *****************************************************************
 *
 * KeywordStatisticService
 *
 * @description KeywordStatisticService
 *
 *
 *****************************************************************
 *
 * @version 1.1.0    2019/11/17 04:31     dorbae	인기 검색어 카운팅 배타락 적용
 * @version 1.0.0    2019/11/14 01:18     dorbae	최초 생성
 * @since 2019/11/14 1:18
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
@Service
public class KeywordStatisticService {
    private static final Logger LOG = LoggerFactory.getLogger(KeywordStatisticService.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    KeywordStatisticRepository keywordStatisticRepository;

    /**
     * 검색 키워드 조회 건수 증가
     *
     * @param entity
     */
    @Transactional
    public void countUp(KeywordStatistic entity) {
        // 배타락 적용. 타임아웃 5초
        KeywordStatistic keywordStatistic = this.entityManager.find(KeywordStatistic.class, entity.getKeyword()
                , LockModeType.PESSIMISTIC_WRITE, Maps.of("javax.persistence.lock.timeout", 5000L));

        if (keywordStatistic != null) { // 기존 키워드 업데이트
            entity.setCreatedDate(keywordStatistic.getCreatedDate());
            entity.setCount(keywordStatistic.getCount() + 1);
        } else {                        // 새로운 키워드
            entity.setCount(1L);
            LOG.debug("A new keyword was detected.");
        }

        this.keywordStatisticRepository.save(entity);
    }

    /**
     * 상위 검색 키워드
     * <p>
     * // TODO: 과제 5. 인기 키워드 목록
     *
     * @param limit 키워드 개수
     * @return
     */
    @Transactional
    public List<KeywordStatistic> getTopRankedKeywords(int limit) {
        Page<KeywordStatistic> page = this.keywordStatisticRepository.findAll(PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "count")));
        return page.getContent();
    }
}
