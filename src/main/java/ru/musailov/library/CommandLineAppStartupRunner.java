package ru.musailov.library;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.musailov.library.models.Role;
import ru.musailov.library.models.User;
import ru.musailov.library.repositories.UserRepository;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

    private final  UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CommandLineAppStartupRunner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String...args) throws Exception {
        if (userRepository.findByUsername("arsenii01").isEmpty()) {
            User superAdmin = new User("Arseniy", "Musailov", "arsenii01", passwordEncoder.encode("9312"));
            superAdmin.setRole(Role.SUPERADMIN);
            userRepository.save(superAdmin);
        }

    }
}