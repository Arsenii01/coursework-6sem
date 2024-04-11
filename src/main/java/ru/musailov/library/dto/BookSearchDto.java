package ru.musailov.library.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookSearchDto {
    private Integer id;
    private String name;
    private String author;

    private int year;
}
