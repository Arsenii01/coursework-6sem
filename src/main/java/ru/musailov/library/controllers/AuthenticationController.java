package ru.musailov.library.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.musailov.library.dto.LoginRequest;
import ru.musailov.library.dto.RegisterRequest;
import ru.musailov.library.models.AuthenticationResponse;
import ru.musailov.library.models.User;
import ru.musailov.library.services.AuthenticationService;

@RestController
public class AuthenticationController {

    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        AuthenticationResponse authenticationResponse = authService.register(convertToUser(request));
        if (authenticationResponse.getRole() == null) {
            return ResponseEntity.badRequest().body(authenticationResponse);
        }
        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/registerAdmin")
    public ResponseEntity<AuthenticationResponse> registerAdmin(@RequestBody RegisterRequest request) {
        AuthenticationResponse authenticationResponse = authService.registerAdmin(convertToUser(request));
        if (authenticationResponse.getRole() == null) {
            return ResponseEntity.badRequest().body(authenticationResponse);
        }
        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody LoginRequest loginRequest
    ) {
        return ResponseEntity.ok(authService.authenticate(loginRequest));
    }

    private User convertToUser(RegisterRequest authRequest) {
        return new User(authRequest.getFirstName(), authRequest.getLastName(),
                authRequest.getUsername(), authRequest.getPassword());
    }
}
