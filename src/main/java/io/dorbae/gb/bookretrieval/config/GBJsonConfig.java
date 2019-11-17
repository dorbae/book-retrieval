package io.dorbae.gb.bookretrieval.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 *****************************************************************
 *
 * GBJsonConfig
 *
 * @description JSON 파서
 *
 *
 *****************************************************************
 *
 * @version 1.0.0    2019/11/17 10:20     dorbae	최초 생성
 * @since 2019/11/17 10:20
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
@Configuration
public class GBJsonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);
        return mapper;
    }
}
