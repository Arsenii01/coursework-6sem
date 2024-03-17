package ru.musailov.library.models;


import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthenticationResponse {
    private String token;
    private String message;

    private Role role;

    public AuthenticationResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }

}