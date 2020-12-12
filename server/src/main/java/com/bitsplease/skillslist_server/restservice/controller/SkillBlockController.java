package com.bitsplease.skillslist_server.restservice.controller;

import com.bitsplease.skillslist_server.SkillslistServerApplication;
import com.bitsplease.skillslist_server.data.SkillBlock;
import com.bitsplease.skillslist_server.data.User;
import com.bitsplease.skillslist_server.restservice.Response;
import com.bitsplease.skillslist_server.restservice.SuccessState;
import com.bitsplease.skillslist_server.restservice.model.SkillblockSkills;
import com.bitsplease.skillslist_server.restservice.model.StringPair;
import com.bitsplease.skillslist_server.utils.Pair;
import com.google.gson.Gson;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class SkillBlockController {
    private final AtomicLong counter = new AtomicLong();

    @PostMapping(path = "/addSkillBlock", consumes = "application/json", produces = "application/json")
    public SuccessState addSkillBlock(
        @RequestBody SkillBlock request) 
    {
        boolean success = SkillslistServerApplication.db.insertSkillBlock(request);
        if(success){
            return new SuccessState(counter.incrementAndGet(), true);
        } else {
            return new SuccessState(counter.incrementAndGet(), false);
        }
    }

    @PostMapping(path = "/addSkillBlockWithSkills", consumes = "application/json", produces = "application/json")
    public SuccessState addSkillBlockWithSkills(
        @RequestBody SkillblockSkills request) 
    {
        boolean success = SkillslistServerApplication.db.insertSkillBlock(request.skillblock, request.skills);
        if(success){
            return new SuccessState(counter.incrementAndGet(), true);
        } else {
            return new SuccessState(counter.incrementAndGet(), false);
        }
    }

    @GetMapping("/getAllSkillBlocks")
    public Response getAll()
    {
        SkillBlock[] skillblocks = SkillslistServerApplication.db.getAllSkillBlock();
        if(skillblocks != null && skillblocks.length > 0) {
            Gson gson = new Gson();
            String contentJson = gson.toJson(skillblocks);
            return new Response(counter.incrementAndGet(), contentJson);
        } else {
            return new Response(counter.incrementAndGet(), "");
        }
    }

    @GetMapping("/getSkillBlock")
    public Response get(
        @RequestParam(value = "name") String name
    )
    {
        SkillBlock skillblock = SkillslistServerApplication.db.getSkillBlock(name);
        if(skillblock != null) {
            Gson gson = new Gson();
            String contentJson = gson.toJson(skillblock);
            return new Response(counter.incrementAndGet(), contentJson);
        } else {
            return new Response(counter.incrementAndGet(), "");
        }
    }

    @GetMapping("/getSubscribedSkillBlock")
    public Response getSubscribed(
        @RequestParam(value = "userId") int id
    )
    {
        SkillBlock[] skillblock = SkillslistServerApplication.db.getSubscribedSkillBlock(id);
        if(skillblock != null) {
            Gson gson = new Gson();
            String contentJson = gson.toJson(skillblock);
            return new Response(counter.incrementAndGet(), contentJson);
        } else {
            return new Response(counter.incrementAndGet(), "");
        }
    }

    @PostMapping(path = "/subscribe", consumes = "application/json", produces = "application/json")
    public SuccessState subscribe(
        @RequestBody StringPair content 
    )
    {
        boolean res = SkillslistServerApplication.db.subscribeSkillBlock(content.first, content.second);
        if(res) {
            return new SuccessState(counter.incrementAndGet(), true);
        } else {
            return new SuccessState(counter.incrementAndGet(), false);
        }
    }

    @PostMapping(path = "/unsubscribe", consumes = "application/json", produces = "application/json")
    public SuccessState unsubscribe(
        @RequestBody StringPair content
    )
    {
        boolean res = SkillslistServerApplication.db.unsubscribeSkillBlock(content.first, content.second);
        if(res) {
            return new SuccessState(counter.incrementAndGet(), true);
        } else {
            return new SuccessState(counter.incrementAndGet(), false);
        }
    }
}
