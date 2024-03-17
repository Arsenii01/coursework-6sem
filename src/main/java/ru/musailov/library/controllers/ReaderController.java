package ru.musailov.library.controllers;

import jakarta.validation.Valid;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.ReaderEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.musailov.library.exceptions.NotFoundException;
import ru.musailov.library.models.Book;
import ru.musailov.library.models.Reader;
import ru.musailov.library.services.ReaderService;
import ru.musailov.library.util.ReaderValidator;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/readers")
public class ReaderController {


    private final ReaderService readerService;

    @Autowired
    public ReaderController(ReaderService readerService, ReaderValidator readerValidator) {
        this.readerService = readerService;
    }


    @GetMapping()
    public List<Reader> getAllReaders() {
        return readerService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reader> show(@PathVariable("id") int id) throws NotFoundException {
        Optional<Reader> reader = Optional.ofNullable(readerService.findById(id));
        if (reader.isPresent()) {
            return ResponseEntity.ok(reader.get());
        } else {
            throw new NotFoundException("Читатель не найден");
        }

    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody Reader reader) {
        readerService.save(reader);
        return ResponseEntity.ok("Пользователь успешно добавлен");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Reader updatedReader,
                         @PathVariable("id") int id) {
        readerService.update(id, updatedReader);
        return ResponseEntity.ok("Пользователь успешно изменён");
    }

    @DeleteMapping("/{id}")
    public HttpStatus delete(@PathVariable("id") int id) {
        readerService.delete(id);
        return HttpStatus.OK;
    }
}
