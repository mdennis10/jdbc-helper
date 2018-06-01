package com.dennis.jdbc.extension.core;

import com.dennis.jdbc.extension.core.annotation.Column;
import com.dennis.jdbc.extension.core.annotation.Table;
import lombok.Data;


@Data
@Table(name = "Book")
public class Book {
    @Column(name = "Author")
    private String author;
}
