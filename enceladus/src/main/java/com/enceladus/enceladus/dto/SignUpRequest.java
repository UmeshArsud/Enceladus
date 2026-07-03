package com.enceladus.enceladus.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor //or use simply @data but data leak can be possible
public class SignUpRequest {

    @NotBlank(message = "First Name IS Required")
    @Size(min = 3, max = 12, message = "First Name Must Contain Character Between 3 to 12")
    @Pattern(regexp = "^[A-Za-z]+(?: [A-Za-z]+)*$", message = "Name can contain only letters and single spaces"
    )
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 12, message = "Last Name Must Contain Character Between 3 to 12")
    @Pattern(regexp = "^[A-Za-z]+(?: [A-Za-z]+)*$", message = "lastname can contain only letters and single spaces")
    private String lastName;

    @NotBlank
    @Size(min = 3, max = 12, message = "Username Must Contain Character Between 3 to 12")
    @Pattern(regexp = "^[a-zA-Z0-9_.-]+$", message = "Username can only contain letters, numbers, dots, dashes, and underscores")
    private String username;

    @NotBlank(message = "Email Is Required")
    @Email(message = "Email Must Be A Valid e-mail Address")
    @Size(max = 60, message = "Email cannot exceed 100 characters")
    private String email;

    @NotBlank(message = "Mobile Number Is Required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid mobile number format")
    private String mobile;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]:;\"'<>,.?/\\\\|`~])\\S+$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, one special character, and no spaces"
    )
    @ToString.Exclude // to not include password in toString method
    private String password;
}
