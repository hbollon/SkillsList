package com.bitsplease.skillslist_server;

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
		SpringApplication.run(SkillslistServerApplication.class, args);
	}
}