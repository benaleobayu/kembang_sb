package com.bca.byc.model;

import com.bca.byc.validation.UniqueEmail;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class RegisterRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @NotBlank(message = "Name is mandatory")
    @Size(max = 50, message = "Name must be less than 50 characters")
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Size(max = 50, message = "Email must be less than 50 characters")
    @UniqueEmail // Custom annotation for unique email validation
    private String email;

    @NotBlank(message = "Phone is mandatory")
    @Size(max = 16, message = "Phone number must be less than 16 characters")
    private String phone;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;



    // Getters and Setters

}
