package io.dorbae.gb.bookretrieval.domain.entity;

import lombok.*;

import javax.persistence.*;

/*
 *****************************************************************
 *
 * RetrievalHistory
 *
 * @description RetrievalHistory
 *
 *
 *****************************************************************
 *
 * @version 1.0.0    2019/11/12 17:59     dorbae	최초 생성
 * @since 2019/11/12 17:59
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
@Table(name = "retrieval_history")
public class RetrievalHistory extends BaseTime {

    @Id
    @GeneratedValue
    private Long id;
    private String keyword;

    @ManyToOne(optional = false)
    @JoinColumn(name = "account",
            referencedColumnName = "account")
    private Member member;
}
