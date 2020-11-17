package com.bitsplease.skillslist_server.restservice.controller;

import com.bitsplease.skillslist_server.SkillslistServerApplication;
import com.bitsplease.skillslist_server.data.SkillBlock;
import com.bitsplease.skillslist_server.data.User;
import com.bitsplease.skillslist_server.restservice.Response;
import com.bitsplease.skillslist_server.restservice.SuccessState;
import com.google.gson.Gson;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class SkillBlockController {
    private final AtomicLong counter = new AtomicLong();

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

    @GetMapping("/skillBlockSubscribe")
    public SuccessState subscribe(
        @RequestParam(value = "currentUser") User user,
        @RequestParam(value = "skillblock") SkillBlock sb
    )
    {
        boolean res = SkillslistServerApplication.db.subscribeSkillBlock(user, sb);
        if(res) {
            return new SuccessState(counter.incrementAndGet(), true);
        } else {
            return new SuccessState(counter.incrementAndGet(), false);
        }
    }

    @GetMapping("/skillBlockUnsubscribe")
    public SuccessState unsubscribe(
        @RequestParam(value = "currentUser") User user,
        @RequestParam(value = "skillblock") SkillBlock sb
    )
    {
        boolean res = SkillslistServerApplication.db.unsubscribeSkillBlock(user, sb);
        if(res) {
            return new SuccessState(counter.incrementAndGet(), true);
        } else {
            return new SuccessState(counter.incrementAndGet(), false);
        }
    }
}
