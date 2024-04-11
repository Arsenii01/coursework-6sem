package ru.musailov.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.musailov.library.dto.BookPageInfo;
import ru.musailov.library.dto.ReaderInfo;
import ru.musailov.library.models.Book;
import ru.musailov.library.models.Reader;
import ru.musailov.library.repositories.BooksRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
public class BookService {


    private final BooksRepository booksRepository;
    private final ReaderService readerService;
    @Autowired
    public BookService(BooksRepository booksRepository, ReaderService readerService) {
        this.booksRepository = booksRepository;
        this.readerService = readerService;
    }

    public List<Book> findAll(boolean sortByName) {
        List<Book> books;
        if (sortByName) {
             books = booksRepository.findAll(Sort.by("name"));
        } else {
            books = booksRepository.findAll();
        }
        for (Book book : books) {
            if (book.getTakenAt() != null) {
                book.setExpired(TimeUnit.MILLISECONDS.
                        toDays((new Date().getTime()) - book.getTakenAt().getTime()) > 10);
            }

        }
        return books;
    }

    public List<Book> findWithPagination(int page, int booksPerPage, boolean sortByName) {
        if (sortByName)
            return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("name"))).getContent();
        else
            return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }

    public BookPageInfo findById(int id) {
        Optional<Book> bookOptional = booksRepository.findById(id);
        if (bookOptional.isPresent()) {

            Book book = bookOptional.get();

            BookPageInfo bookPageInfo = new BookPageInfo();
            bookPageInfo.setBook(book);

            Reader owner = book.getOwner();
            if (owner != null) {
                ReaderInfo readerInfo = new ReaderInfo();
                readerInfo.setId(owner.getId());
                readerInfo.setName(owner.getFullName());
                bookPageInfo.setOwner(readerInfo);
            } else {
                bookPageInfo.setReaders(convertToReaderInfo(readerService.findAll()));

            }


            return bookPageInfo;
        } else {
            return null;
        }
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }
    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }
    @Transactional
    public void update(int id, Book bookToUpdate) {
        Book updatedBook = booksRepository.findById(id).get();
        updatedBook.setName(bookToUpdate.getName());
        updatedBook.setAuthor(bookToUpdate.getAuthor());
        updatedBook.setYear(bookToUpdate.getYear());
        booksRepository.save(updatedBook);
    }
    @Transactional
    public void release(int id) {
        Optional<Book> book = booksRepository.findById(id);
        Book book1 = book.get();
        book1.setOwner(null);
        book1.setTakenAt(null);
    }
    @Transactional
    public void set(int id, Reader reader) {
        Optional<Book> book = booksRepository.findById(id);
        book.get().setOwner(reader);
        book.get().setTakenAt(new Date());
    }

    public List<Book> findWithTitle(String title) {
        return booksRepository.findByNameContainingIgnoreCase(title);
    }

    private List<ReaderInfo> convertToReaderInfo(List<Reader> readers) {
        List<ReaderInfo> readerInfos = new ArrayList<>();

        readers.forEach(r -> {
            ReaderInfo readerInfo = new ReaderInfo();
            readerInfo.setId(r.getId());
            readerInfo.setName(r.getFullName());
            readerInfo.setEmail(r.getEmail());
            readerInfos.add(readerInfo);
        });

        return readerInfos;
    }
}

