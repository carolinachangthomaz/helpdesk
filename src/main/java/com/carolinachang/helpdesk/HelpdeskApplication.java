package com.carolinachang.helpdesk;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import com.carolinachang.helpdesk.entity.User;
import com.carolinachang.helpdesk.enuns.ProFileEnum;
import com.carolinachang.helpdesk.repository.UsuarioRepository;

@SpringBootApplication
public class HelpdeskApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(HelpdeskApplication.class, args);
	}
	
	@Bean
	CommandLineRunner init(UsuarioRepository usuarioRepository, PasswordEncoder encoder) {
		return args ->{
			initUsers(usuarioRepository,encoder);
		};
	}
	
	@Bean
	public HttpFirewall defaultHttpFirewall() {
	    return new DefaultHttpFirewall();
	}

	private void initUsers(UsuarioRepository usuarioRepository, PasswordEncoder encoder) {
		User admin = new User();
		admin.setEmail("carol.com.jp@gmail.com");
		admin.setPassword(encoder.encode("123456"));
		admin.setProFile(ProFileEnum.ROLE_ADMIN);
		
		User find = usuarioRepository.findByEmail("carol.com.jp@gmail.com");
		if(find == null) {
			usuarioRepository.save(admin);
		}
		
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		
		return super.configure(builder);
	}
	
	
}
