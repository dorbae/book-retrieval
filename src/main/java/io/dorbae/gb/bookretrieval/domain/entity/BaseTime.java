package io.dorbae.gb.bookretrieval.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/*
 *****************************************************************
 *
 * BaseTime
 *
 * @description 기본 생성일시, 변경일시 필드 추가
 *
 *
 *****************************************************************
 *
 * @version 1.0.0    2019/11/11 21:06     dorbae	최초 생성
 * @since 2019/11/11 21:06
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
@Getter
@Setter
// JPA Entity 클래스가 해당 클래스를 상속할 경우 createdDate, modifiedDate)도 컬럼으로 인식
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTime {
    // Entity가 생성되어 저장될 때 시간이 자동 저장
    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;
    // Entity의 값을 변경할 때 시간이 자동 저장
    @LastModifiedDate
    @Column(name = "last_modified_date", nullable = false)
    private LocalDateTime modifiedDate;
}
