package ru.musailov.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.musailov.library.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);
}