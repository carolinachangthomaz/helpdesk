package com.carolinachang.helpdesk.resource;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.carolinachang.helpdesk.entity.StatusAlteracao;
import com.carolinachang.helpdesk.entity.Ticket;
import com.carolinachang.helpdesk.repository.StatusAlteracaoRepository;
import com.carolinachang.helpdesk.repository.TicketRepository;
import com.carolinachang.helpdesk.security.service.TicketService;

@Service
public class TicketResource implements TicketService{

	@Autowired
	private TicketRepository ticketRepository;
	
	@Autowired
	private StatusAlteracaoRepository statusAlteracaoRepository;
	
	
	
	@Override
	public Ticket createOrUpdate(Ticket ticket) {
		return this.createOrUpdate(ticket);
	}

	@Override
	public Ticket findById(String id) {
		Optional<Ticket> obj = ticketRepository.findById(id);
		return obj.get();
	}

	@Override
	public void delete(String id) {
		this.ticketRepository.deleteById(id);
		
	}

	@Override
	public Page<Ticket> listTicket(int page, int count) {
		PageRequest pageRequest = PageRequest.of(page, count);
		return this.ticketRepository.findAll(pageRequest);
	}

	@Override
	public StatusAlteracao createStatusAlteracao(StatusAlteracao statusAlteracao) {
		return this.statusAlteracaoRepository.save(statusAlteracao);
	}

	@Override
	public Iterable<StatusAlteracao> listStatusAlteracao(String tichetId) {
		return this.statusAlteracaoRepository.findByTicketIdOrderByDataAlteracao(tichetId);
	}

	@Override
	public Page<Ticket> findByCurrentUser(int page, int count, String userId) {
		PageRequest pageRequest = PageRequest.of(page, count);
		return this.ticketRepository.findByUsuarioIdOrderByDateDesc(pageRequest, userId);
	}

	@Override
	public Page<Ticket> findByParametros(int page, int count, String title, String status, String priority) {
		PageRequest pageRequest = PageRequest.of(page, count);
		return this.ticketRepository.findByTituloIgnoreCaseContainingAndStatusAndPrioridadeOrderByDateDesc(title, status, priority, pageRequest);
	}

	@Override
	public Page<Ticket> findByParametrosAndCurrentUser(int page, int count, String title, String status,
			String priority, String userId) {
		PageRequest pageRequest = PageRequest.of(page, count);
		return this.ticketRepository.findByTituloIgnoreCaseContainingAndStatusAndPrioridadeAndUsuarioIdOrderByDateDesc(title, status, priority, pageRequest);
	}

	@Override
	public Page<Ticket> findByNumber(int page, int count, Integer number) {
		PageRequest pageRequest = PageRequest.of(page, count);
		return this.ticketRepository.findByNumero(number, pageRequest);
	}

	@Override
	public Iterable<Ticket> findAll() {
		return this.ticketRepository.findAll();
	}

	@Override
	public Page<Ticket> findByParametrosAndUsuarioDesignado(int page, int count, String title, String status,
			String priority, String usuarioDesignado) {
		PageRequest pageRequest = PageRequest.of(page, count);
		return this.ticketRepository.findByTituloIgnoreCaseContainingAndStatusAndPrioridadeAndAssinaturaUsuarioIdOrderByDateDesc(title, status, priority, pageRequest);
	}

}
