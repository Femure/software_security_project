package ku.project.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.Date;

@Data
public class SignupResponse {

        private String firstName;
        private String lastName;
        private String username;
        private String email;
        private String role;
        private String password;
        private String confirmPassword;

        private String verificationCode;
        private boolean enabled;

        private boolean accountNonLocked;
        private int failedAttempt;
        private Date lockTime;
}
