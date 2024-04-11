package ru.musailov.library.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.musailov.library.dto.UserDTO;
import ru.musailov.library.exceptions.NotFoundException;
import ru.musailov.library.models.Reader;
import ru.musailov.library.models.Role;
import ru.musailov.library.models.User;
import ru.musailov.library.services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<UserDTO> getAllUsers() {
        return convertToUserDto(userService.getAll());
    }


    @DeleteMapping("/{id}")
    public HttpStatus delete(@PathVariable("id") int id) {
        userService.delete(id);
        return HttpStatus.OK;
    }

    private List<UserDTO> convertToUserDto(List<User> users) {
        List<UserDTO> userDTOS = new ArrayList<>();
        users.forEach( e -> {
            if (!e.getRole().equals(Role.SUPERADMIN)) userDTOS.add(new UserDTO(e.getId(), e.getFirstName(), e.getLastName(), e.getUsername(), e.getRole()));
        }
        );
        return userDTOS;
    }
}
