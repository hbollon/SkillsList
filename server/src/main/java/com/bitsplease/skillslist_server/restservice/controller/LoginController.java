package com.bitsplease.skillslist_server.restservice.controller;

import com.bitsplease.skillslist_server.restservice.Login;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class LoginController {

    private static final String template = "Login: %s, Password: %s";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/login")
    public Login login(@RequestParam(value = "login") String login, @RequestParam(value = "password") String password) {
        return new Login(counter.incrementAndGet(), String.format(template, login, password));
    }
}