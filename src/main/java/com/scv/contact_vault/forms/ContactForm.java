package com.scv.contact_vault.forms;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ContactForm {

    @NotBlank(message = "Username is required")
    @Size(min = 3, message = "Minimum three characters are required")
    private String name;

    @Email(message = "Invalid Email Address")
    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$", message = "Email must be in a valid format (e.g. xyz@gmail.com)")
    private String email;

    @NotBlank(message = "Phone Number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone Number must contain 10 numbers and contain only digits")
    private String phoneNumber;

    @NotBlank(message = "Address is required")
    private String address;

    private String description;

    private boolean favourite;

    private String instagramUsername;

    private String linkedInLink;

    private MultipartFile profileImage;
}