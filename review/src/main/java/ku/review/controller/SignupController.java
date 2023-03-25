package ku.review.controller;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import ku.review.dto.SignupRequest;
import ku.review.dto.SignupResponse;
import ku.review.service.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/signup")
public class SignupController {

    @Autowired
    private SignupService service;


    @GetMapping("/{username}")
    public SignupRequest isUsernameAvailable(@PathVariable("username") String username) {
        return service.getMember(username);
    }



    @PostMapping
    public ResponseEntity create(@Valid @RequestBody SignupRequest signup,
            BindingResult result) throws UnsupportedEncodingException, MessagingException {

        if (result.hasErrors())
            return new ResponseEntity<String>("Invalid request format", HttpStatus.UNPROCESSABLE_ENTITY);

        service.createMember(signup);
        return new ResponseEntity<SignupRequest>(signup, HttpStatus.OK);
    }
}
