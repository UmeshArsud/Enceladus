package com.enceladus.enceladus.dto;

import lombok.*;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SignInResponce {

    @ToString.Exclude
    private final String token;
    private final String type;
    private final Long id;
    private final String username;
    private final String email;

    public SignInResponce(String token, String type, Long id, String username, String email) {
        this.token = token;
        this.type = type;
        this.id = id;
        this.username = username;
        this.email = email;
    }
}