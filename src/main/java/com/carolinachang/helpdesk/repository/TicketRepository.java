package com.carolinachang.helpdesk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.carolinachang.helpdesk.entity.Ticket;

public interface TicketRepository extends MongoRepository<Ticket, String>{

	Page<Ticket> findByUsuarioIdOrderByDateDesc(Pageable pageable, String userId);
	
	Page<Ticket> findByTituloIgnoreCaseContainingAndStatusContainingAndPrioridadeContainingOrderByDateDesc(
			String titulo,String status, String prioridade, Pageable pageable);
	
	Page<Ticket> findByTituloIgnoreCaseContainingAndStatusContainingAndPrioridadeContainingAndUsuarioIdOrderByDateDesc(
			String titulo,String status, String prioridade,String userId, Pageable pageable);
	
	Page<Ticket> findByTituloIgnoreCaseContainingAndStatusContainingAndPrioridadeContainingAndAssinaturaUsuarioIdOrderByDateDesc(
			String titulo,String status, String prioridade, String userId, Pageable pageable);
	
	Page<Ticket> findByNumero(Integer numero, Pageable pageable);
}
