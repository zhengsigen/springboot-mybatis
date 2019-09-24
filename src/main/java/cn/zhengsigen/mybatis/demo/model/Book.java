package cn.zhengsigen.mybatis.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Book {
    private Integer id;
    private String ISBN10;
    private String ISBN13;
    private String name;
    private String cover;
    private String author;
    private double price;
    private String publisher;
    private Date pubDate;
    private double doubanRate;
    private String gist;
    private String binding;
    private Date createDate;
    private Date updateDate;

    public Book() {

    }
}
