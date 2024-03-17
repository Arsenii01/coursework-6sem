package ru.musailov.library.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.musailov.library.models.Reader;
import ru.musailov.library.repositories.ReaderRepository;

import java.util.Optional;

@Component
public class ReaderValidator implements Validator {
    private final ReaderRepository readerRepository;

    @Autowired
    public ReaderValidator(ReaderRepository peopleRepository) {
        this.readerRepository = peopleRepository;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return Reader.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Reader person = (Reader) target;
        Optional<Reader> foundPerson = readerRepository.findByFullName(person.getFullName());
        if (foundPerson.isPresent() && person.getId() != foundPerson.get().getId()) {
            errors.rejectValue("fullName", "", "Человек с таким ФИО уже зарегистрирован");
        }

    }
}
