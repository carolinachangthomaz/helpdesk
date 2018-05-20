package com.carolinachang.helpdesk.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.carolinachang.helpdesk.entity.User;
import com.carolinachang.helpdesk.security.jwt.JwtUserfactory;
import com.carolinachang.helpdesk.service.UserService;

@Service
public class JwtUserDetailsService implements UserDetailsService{

	
	@Autowired
	private UserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userService.findbyEmail(email);
		if(user == null) {
			throw new UsernameNotFoundException(String.format("Usuario não encontrado", email));
		}else {
			return JwtUserfactory.create(user);
		}
	}

}
