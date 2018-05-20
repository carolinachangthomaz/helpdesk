package com.carolinachang.helpdesk.resource;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.carolinachang.helpdesk.entity.User;
import com.carolinachang.helpdesk.repository.UsuarioRepository;
import com.carolinachang.helpdesk.service.UserService;

@Service
public class UserResource implements UserService{

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	
	@Override
	public User findbyEmail(String email) {
		return this.usuarioRepository.findByEmail(email);
	}

	@Override
	public User createOrUpdate(User user) {
		return this.usuarioRepository.save(user);
	}

	@Override
	public User findById(String id) {
		Optional<User> obj = usuarioRepository.findById(id);
		return obj.get();
	}

	@Override
	public void delete(String id) {
		this.usuarioRepository.deleteById(id);
		
	}

	@Override
	public Page<User> findAll(int page, int count) {
		PageRequest pageRequest =  PageRequest.of(page,count);
		return usuarioRepository.findAll(pageRequest);
	}
	
}
