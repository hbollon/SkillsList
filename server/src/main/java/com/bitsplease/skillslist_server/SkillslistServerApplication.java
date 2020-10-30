package com.bitsplease.skillslist_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SkillslistServerApplication {

	public static void main(String[] args) {
		database.DatabaseHandler db = new database.DatabaseHandler();
		SpringApplication.run(SkillslistServerApplication.class, args);
	}

}