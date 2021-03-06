package com.example.security.filter;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.jooq.JooqExceptionTranslator;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.HttpClientErrorException.Forbidden;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if(request.getServletPath().equals("/login") || request.getServletPath().equals("/api/token/refresh")) {
			
			filterChain.doFilter(request, response);
			
		}
		else {
			
			String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		    if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")) {
		    	try {
		    	String token = authorizationHeader.substring("Bearer ".length());
		    	Algorithm algorithm = Algorithm.HMAC256("test1234".getBytes());
		    	
		    	JWTVerifier verifier = JWT.require(algorithm).build();
		    	DecodedJWT decodedJwt = verifier.verify(token);
		    	String username = decodedJwt.getSubject();
		    	String[] roles= decodedJwt.getClaim("roles").asArray(String.class);
		    	
		    	Collection<SimpleGrantedAuthority> authorities =  new ArrayList<>();
		    	for(String role : roles) {
		    		
		    		authorities.add(new SimpleGrantedAuthority(role));
		    	}
		    	
		    	UsernamePasswordAuthenticationToken authenticationToken = 
		    			new UsernamePasswordAuthenticationToken(username, null, authorities);
		    	SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		    	filterChain.doFilter(request, response);
		    	
		    	}catch (Exception e) {
					
		    		log.error("Error logging in", e.getMessage());
		    		response.setHeader("error", e.getMessage());
		    		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		    		Map<String,String> error = new HashMap<>();
		    		error.put("error_message", e.getMessage());
		    		response.setContentType("application/json");
		    		new ObjectMapper().writeValue(response.getOutputStream(), error);
				}
		    }
		    else {
		    	filterChain.doFilter(request, response);
		    }
		}
		
	}

}
