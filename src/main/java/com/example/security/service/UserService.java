package com.example.security.service;

import java.util.List;

import com.example.security.domain.Role;
import com.example.security.domain.User;

public interface UserService {
	
	User saveUser(User user);
	Role saveRole(Role role);
	void addROleUser(String username, String rolename);
	User getUser(String username);
	List<User> getUsers();

}
