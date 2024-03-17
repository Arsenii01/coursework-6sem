package ru.musailov.library.dto;

import lombok.Data;
import ru.musailov.library.models.Book;
import ru.musailov.library.models.Reader;

import java.util.List;


@Data
public class BookPageInfo {
    private Book book;
    private ReaderInfo owner;

    private List<ReaderInfo> readers;
}
