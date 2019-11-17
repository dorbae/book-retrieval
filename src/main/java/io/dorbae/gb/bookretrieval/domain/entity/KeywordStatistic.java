package io.dorbae.gb.bookretrieval.domain.entity;

import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

/*
 *****************************************************************
 *
 * KeywordStatisticEntity
 *
 * @description KeywordStatisticEntity
 *
 *
 *****************************************************************
 *
 * @version 1.0.0    2019/11/14 1:11     dorbae	최초 생성
 * @since 2019/11/14 1:11
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
@Table(name = "keyword_statistic")
public class KeywordStatistic extends BaseTime {

//    @Id
//    @GeneratedValue
//    private Long id;
    @Id
    private String keyword;
    private long count;

//    public void setId(long id) {
//        this.id = id;
//    }

    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        KeywordStatistic that = (KeywordStatistic) o;
        return keyword.equals(that.keyword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyword);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
//                .append("id", id)
                .append("keyword", keyword)
                .append("count", count)
                .toString();
    }
}
