package com.example.springsecurityclient.service;

import com.example.springsecurityclient.entity.User;
import com.example.springsecurityclient.model.UserModel;

public interface UserService {
    User registerUser(UserModel userModel);
    void saveTokenForUser(String token, User user);
    String validateVerificationToken(String token);
}
