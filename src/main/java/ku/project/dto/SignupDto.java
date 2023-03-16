package ku.project.dto;

import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import ku.project.validation.PasswordValueMatch;
import ku.project.validation.ValidPassword;

@PasswordValueMatch.List({
        @PasswordValueMatch(
                field = "password",
                fieldMatch = "confirmPassword",
                message = "Passwords do not match!"
        )
})

@Data
public class SignupDto {

        @Column(name = "verification_code", length = 64)
        private String verificationCode;
         
        private boolean enabled;
     
        
        // @NotBlank(message = "First name is required")
        // @Pattern(regexp = "^[a-zA-Z]+$", message = "First name can only contain letters")
        private String firstName;

        // @NotBlank(message = "Last name is required")
        private String lastName;

        // @NotBlank(message = "Username is required")
        // @Size(min = 4, message = "Username must have at least 4 characters")
        private String username;

        @Email
        @NotBlank(message = "Email is required")
        private String email;

        // @NotBlank(message = "Password is required")
        //@ValidPassword
        private String password;

        //@NotBlank(message = "Confirm Password is required")
        private String confirmPassword;
}
