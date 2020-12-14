package com.bitsplease.skillslist_server;

import com.bitsplease.skillslist_server.data.Role;
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
		db.resetAll(true);

		Role teacherRole = new Role("Teacher", true, true);
		Role studentRole = new Role("Student", true, true);
		db.insertRole(teacherRole);
		db.insertRole(studentRole);
		studentRole.setCanAddSkill(false);
		studentRole.setCanValidate(false);
		db.updateRole(studentRole);

		// teacherRole = db.getRole("Teacher");
		// studentRole = db.getRole("Student");

		User admin = new User("admin", "password", "", "", teacherRole);
		db.insertUser(admin);
		// User test = new User("test", "wesh", "Hugo", "Bollon", studentRole);

		// db.insertUser(new User("hbollon", "coucou", "Hugo", "Bollon", teacherRole));
		// db.insertUser(new User("user1", "password", "", "", studentRole));
		// db.insertUser(new User("user2", "password", "", "", studentRole));
		// db.insertUser(new User("user3", "password", "", "", studentRole));
		// db.insertUser(test);
		// db.updateUser(new User("hbollon", "coucou", "Hugo", "Bollon", studentRole));
		// User currentUser = db.connectUser("hbollon", "coucou");

		// db.insertSkillBlock(new SkillBlock("C++", "Your skill in this language"));
		// db.insertSkillBlock(new SkillBlock("Go", "Your skill in this language"));
		// db.insertSkillBlock(new SkillBlock("C", "Your skill in this language"));

		// db.insertSkillBlock(new SkillBlock("JS", "Your skill in this language"), new Skill[]{
		// 	new Skill("Threads", "", false),
		// 	new Skill("VueJS", "", true),
		// 	new Skill("Lambda functions", "", false)
		// });

		// SkillBlock[] testSkillBlocks = db.getAllSkillBlock();
		// for (SkillBlock skillBlock : testSkillBlocks) {
		// 	System.out.println(skillBlock.toString());
		// }
		// db.updateSkillBlock(new SkillBlock("C++", "You're a dumb bro!"));
		// System.out.println(db.getSkillBlock("C++").toString());

		// db.insertSkill("Go", new Skill("COO", "ex1", true));
		// db.insertSkill("Go", new Skill("Pointeurs", "ex", false));
		// db.insertSkill("Go", new Skill("Modules", "ex", false));
		// db.insertSkill("C++", new Skill("COO", "ex", true));
		// db.insertSkill("C++", new Skill("Pointeurs", "ex", false));
		// db.updateSkill("Go", new Skill("Pointeurs", "ex3", false));
		// db.deleteSkill("Go", "COO");
		// Skill[] testSkills = db.getAllSkillFromSkillBlock("Go");
		// for (Skill skill : testSkills) {
		// 	System.out.println(skill.toString());
		// }

		// db.subscribeSkillBlock(currentUser.getUsername(), db.getSkillBlock("Go").getBlockName());
		// db.subscribeSkillBlock(currentUser.getUsername(), db.getSkillBlock("C").getBlockName());
		// db.subscribeSkillBlock(currentUser.getUsername(), db.getSkillBlock("C++").getBlockName());
		// db.unsubscribeSkillBlock(currentUser.getUsername(), db.getSkillBlock("C").getBlockName());

		// db.requestSkill(currentUser.getUsername(), "Pointeurs", "Go");
		// db.requestSkill(currentUser.getUsername(), "Modules", "Go");
		// db.requestSkill(currentUser.getUsername(), "Pointeurs", "C++");
		// db.requestSkill(currentUser.getUsername(), "COO", "C++");
		// db.requestSkill(currentUser.getUsername(), "VueJS", "JS");

		// db.validateSkill(admin.getUsername(), "hbollon", "Go", "Pointeurs");
		// db.validateSkill(test.getUsername(), "hbollon", "Go", "Modules");
		// db.cancelSkillRequest(test.getUsername(), "hbollon", "C++", "Pointeurs");
		// db.cancelSkillRequest(admin.getUsername(), "hbollon", "C++", "Pointeurs");
		// db.cancelSkillRequest(currentUser.getUsername(), "hbollon", "C++", "COO");

		// Skill[] userSkills = db.getAllSkillOfUser("hbollon");
		// for (Skill skill : userSkills) {
		// 	System.out.println(skill.toString());
		// }

		// System.out.println();

		// Skill[] userSkillsSb = db.getAllSkillOfUserBySkillblock("hbollon", "Go");
		// for (Skill skill : userSkillsSb) {
		// 	System.out.println(skill.toString());
		// }

		// System.out.println();

		// Skill[] testValidateSkills = db.getAllSkillOfUser("hbollon");
		// for (Skill skill : testValidateSkills) {
		// 	try{
		// 		System.out.println(db.checkSkillValidation("hbollon", skill));
		// 	} catch (Exception e) {
		// 		e.printStackTrace();
		// 	}
		// }

		// Skill[] allSkillsSbByUser = db.getAllSkillOfSkillblockByUser("hbollon", "JS");
		// for (Skill skill : allSkillsSbByUser) {
		// 	System.out.println(skill.toString());
		// }

		// User[] students = db.getAllUserWithRole(new Role(2));
		// for (User user : students) {
		// 	System.out.println(user.toString());
		// }

		/*

		db.subscribeSkillBlock(currentUser.getUsername(), db.getSkillBlock("Go").getBlockName());
		db.subscribeSkillBlock(currentUser.getUsername(), db.getSkillBlock("C").getBlockName());
		db.subscribeSkillBlock(currentUser.getUsername(), db.getSkillBlock("Cpp").getBlockName());
		db.unsubscribeSkillBlock(currentUser.getUsername(), db.getSkillBlock("C").getBlockName());

		Skill[] userSkills = db.getAllSkillOfUser("hbollon");
		for (Skill skill : userSkills) {
			System.out.println(skill.toString());
		}

		System.out.println();

		Skill[] userSkillsSb = db.getAllSkillOfUserBySkillblock("hbollon", "Go");
		for (Skill skill : userSkillsSb) {
			System.out.println(skill.toString());
		}

		System.out.println();

		Skill[] testValidateSkills = db.getAllSkillOfUser("hbollon");
		for (Skill skill : testValidateSkills) {
			try{
				System.out.println(db.checkSkillValidation("hbollon", skill));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Skill[] allSkillsSbByUser = db.getAllSkillOfSkillblockByUser("hbollon", "JS");
		for (Skill skill : allSkillsSbByUser) {
			System.out.println(skill.toString());
		}

		User[] students = db.getAllUserWithRole(new Role(2));
		for (User user : students) {
			System.out.println(user.toString());
		}
		*/

		SpringApplication.run(SkillslistServerApplication.class, args);
	}
}