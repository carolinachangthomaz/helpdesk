package com.carolinachang.helpdesk.security.jwt;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.carolinachang.helpdesk.entity.User;
import com.carolinachang.helpdesk.enuns.ProFileEnum;

public class JwtUserfactory {

	public JwtUserfactory() {
	}
	
	public static JwtUser  create(User user) {
		return new JwtUser(user.getId(), user.getEmail(), user.getPassword(), 
				mapToGrantedAuthorities(user.getProFile()));
	}

	private static List<GrantedAuthority> mapToGrantedAuthorities(ProFileEnum proFile) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(proFile.toString()));
		return authorities;
	}
	

}
