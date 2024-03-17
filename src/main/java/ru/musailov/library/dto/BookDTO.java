package ru.musailov.library.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class BookDTO {
    @NotEmpty(message = "Название книги не должно быть пустым")
    @Column(name = "name")
    private String name;


    @NotEmpty(message = "Author name should not be empty")
    @Column(name = "author")
    private String author;

    @Min(value = 1000, message = "Дата написания книги должна быть больше 1000 года")
    @Column(name = "year")
    private Integer year;
}
