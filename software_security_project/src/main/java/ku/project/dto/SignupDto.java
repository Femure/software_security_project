package ku.project.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.Date;

import ku.project.validation.PasswordValueMatch;
import ku.project.validation.ValidPassword;

@PasswordValueMatch.List({
                @PasswordValueMatch(field = "password", fieldMatch = "confirmPassword", message = "Passwords do not match!")
})

@Data
public class SignupDto {

        // @NotBlank(message = "First name is required")
        // @Size(max = 100)
        // @Pattern(regexp = "^[a-zA-Z]+$", message = "First name can only contain letters")
        private String firstName;

        // @NotBlank(message = "Last name is required")
        // @Size(max = 100)
        private String lastName;

        @NotBlank(message = "Username is required")
        @Size(min = 4, max = 30, message = "Username must have at least 4 characters and at most 30")
        private String username;

        @Email
        @Size(max = 100)
        @NotBlank(message = "Email is required")
        private String email;

        @NotBlank
        @Pattern(regexp = "^(ROLE_ADMIN|ROLE_USER)$", message = "Role is in an incorrect format.")
        private String role;

        // @NotBlank(message = "Password is required")
        // @Size(max = 128)
        // @ValidPassword
        private String password;

        // @NotBlank(message = "Confirm Password is required")
        // @Size(max = 128)
        private String confirmPassword;

        private boolean enabled;

        private boolean accountNonLocked;
        private int failedAttempt;
        private Date lockTime;
}
