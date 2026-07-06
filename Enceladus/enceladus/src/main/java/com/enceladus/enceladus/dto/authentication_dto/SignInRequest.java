package com.enceladus.enceladus.dto.authentication_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor // or simply use @data but possibly data leak
public class SignInRequest {

    @NotBlank
    @Size(min = 3, max = 12, message = "Username Must Contain Character Between 3 to 12")
    private String username;

    @NotBlank
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
    @ToString.Exclude
    private String password;
}
