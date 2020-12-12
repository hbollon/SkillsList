package com.bitsplease.skillslist_server.restservice.controller;

import com.bitsplease.skillslist_server.SkillslistServerApplication;
import com.bitsplease.skillslist_server.data.Role;
import com.bitsplease.skillslist_server.data.User;
import com.bitsplease.skillslist_server.restservice.Login;
import com.bitsplease.skillslist_server.restservice.Response;
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
        User connectedUser = SkillslistServerApplication.db.connectUser(user.getUsername(), user.getPassword());
        if(connectedUser != null){
            Gson gson = new Gson();
            String userJson = gson.toJson(connectedUser);
            return new Login(counter.incrementAndGet(), userJson);
        } else {
            return new Login(counter.incrementAndGet(), "");
        }
    }

    @PostMapping(path = "/getAllUserWithRole", consumes = "application/json", produces = "application/json")
    public Response getAllUserWithRole(
        @RequestBody Role role) 
    {
        User[] students = SkillslistServerApplication.db.getAllUserWithRole(role);
        if(students != null && students.length > 0) {
            Gson gson = new Gson();
            String contentJson = gson.toJson(students);
            return new Response(counter.incrementAndGet(), contentJson);
        } else {
            return new Response(counter.incrementAndGet(), "");
        }
    }
}