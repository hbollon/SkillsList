package com.bitsplease.skillslist_server;

import com.bitsplease.skillslist_server.data.Skill;
import com.bitsplease.skillslist_server.data.SkillBlock;
import com.bitsplease.skillslist_server.data.User;
import com.bitsplease.skillslist_server.database.DatabaseHandler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SkillslistServerApplication {
	public static DatabaseHandler db = new DatabaseHandler();
	public static void main(String[] args) {
		db.resetAll();
		db.insertUser(new User("hbollon", "coucou", "Hugo", "Bollon"));

		db.insertSkillBlock(new SkillBlock("C++", "Your skill in this language"));
		db.insertSkillBlock(new SkillBlock("Go", "Your skill in this language"));
		db.insertSkillBlock(new SkillBlock("C", "Your skill in this language"));

		SkillBlock[] testSkillBlocks = db.getAllSkillBlock();
		for (SkillBlock skillBlock : testSkillBlocks) {
			System.out.println(skillBlock.toString());
		}
		db.updateSkillBlock(new SkillBlock("C++", "You're a dumb bro!"));
		System.out.println(db.getSkillBlock("C++").toString());

		db.insertSkill("Go", new Skill("COO", "ex1"));
		db.insertSkill("Go", new Skill("Pointeurs", "ex2"));
		db.updateSkill(new Skill("Pointeurs", "ex3"));
		db.deleteSkill("COO");
		Skill[] testSkills = db.getAllSkillFromSkillBlock("Go");
		for (Skill skill : testSkills) {
			System.out.println(skill.toString());
		}

		SpringApplication.run(SkillslistServerApplication.class, args);
	}
}