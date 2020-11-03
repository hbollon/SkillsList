package com.bitsplease.skillslist_server;

import com.bitsplease.skillslist_server.database.DatabaseHandler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SkillslistServerApplication {

	public static void main(String[] args) {
		DatabaseHandler db = new DatabaseHandler();
		SpringApplication.run(SkillslistServerApplication.class, args);
	}

}