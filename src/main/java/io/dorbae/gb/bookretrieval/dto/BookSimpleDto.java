package io.dorbae.gb.bookretrieval.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/*
 *****************************************************************
 *
 * BookSimpleDto
 *
 * @description BookSimpleDto
 *
 *
 *****************************************************************
 *
 * @version 1.0.0    2019/11/15 18:13     dorbae	최초 생성
 * @since 2019/11/15 18:13
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class BookSimpleDto {
    private String title;
    private String authors;
    private String publisher;
    private int salesPrice;
    private String thumbnail;
    private String isbn;

    @Override
    public String toString() {
        return "BookSimpleDto{" +
                "title='" + title + '\'' +
                ", authors='" + authors + '\'' +
                ", publisher='" + publisher + '\'' +
                ", salesPrice=" + salesPrice +
                ", thumbnail='" + thumbnail + '\'' +
                ", isbn='" + isbn + '\'' +
                '}';
    }
}
