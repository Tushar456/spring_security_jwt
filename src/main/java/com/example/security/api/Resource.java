package com.example.security.api;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.security.domain.Role;
import com.example.security.domain.User;
import com.example.security.dto.RoleForm;
import com.example.security.service.UserService;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class Resource {
	
	private final UserService userService;
	
	@GetMapping("/user")
	public ResponseEntity<List<User>> getUsers(){
	
		return ResponseEntity.ok().body(userService.getUsers());
		
		
	}
	
	@PostMapping("/save/user")
	public ResponseEntity<User> saveUser(@RequestBody User user){
		
	   URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toString());
		return ResponseEntity.created(uri).body(userService.saveUser(user));
		
		
	}
	

	@PostMapping("/save/role")
	public ResponseEntity<Role> saveRole(@RequestBody Role role){
		
	   URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toString());
		return ResponseEntity.created(uri).body(userService.saveRole(role));
		
		
	}
	
	
	@PostMapping("/save/addToUser")
	public ResponseEntity<Role> addRoleToUser(@RequestBody RoleForm form){
		
	   URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toString());
	   userService.addROleUser(form.getUsername(), form.getRolename());
		return ResponseEntity.ok().build();
		
		
	}
	
	@GetMapping("/token/refresh")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException{
	

		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
	    if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")) {
	    	try {
	    	String refreshtoken = authorizationHeader.substring("Bearer ".length());
	    	Algorithm algorithm = Algorithm.HMAC256("test1234".getBytes());
	    	
	    	JWTVerifier verifier = JWT.require(algorithm).build();
	    	DecodedJWT decodedJwt = verifier.verify(refreshtoken);
	    	String username = decodedJwt.getSubject();
	    	User user = userService.getUser(username);
	    	
	   
			String access_token = JWT.create()
					              .withSubject(user.getUsername())
					              .withIssuer(request.getRequestURL().toString())
					              .withExpiresAt(new Date(System.currentTimeMillis() + 10 *60*1000))
					              .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
					              .sign(algorithm);

			
			Map<String,String> token = new HashMap<>();
			token.put("access_token", access_token);
			token.put("refresh_token", refreshtoken);
			response.setContentType("application/json");
			new ObjectMapper().writeValue(response.getOutputStream(), token);
			
	    	
	    	}catch (Exception e) {
				
	    		response.setHeader("error", e.getMessage());
	    		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
	    		Map<String,String> error = new HashMap<>();
	    		error.put("error_message", e.getMessage());
	    		response.setContentType("application/json");
	    		new ObjectMapper().writeValue(response.getOutputStream(), error);
			}
	    }
	    else {
	    	throw new RuntimeException("Refresh Token is missing");
	    }
	
	}
}
