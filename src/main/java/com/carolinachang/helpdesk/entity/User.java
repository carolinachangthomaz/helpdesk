package com.carolinachang.helpdesk.entity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.carolinachang.helpdesk.enuns.ProFileEnum;

@Document
public class User {

	@Id
	private String id;
	@Indexed(unique = true)
	@NotBlank(message="Email required")
	@Email(message="Email válido")
	private String email;
	
	@NotBlank(message="Password required")
	@Size(min=6)
	private String password;
	
	private ProFileEnum proFile;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ProFileEnum getProFile() {
		return proFile;
	}

	public void setProFile(ProFileEnum proFile) {
		this.proFile = proFile;
	}
	
	
	
}
