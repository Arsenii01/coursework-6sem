package ru.musailov.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.musailov.library.models.Book;
import ru.musailov.library.models.User;
import ru.musailov.library.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

//    public Optional<User> getById(int id) {
//        return userRepository.findById(id);
//    }

    @Transactional
    public void delete(int id) {
        userRepository.deleteById(id);
    }

//    @Transactional
//    public void update(int id, User userToUpdate) {
//        userToUpdate.setId(id);
//        userRepository.save(userToUpdate);
//    }
}
