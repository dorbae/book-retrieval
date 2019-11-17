package io.dorbae.gb.bookretrieval.domain.repository;

import io.dorbae.gb.bookretrieval.domain.entity.KeywordStatistic;
import io.dorbae.gb.bookretrieval.domain.entity.Member;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/*
 *****************************************************************
 *
 * KeywordStatisticRepositoryTest
 *
 * @description KeywordStatisticRepositoryTest
 *
 *
 *****************************************************************
 *
 * @version 1.0.0    2019/11/17 6:31     dorbae	최초 생성
 * @since 2019/11/17 6:31
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class KeywordStatisticRepositoryTest {
    @Autowired
    private io.dorbae.gb.bookretrieval.domain.repository.KeywordStatisticRepository keywordStatisticRepository;
    private Member member;

    @Before
    public void setUp() {
    }

    @Test
    public void testTopRankedKeywordRepository() {
        // 데이터 적재
        KeywordStatistic keywordStatistic = KeywordStatistic.builder().keyword("java").count(953).build();
        this.keywordStatisticRepository.save(keywordStatistic);

        keywordStatistic = KeywordStatistic.builder().keyword("c").count(230).build();
        this.keywordStatisticRepository.save(keywordStatistic);

        keywordStatistic = KeywordStatistic.builder().keyword("python").count(130).build();
        this.keywordStatisticRepository.save(keywordStatistic);

        // 전체 행 개수 확인
        assertEquals(3, this.keywordStatisticRepository.findAll().size());

        // 특정 키워드 확인
        assertNotNull(this.keywordStatisticRepository.getOne("java"));
    }

    /**
     * 데이터 정리
     */
    @After
    public void cleanup() {
        this.keywordStatisticRepository.deleteAll();
    }
}