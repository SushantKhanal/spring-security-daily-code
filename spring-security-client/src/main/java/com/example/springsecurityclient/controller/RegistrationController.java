package com.example.springsecurityclient.controller;

import com.example.springsecurityclient.entity.User;
import com.example.springsecurityclient.entity.VerificationToken;
import com.example.springsecurityclient.event.RegistrationCompleteEvent;
import com.example.springsecurityclient.model.PasswordModel;
import com.example.springsecurityclient.model.UserModel;
import com.example.springsecurityclient.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
public class RegistrationController {
    @Autowired
    private UserService userService;
    @Autowired
    private ApplicationEventPublisher publisher;
    @PostMapping("/register")
    public ResponseEntity<String> registerNewUser(@RequestBody UserModel userModel, final HttpServletRequest request) {
        User user = userService.registerUser(userModel);
        publisher.publishEvent(new RegistrationCompleteEvent(
                user,
                applicationUrl(request)
        ));
        return ResponseEntity.status(HttpStatus.CREATED).body("New User Created.");
    }
    @GetMapping("/verifyRegistration")
    public ResponseEntity<String> verifyRegistration(@RequestParam("token") String token) {
        String result = userService.validateVerificationToken(token);
        if(result.equalsIgnoreCase("valid")) {
            return ResponseEntity.ok("User Verified Successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request");
    }
    @GetMapping("/resendVerifyToken")
    public ResponseEntity<String> resendVerificationToken(@RequestParam("token") String oldToken,
                                                          HttpServletRequest request) {
        VerificationToken verificationToken = userService.generateNewVerificationToken(oldToken);
        User user = verificationToken.getUser();
        resendVerificationTokenMail(user, applicationUrl(request), verificationToken);
        return ResponseEntity.status(HttpStatus.OK).body("Verification Link Sent");
    }
    @GetMapping("/hello")
    public String helloUser() {
        return "Hello New User";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody PasswordModel passwordModel,
                                HttpServletRequest request) {
        User user = userService.findUserByEmail(passwordModel.getEmail());
        String url = "";
        if(user != null) {
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, token);
            url = sendPasswordResetTokenMail(user, applicationUrl(request), token);
        }
        return url;
    }

    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token,
                               @RequestBody PasswordModel passwordModel) {

    }

    private String sendPasswordResetTokenMail(User user, String applicationUrl, String token) {
        String url = applicationUrl + "/savePassword?token=" + token;
        //Send Verification Email
        log.info("click the link to rese your password:  {}", url);
    }

    private void resendVerificationTokenMail(User user, String applicationUrl, VerificationToken verificationToken) {
        String url = applicationUrl + "/verifyRegistration?token=" + verificationToken.getToken();
        //Send Verification Email
        log.info("click the link to verify your account:  {}", url);
    }

    private String applicationUrl(HttpServletRequest request) {
        //constructs and returns a String representing the base URL of the application
        return "http://" + request.getServerName() + ":" + request.getServerPort() +
                request.getContextPath();
        //This gets the context path of the application. The context path is the portion of the URL that indicates the
        // base path of the application. If your application is hosted at the root context, this will be an empty string.
        // If it’s not, it will be something like /myapp.
    }
}
