package com.bitsplease.skillslist_server.restservice.controller;

import com.bitsplease.skillslist_server.SkillslistServerApplication;
import com.bitsplease.skillslist_server.data.User;
import com.bitsplease.skillslist_server.restservice.Login;
import com.google.gson.Gson;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class LoginController {

    private static final String template = "Content: %s";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/login")
    public Login login(@RequestParam(value = "login") String login, @RequestParam(value = "password") String password) {
        User user = SkillslistServerApplication.db.connectUser(login, password);
        if(user != null){
            Gson gson = new Gson();
            String userJson = gson.toJson(user);
            return new Login(counter.incrementAndGet(), String.format(template, userJson));
        } else {
            return new Login(counter.incrementAndGet(), String.format(template, ""));
        }
    }
}