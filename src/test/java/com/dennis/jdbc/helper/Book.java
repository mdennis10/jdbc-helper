package com.dennis.jdbc.helper;

import com.dennis.jdbc.helper.annotation.Column;
import lombok.Data;


@Data
public class Book {
    @Column(name = "Author")
    private String author;
}
