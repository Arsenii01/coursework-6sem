package ru.musailov.library.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.musailov.library.dto.*;
import ru.musailov.library.models.Book;
import ru.musailov.library.services.BookService;
import ru.musailov.library.services.ReaderService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
                        @RequestParam(name = "sort_by_name", required = false) boolean sortByName) {
        if (page == null || booksPerPage == null) {
            return bookService.findAll(sortByName);
        } else {
            return bookService.findWithPagination(page, booksPerPage, sortByName);
        }
    }

    @PostMapping()
    public ResponseEntity<?> createBook(@RequestBody @Valid BookDTO bookDTO,
                                     BindingResult bindingResult) {
        Book book = convertToBook(bookDTO);
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDto(bindingResult.getAllErrors().stream()
                            .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining())));
        }
        bookService.save(book);
        return ResponseEntity.ok(new ResponseDto("Книга успешно добавлена"));
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
    public ResponseEntity<?> update(@RequestBody @Valid BookDTO bookDTO,
                                    BindingResult bindingResult,
                                    @PathVariable("id") int id) {
        Book book = convertToBook(bookDTO);
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new ResponseDto(bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining())));
        }
        bookService.update(id, book);
        return ResponseEntity.ok(new ResponseDto("Книга обновлена"));
    }
    @DeleteMapping("/{id}")
    public HttpStatus delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return HttpStatus.OK;
    }

    @GetMapping("/search")
    public List<BookSearchDto> searchBook(@RequestParam("title") String title) {
        List<BookSearchDto> books = new ArrayList<>();
        bookService.findWithTitle(title).forEach(e -> books.add(convertToBookSearchDto(e)));
        return books;
    }


    private Book convertToBook(BookDTO bookDTO) {
        return new Book(bookDTO.getName(), bookDTO.getAuthor(), bookDTO.getYear());
    }

    private BookSearchDto convertToBookSearchDto(Book book) {
        return new BookSearchDto(book.getId(), book.getName(), book.getAuthor(), book.getYear());
    }

}
