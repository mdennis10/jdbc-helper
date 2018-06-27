package com.dennis.jdbc.helper;

import com.dennis.jdbc.helper.annotation.Column;
import lombok.Data;

import java.sql.Date;


@Data
public class Book {
    @Column(name = "Author")
    private String author;

    @Column(name = "Created")
    private Date created;
}
