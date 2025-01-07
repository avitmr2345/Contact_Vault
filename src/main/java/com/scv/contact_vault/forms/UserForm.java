package com.scv.contact_vault.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserForm {

    @NotBlank(message = "Username is required")
    @Size(min = 3, message = "Minimum three characters are required")
    private String name;

    @Email(message = "Invalid Email Address")
    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$", message = "Email must be in a valid format (e.g. xyz@gmail.com)")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Minimum six characters are required")
    private String password;

    @NotBlank(message = "About is required")
    private String about;

    @Size(min = 10, max = 10, message = "Invalid Phone Number")
    private String phoneNumber;

}