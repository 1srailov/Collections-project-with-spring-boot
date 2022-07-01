package com.itransition.final_task.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class SignupRequest{
        @NotBlank
        @NotNull
        @Size(min = 3, max = 20)
        private String username;

        @NotBlank
        @Size(max = 50)
        @NotNull
        @Email
        private String email;

        @NotBlank
        @NotNull
        @Size(min = 6, max = 40)
        private String password;

        @NotBlank
        @NotNull
        private String role;
}
