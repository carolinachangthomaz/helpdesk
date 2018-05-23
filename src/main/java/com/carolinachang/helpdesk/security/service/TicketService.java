package com.carolinachang.helpdesk.security.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.carolinachang.helpdesk.entity.StatusAlteracao;
import com.carolinachang.helpdesk.entity.Ticket;

@Component
public interface TicketService {

	Ticket createOrUpdate(Ticket ticket);
	
	Ticket findById(String id);
	
	void delete(String id);
	
	Page<Ticket> listTicket(int age,int count);
	
	StatusAlteracao createStatusAlteracao(StatusAlteracao statusAlteracao);
	
	Iterable<StatusAlteracao> listStatusAlteracao(String tichetId);
	
	Page<Ticket> findByCurrentUser(int page, int count, String userId);
	
	Page<Ticket>findByParametros(int page, int count, String title, String status, String priority);
	
	Page<Ticket> findByParametrosAndCurrentUser(int page, int count, String title, String status, String priority, String userId);
	
	Page<Ticket> findByNumber(int page, int count, Integer number);
	
	Iterable<Ticket> findAll();
	
	Page<Ticket> findByParametrosAndUsuarioDesignado(int page, int count, String title, String status, String priority, String usuarioDesignado);
	
}
