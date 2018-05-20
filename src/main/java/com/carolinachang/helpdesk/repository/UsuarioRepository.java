package com.carolinachang.helpdesk.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.carolinachang.helpdesk.entity.User;

public interface UsuarioRepository extends MongoRepository<User, String>{

	User findByEmail(String email);
}
