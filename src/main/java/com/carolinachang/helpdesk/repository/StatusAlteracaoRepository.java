package com.carolinachang.helpdesk.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.carolinachang.helpdesk.entity.StatusAlteracao;

public interface StatusAlteracaoRepository  extends MongoRepository<StatusAlteracao, String>{

	Iterable<StatusAlteracao> findByTicketIdOrderByDataAlteracao(String ticketId);
}
