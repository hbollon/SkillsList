package com.bitsplease.skillslist_server.restservice.controller;

import com.bitsplease.skillslist_server.SkillslistServerApplication;
import com.bitsplease.skillslist_server.data.User;
import com.bitsplease.skillslist_server.restservice.Register;
import com.google.gson.Gson;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class RegisterController {

    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/register")
    public Register register(
        @RequestParam(value = "login") String login, 
        @RequestParam(value = "password") String password,
        @RequestParam(value = "firstName") String firstName,
        @RequestParam(value = "lastName") String lastName) 
    {
        User user = new User(login, password, firstName, lastName);
        boolean success = SkillslistServerApplication.db.insertUser(user);
        if(success){
            Gson gson = new Gson();
            String userJson = gson.toJson(user);
            return new Register(counter.incrementAndGet(), userJson, success);
        } else {
            return new Register(counter.incrementAndGet(), "", success);
        }
    }
}