package com.enceladus.enceladus.dto;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SignInResponce {

    @ToString.Exclude
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;


}
