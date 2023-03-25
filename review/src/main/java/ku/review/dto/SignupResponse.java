package ku.review.dto;

import lombok.*;

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
