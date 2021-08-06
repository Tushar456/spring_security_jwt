package com.example.security;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.security.domain.Role;
import com.example.security.domain.User;
import com.example.security.service.UserService;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
		
	}
	
	
	@Bean
	CommandLineRunner run(UserService userService) {
		return args -> {
//			userService.saveRole(new Role(null,"ROLE_USER"));
//			userService.saveRole(new Role(null,"ROLE_ADMIN"));
//			userService.saveRole(new Role(null,"ROLE_MANAGER"));
//			userService.saveRole(new Role(null,"ROLE_SUPER_ADMIN"));
			
			userService.saveRole(new Role(null,"501"));
			userService.saveRole(new Role(null,"502"));
			userService.saveRole(new Role(null,"503"));
			userService.saveRole(new Role(null,"504"));
			
			userService.saveUser(new User(null, "Tushar", "tushar231", "1234", new ArrayList<>()));
			userService.saveUser(new User(null, "Vishal", "vishal231", "1234", new ArrayList<>()));
			userService.saveUser(new User(null, "Rahul", "rahul231", "1234", new ArrayList<>()));
			userService.saveUser(new User(null, "Rinky", "rinky231", "1234", new ArrayList<>()));
			userService.saveUser(new User(null, "Sanjay", "sanjay231", "1234", new ArrayList<>()));
			userService.saveUser(new User(null, "Ritesh", "ritesh231", "1234", new ArrayList<>()));
			
//			userService.addROleUser("tushar231", "ROLE_SUPER_ADMIN");
//			userService.addROleUser("vishal231", "ROLE_ADMIN");
//			userService.addROleUser("rahul231", "ROLE_MANAGER");
//			userService.addROleUser("rinky231", "ROLE_USER");
//			userService.addROleUser("sanjay231", "ROLE_USER");
//			userService.addROleUser("ritesh231", "ROLE_USER");
			
			userService.addROleUser("tushar231", "501");
			userService.addROleUser("vishal231", "502");
			userService.addROleUser("rahul231", "503");
			userService.addROleUser("rinky231", "504");
			userService.addROleUser("rinky231", "501");
			userService.addROleUser("sanjay231", "504");
			userService.addROleUser("ritesh231", "504");
			
			
		};
		
	}

}
