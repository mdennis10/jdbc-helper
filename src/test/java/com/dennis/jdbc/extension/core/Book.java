package com.dennis.jdbc.extension.core;

import com.dennis.jdbc.extension.core.annotation.Column;
import lombok.Data;


@Data
public class Book {
    @Column(name = "Author")
    private String author;
}
