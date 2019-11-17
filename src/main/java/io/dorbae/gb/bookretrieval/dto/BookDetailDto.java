package io.dorbae.gb.bookretrieval.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/*
 *****************************************************************
 *
 * BookDetailDto
 *
 * @description BookDetailDto
 *
 *
 *****************************************************************
 *
 * @version 1.1.0    2019/11/16 20:49     dorbae	출판일 필드 추가
 * @version 1.0.0    2019/11/16 15:50     dorbae	최초 생성
 * @since 2019/11/16 15:50
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class BookDetailDto {
    private String title;
    private String authors;
    private String publisher;
    private int salesPrice;
    private String thumbnail;
    private String isbn;
    private String contents;
    private int price;
    private LocalDateTime datetime;

    @Override
    public String toString() {
        return "BookDetailDto{" +
                "title='" + title + '\'' +
                ", authors='" + authors + '\'' +
                ", publisher='" + publisher + '\'' +
                ", salesPrice=" + salesPrice +
                ", thumbnail='" + thumbnail + '\'' +
                ", isbn='" + isbn + '\'' +
                ", contents='" + contents + '\'' +
                ", price=" + price +
                ", datetime=" + datetime +
                '}';
    }
}
