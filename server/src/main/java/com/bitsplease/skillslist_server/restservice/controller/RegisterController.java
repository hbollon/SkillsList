package com.bitsplease.skillslist_server.restservice.controller;

import com.bitsplease.skillslist_server.SkillslistServerApplication;
import com.bitsplease.skillslist_server.data.Role;
import com.bitsplease.skillslist_server.data.User;
import com.bitsplease.skillslist_server.restservice.Register;
import com.bitsplease.skillslist_server.restservice.Response;
import com.google.gson.Gson;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class RegisterController {

    private final AtomicLong counter = new AtomicLong();

    @PostMapping(path = "/register", consumes = "application/json", produces = "application/json")
    public Register register(
        @RequestBody User user) 
    {
        System.out.println("Hey yooooo: " + user.toString());
        boolean success = SkillslistServerApplication.db.insertUser(user);
        if(success){
            Gson gson = new Gson();
            String userJson = gson.toJson(user);
            return new Register(counter.incrementAndGet(), userJson, success);
        } else {
            return new Register(counter.incrementAndGet(), "", success);
        }
    }

    @GetMapping("/getRole")
    public Response getRole(
        @RequestParam(value = "roleName") String roleName
    )
    {
        Role role = SkillslistServerApplication.db.getRole(roleName);
        if(role != null) {
            Gson gson = new Gson();
            String contentJson = gson.toJson(role);
            return new Response(counter.incrementAndGet(), contentJson);
        } else {
            return new Response(counter.incrementAndGet(), "");
        }
    }

    @GetMapping("/getAllRoles")
    public Response getAllRoles()
    {
        Role[] role = SkillslistServerApplication.db.getAllRoles();
        if(role != null) {
            Gson gson = new Gson();
            String contentJson = gson.toJson(role);
            return new Response(counter.incrementAndGet(), contentJson);
        } else {
            return new Response(counter.incrementAndGet(), "");
        }
    }
}