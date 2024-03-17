package ru.musailov.library.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.musailov.library.models.Book;
import ru.musailov.library.models.Reader;
import ru.musailov.library.repositories.ReaderRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
public class ReaderService {
    private final ReaderRepository readerRepository;


    @Autowired
    public ReaderService(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }

    public List<Reader> findAll() {
        return readerRepository.findAll();
    }

    public Reader findById(int id) {
        Optional<Reader> reader = readerRepository.findById(id);
        return reader.orElse(null);

    }

    public List<Book> getBooksByPersonId(int id) {
        Optional<Reader> reader = readerRepository.findById(id);
        if (reader.isPresent()) {
            Hibernate.initialize(reader.get().getBooks());

            reader.get().getBooks().forEach(book -> {
                if (book.getTakenAt() == null) {
                    book.setExpired(false);
                } else book.setExpired(TimeUnit.MILLISECONDS.
                        toDays((new Date().getTime()) - book.getTakenAt().getTime()) > 10);
            });
            return reader.get().getBooks();
        } else {
            return Collections.emptyList();
        }
    }

    @Transactional
    public void save(Reader reader) {
        readerRepository.save(reader);
    }

    @Transactional
    public void delete(int id) {
        Optional<Reader> reader = readerRepository.findById(id);
        reader.ifPresent(value -> value.getBooks().forEach(book -> book.setTakenAt(null)));
        readerRepository.deleteById(id);
    }

    @Transactional
    public void update(int id, Reader reader) {
        reader.setId(id);
        readerRepository.save(reader);
    }
}
