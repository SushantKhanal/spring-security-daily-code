package com.example.springsecurityclient.controller;

import com.example.springsecurityclient.entity.User;
import com.example.springsecurityclient.event.RegistrationCompleteEvent;
import com.example.springsecurityclient.model.UserModel;
import com.example.springsecurityclient.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegistrationController {
    @Autowired
    private UserService userService;
    @Autowired
    private ApplicationEventPublisher publisher;
    @PostMapping("/register")
    public String registerNewUser(@RequestBody UserModel userModel, final HttpServletRequest request) {
        User user = userService.registerUser(userModel);
        publisher.publishEvent(new RegistrationCompleteEvent(
                user,
                applicationUrl(request)
        ));
        return "Success";
    }

    @GetMapping("/verifyRegistration")
    public ResponseEntity<String> verifyRegistration(@RequestParam("token") String token) {
        String result = userService.validateVerificationToken(token);
        if(result.equalsIgnoreCase("valid")) {
            return ResponseEntity.ok("User Verified Successfully");
        }
        return ResponseEntity.status(400).body("Bad Request");
    }

    @GetMapping("/hello")
    public String helloUser() {
        return "Hello New User";
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
