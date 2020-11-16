package com.bitsplease.skillslist_server.restservice.controller;

import com.bitsplease.skillslist_server.SkillslistServerApplication;
import com.bitsplease.skillslist_server.data.Skill;
import com.bitsplease.skillslist_server.restservice.Response;
import com.google.gson.Gson;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class SkillController {
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/getAllSkill")
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
}