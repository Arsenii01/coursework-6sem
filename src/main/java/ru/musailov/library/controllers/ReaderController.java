package ru.musailov.library.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.musailov.library.dto.ResponseDto;
import ru.musailov.library.dto.ReaderDTO;
import ru.musailov.library.exceptions.NotFoundException;
import ru.musailov.library.models.Reader;
import ru.musailov.library.services.ReaderService;
import ru.musailov.library.util.ReaderValidator;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/readers")
public class ReaderController {


    private final ReaderService readerService;
    private final ReaderValidator readerValidator;

    @Autowired
    public ReaderController(ReaderService readerService, ReaderValidator readerValidator, ReaderValidator readerValidator1) {
        this.readerService = readerService;
        this.readerValidator = readerValidator1;
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
    public ResponseEntity<?> create(@RequestBody ReaderDTO readerDTO,
                                    BindingResult result) {
        Reader reader = convertToReader(readerDTO);
        readerValidator.validate(reader, result);
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(new ResponseDto("Данный email уже занят другим читателем."));
        }
        readerService.save(reader);
        return ResponseEntity.ok(new ResponseDto("Пользователь успешно добавлен"));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody ReaderDTO updatedReader,
                         BindingResult result,
                         @PathVariable("id") int id) {
        Reader reader = convertToReader(updatedReader);
        reader.setId(id);
        readerValidator.validate(reader, result);
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(new ResponseDto("Данный email уже занят другим читателем."));
        }
        readerService.update(id, reader);
        return ResponseEntity.ok(new ResponseDto("Пользователь успешно изменён"));
    }

    @DeleteMapping("/{id}")
    public HttpStatus delete(@PathVariable("id") int id) {
        readerService.delete(id);
        return HttpStatus.OK;
    }

    private Reader convertToReader(ReaderDTO readerDTO) {
        return new Reader(readerDTO.getFullName(), readerDTO.getBirthYear(), readerDTO.getEmail());
    }
}
