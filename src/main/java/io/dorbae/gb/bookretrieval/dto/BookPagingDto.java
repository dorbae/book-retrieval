package io.dorbae.gb.bookretrieval.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/*
 *****************************************************************
 *
 * BookPagingDto
 *
 * @description BookPagingDto
 *
 *
 *****************************************************************
 *
 * @version 1.0.0    2019/11/15 18:43     dorbae	최초 생성
 * @since 2019/11/15 18:43
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class BookPagingDto {
    private int pageableCount;
    private int totalCount;
    private List<BookSimpleDto> books;

    @Override
    public String toString() {
        return "BookPagingDto{" +
                "pageableCount=" + pageableCount +
                ", totalCount=" + totalCount +
                ", books=" + books +
                '}';
    }
}
