package ru.musailov.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.musailov.library.models.Book;

import java.util.List;

public interface BooksRepository extends JpaRepository<Book, Integer> {
    List<Book> findByNameStartingWithIgnoreCase(String title);
}

