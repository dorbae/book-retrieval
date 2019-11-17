package io.dorbae.gb.bookretrieval.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/*
 *****************************************************************
 *
 * GBPersistentConfig
 *
 * @description GBPersistentConfig
 *
 *
 *****************************************************************
 *
 * @version 1.0.0    2019/11/15 4:09     dorbae	최초 생성
 * @since 2019/11/15 4:09
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
@Configuration
// CreatedDate, ModifiedDate 적용
@EnableJpaAuditing
public class GBPersistentConfig {
}