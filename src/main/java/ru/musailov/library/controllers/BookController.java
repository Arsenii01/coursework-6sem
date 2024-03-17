package ru.musailov.library.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.musailov.library.dto.BookPageInfo;
import ru.musailov.library.dto.ReaderRequest;
import ru.musailov.library.models.Book;
import ru.musailov.library.services.BookService;
import ru.musailov.library.services.ReaderService;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    private final ReaderService readerService;

    @Autowired
    public BookController(BookService bookService, ReaderService peopleService) {
        this.bookService = bookService;
        this.readerService = peopleService;
    }

//    @GetMapping
//    public ResponseEntity<List<Book>> getAllBooks() {
//        return ResponseEntity.ok(bookService.findAll());
//    }

    @GetMapping()
    public List<Book> getAllBooks(
                        @RequestParam(name = "page", required = false) Integer page,
                        @RequestParam(name = "books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(name = "sort_by_year", required = false) boolean sortByYear) {
        if (page == null || booksPerPage == null) {
            return bookService.findAll(sortByYear);
        } else {
            return bookService.findWithPagination(page, booksPerPage, sortByYear);
        }
    }

    @PostMapping()
    public HttpStatus createBook(@RequestBody @Valid Book book,
                                     BindingResult bindingResult) {
        bookService.save(book);
        return HttpStatus.OK;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookPageInfo> getBookById(@PathVariable("id") int id) {
        BookPageInfo bookPageInfo = bookService.findById(id);
        if (bookPageInfo != null) {
            return ResponseEntity.ok(bookPageInfo);
        } else {
            return  ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/set")
    public ResponseEntity<?> set(@RequestBody ReaderRequest request,
                                 @PathVariable("id") int id) {
        bookService.set(id, readerService.findById(request.getId()));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/release")
    public ResponseEntity<?> release(@PathVariable("id") int id) {
        bookService.release(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Book book,
                                    @PathVariable("id") int id) {
        bookService.update(id, book);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{id}")
    public HttpStatus delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return HttpStatus.OK;
    }

    @GetMapping("/search")
    public List<Book> searchBook(@RequestParam("title") String title) {
        return bookService.findWithTitle(title);
    }

}
