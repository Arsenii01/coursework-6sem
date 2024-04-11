package ru.musailov.library.repositories;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.musailov.library.models.Reader;

import java.util.Optional;

public interface ReaderRepository extends JpaRepository<Reader, Integer> {
    Optional<Reader> findByFullName(String name);

    Optional<Reader> findByEmail(String email);
}
