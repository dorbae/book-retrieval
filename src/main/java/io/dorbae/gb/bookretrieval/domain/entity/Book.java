package io.dorbae.gb.bookretrieval.domain.entity;

import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.List;
import java.util.Objects;

/*
 *****************************************************************
 *
 * Book
 *
 * @description 책 정보
 *
 *
 *****************************************************************
 *
 * @version 1.0.1    2019/11/11 22:21     dorbae	author -> authors (공동 저자 적용)
 * @version 1.0.0    2019/11/11 21:47     dorbae	최초 생성
 * @since 2019/11/11 21:47
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
public class Book {
    @Id
    private long id;
    private String isbn;        // 공백이나 마지막에 문자도 들어감
    private String title;
    private String thumbnail;   // ThumbnailURL
    private String introduction;
    @Transient
    private List<String> authors;
    private String publisher;
    private int price;         // 정가
    private int salePrice;     // 판매가

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        io.dorbae.gb.bookretrieval.domain.entity.Book book = (io.dorbae.gb.bookretrieval.domain.entity.Book) o;
        return isbn.equals(book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("isbn", isbn)
                .append("title", title)
                .append("thumbnail", thumbnail)
                .append("introduction", introduction)
                .append("author", authors.toArray(new String[0]))
                .append("publisher", publisher)
                .append("price", price)
                .append("salePrice", salePrice)
                .toString();
    }
}
