package io.dorbae.gb.bookretrieval.domain.repository;

import java.util.Optional;

import io.dorbae.gb.bookretrieval.domain.entity.KeywordStatistic;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 *****************************************************************
 *
 * KeywordStatisticRepository
 *
 * @description KeywordStatisticRepository
 *
 *
 *****************************************************************
 *
 * @version 1.0.0    2019/11/14 1:14     dorbae	최초 생성
 * @since 2019/11/14 1:14
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
public interface KeywordStatisticRepository extends JpaRepository<KeywordStatistic, String> {
}
