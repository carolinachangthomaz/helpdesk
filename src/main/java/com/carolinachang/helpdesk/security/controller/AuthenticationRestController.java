package com.carolinachang.helpdesk.security.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.carolinachang.helpdesk.entity.User;
import com.carolinachang.helpdesk.security.jwt.JwtAuthenticationRequest;
import com.carolinachang.helpdesk.security.jwt.JwtTokenUtil;
import com.carolinachang.helpdesk.security.model.CurrentUser;
import com.carolinachang.helpdesk.service.UserService;

@RestController
@CrossOrigin(origins = "*")
public class AuthenticationRestController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private UserService userService;
	
	
	@PostMapping(value="/login")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticatioRequest) throws Exception{
		final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticatioRequest.getEmail(),authenticatioRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticatioRequest.getEmail());
		final String token = jwtTokenUtil.generateToken(userDetails);
		final User user = userService.findbyEmail(authenticatioRequest.getEmail());
		user.setPassword(null);
		return ResponseEntity.ok(new CurrentUser(token, user));
				
	}
	
	@PostMapping(value="/login/refresh")
	public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) throws Exception{
		String token = request.getHeader("Authorization");
		String username = jwtTokenUtil.getUserNameFromToken(token);
		final User user = userService.findbyEmail(username);
		
		if(jwtTokenUtil.canTokenBeRefreshed(token)) {
			String refreshtoken = jwtTokenUtil.refreshToken(token);
			return ResponseEntity.ok(new CurrentUser(refreshtoken, user));
		}else{
			return ResponseEntity.badRequest().body(null);
		}	
				
	}
	
}
