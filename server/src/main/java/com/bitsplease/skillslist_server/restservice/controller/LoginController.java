package com.bitsplease.skillslist_server.restservice.controller;

import com.bitsplease.skillslist_server.SkillslistServerApplication;
import com.bitsplease.skillslist_server.data.User;
import com.bitsplease.skillslist_server.restservice.Login;
import com.google.gson.Gson;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class LoginController {

    private final AtomicLong counter = new AtomicLong();

    @PostMapping(path = "/login", consumes = "application/json", produces = "application/json")
    public Login register(
        @RequestBody User user) 
    {
        System.out.println("Hey yooooo: " + user.toString());
        User connectedUser = SkillslistServerApplication.db.connectUser(user.getUsername(), user.getPassword());
        if(connectedUser != null){
            Gson gson = new Gson();
            String userJson = gson.toJson(connectedUser);
            return new Login(counter.incrementAndGet(), userJson);
        } else {
            return new Login(counter.incrementAndGet(), "");
        }
    }
}