package com.bitsplease.skillslist_server.restservice.controller;

import com.bitsplease.skillslist_server.SkillslistServerApplication;
import com.bitsplease.skillslist_server.data.Skill;
import com.bitsplease.skillslist_server.restservice.Response;
import com.bitsplease.skillslist_server.restservice.SuccessState;
import com.bitsplease.skillslist_server.restservice.model.SkillRequest;
import com.bitsplease.skillslist_server.restservice.model.SkillValidation;
import com.google.gson.Gson;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class SkillController {
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/getSkills")
    public Response getAllFromSkillBlock(
        @RequestParam(value = "blockName") String blockName
    )
    {
        Skill[] skills = SkillslistServerApplication.db.getAllSkillFromSkillBlock(blockName);
        if(skills != null && skills.length > 0) {
            Gson gson = new Gson();
            String contentJson = gson.toJson(skills);
            return new Response(counter.incrementAndGet(), contentJson);
        } else {
            return new Response(counter.incrementAndGet(), "");
        }
    }

    @GetMapping("/getAllUserSkills")
    public Response getAllUserSkills(
        @RequestParam(value = "username") String username
    )
    {
        Skill[] skills = SkillslistServerApplication.db.getAllSkillOfUser(username);
        if(skills != null && skills.length > 0) {
            Gson gson = new Gson();
            String contentJson = gson.toJson(skills);
            return new Response(counter.incrementAndGet(), contentJson);
        } else {
            return new Response(counter.incrementAndGet(), "");
        }
    }

    @PostMapping(path = "/requestSkill", consumes = "application/json", produces = "application/json")
    public SuccessState requestSkill(
        @RequestBody SkillRequest request) 
    {
        boolean success = SkillslistServerApplication.db.requestSkill(request.username, request.skillName, request.skillblockName);
        if(success){
            return new SuccessState(counter.incrementAndGet(), true);
        } else {
            return new SuccessState(counter.incrementAndGet(), false);
        }
    }

    @PostMapping(path = "/validateSkill", consumes = "application/json", produces = "application/json")
    public SuccessState register(
        @RequestBody SkillValidation request) 
    {
        boolean success = SkillslistServerApplication.db.validateSkill(request.connectedUsername, request.username, request.skillblockName, request.skillName);
        if(success){
            return new SuccessState(counter.incrementAndGet(), true);
        } else {
            return new SuccessState(counter.incrementAndGet(), false);
        }
    }
}