package com.carolinachang.helpdesk.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carolinachang.helpdesk.dto.Sumario;
import com.carolinachang.helpdesk.entity.StatusAlteracao;
import com.carolinachang.helpdesk.entity.Ticket;
import com.carolinachang.helpdesk.entity.User;
import com.carolinachang.helpdesk.enuns.ProFileEnum;
import com.carolinachang.helpdesk.enuns.StatusEnum;
import com.carolinachang.helpdesk.response.Response;
import com.carolinachang.helpdesk.security.jwt.JwtTokenUtil;
import com.carolinachang.helpdesk.security.service.TicketService;
import com.carolinachang.helpdesk.service.UserService;

@RestController
@RequestMapping("/tickets")
@CrossOrigin(origins = "*")
public class TicketController {

	@Autowired
	private TicketService ticketService;

	@Autowired
	protected JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserService userService;

	@PostMapping
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	public ResponseEntity<Response<Ticket>> create(HttpServletRequest request, @RequestBody Ticket ticket,
			BindingResult result) {
		Response<Ticket> response = new Response<Ticket>();
		try {
			validateCreateTicket(ticket, result);
			if(result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			ticket.setStatus(StatusEnum.getStatus("Novo"));
			ticket.setUsuario(userFromRequest(request));
			ticket.setDate(new Date());
			ticket.setNumero(generateNumber());
			Ticket ticketPersisted = ticketService.createOrUpdate(ticket);
			response.setData(ticketPersisted);

		} catch (Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok().body(response);
	}

	private Integer generateNumber() {
		Random random = new Random();
		return random.nextInt(9999);
	}

	private User userFromRequest(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		String email = jwtTokenUtil.getUserNameFromToken(token);
		return userService.findbyEmail(email);
	}

	private void validateCreateTicket(Ticket ticket, BindingResult result) {
		if (ticket.getTitulo() == null) {
			result.addError(new ObjectError("Ticket ", "Titulo não informado"));
		}
	}

	@PutMapping
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	public ResponseEntity<Response<Ticket>> update(HttpServletRequest request, @RequestBody Ticket ticket, BindingResult result) {
		Response<Ticket> response = new Response<Ticket>();
		try {
			validateUpdateTicket(ticket, result);
			if(result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			Ticket ticketCurrent = ticketService.findById(ticket.getId());
			ticket.setStatus(ticketCurrent.getStatus());
			ticket.setUsuario(ticketCurrent.getUsuario());
			ticket.setDate(new Date());
			ticket.setNumero(ticketCurrent.getNumero());
			if(ticketCurrent.getAssinaturaUsuario() != null) {
				ticket.setAssinaturaUsuario(ticketCurrent.getAssinaturaUsuario());
			}
			Ticket ticketPersisted = ticketService.createOrUpdate(ticket);
			response.setData(ticketPersisted);
			
		} catch (Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok().body(response);
	}

	private void validateUpdateTicket(Ticket ticket,BindingResult result) {
		if(ticket.getId() == null) {
			result.addError(new ObjectError("Ticket ", "ID não informado"));
		}
		if(ticket.getTitulo() == null) {
			result.addError(new ObjectError("Ticket ", "Titulo não informado"));
		}
	}
	
	@GetMapping(value="{id}")
	@PreAuthorize("hasAnyRole('CUSTOMER' , 'TECNICIAN')")
	public ResponseEntity<Response<Ticket>> findById(@PathVariable("id") String id ){
		Response<Ticket> response = new Response<Ticket>();
		Ticket ticket = ticketService.findById(id);
		if(ticket == null) {
			response.getErros().add("Usuario não encontrado ID: " +id);
			return ResponseEntity.badRequest().body(response);
		}
		List<StatusAlteracao> alteracoes = new ArrayList<StatusAlteracao>();
		Iterable<StatusAlteracao> altercoescorrentes = ticketService.listStatusAlteracao(ticket.getId());
		for (Iterator<StatusAlteracao> iterator = altercoescorrentes.iterator() ; iterator.hasNext();) {
			StatusAlteracao alteracoesStatus = (StatusAlteracao) iterator.next();
			alteracoesStatus.setTicket(null);
			alteracoes.add(alteracoesStatus);
		}
		ticket.setListStatusAlteracao(alteracoes);
		response.setData(ticket);
		return ResponseEntity.ok().body(response);
	}
	
	@DeleteMapping(value="{id}")
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	public ResponseEntity<Response<String>> delete(@PathVariable("id") String id ){
		Response<String> response = new Response<String>();
		Ticket ticket = ticketService.findById(id);
		if(ticket == null) {
			response.getErros().add("Ticket não encontrado ID: " +id);
			return ResponseEntity.badRequest().body(response);
		}
		ticketService.delete(id);
		return ResponseEntity.ok(new Response<String>());
	}
	
	@GetMapping(value="{page}/{count}")
	@PreAuthorize("hasAnyRole('CUSTOMER' , 'TECNICIAN')")
	public ResponseEntity<Response<Page<Ticket>>> findAll(HttpServletRequest request,@PathVariable int page, @PathVariable int count){
		Response<Page<Ticket>> response = new Response<Page<Ticket>>();
		Page<Ticket> tickets = null;
		User user = userFromRequest(request);
		if(user.getProFile().equals(ProFileEnum.ROLE_TECNICIAN)) {
			tickets = ticketService.listTicket(page, count);
		}else if(user.getProFile().equals(ProFileEnum.ROLE_CUSTOMER)) {
			tickets = ticketService.findByCurrentUser(page, count, user.getId());
		}
		response.setData(tickets);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value="{page}/{count}/{numero}/{titulo}/{status}/{prioridade}/{designado}}")
	@PreAuthorize("hasAnyRole('CUSTOMER' , 'TECNICIAN')")
	public ResponseEntity<Response<Page<Ticket>>> findByParamsl(HttpServletRequest request,
			                                                    @PathVariable int page,
			                                                    @PathVariable int count,
			                                                    @PathVariable Integer numero,
			                                                    @PathVariable String titulo,
			                                                    @PathVariable String status,
			                                                    @PathVariable String prioridade,
			                                                    @PathVariable boolean designado){
		titulo = titulo.equals("uninformed") ? "" : titulo;
		status = titulo.equals("uninformed") ? "" : status;
		prioridade = titulo.equals("uninformed") ? "" : prioridade;
		
		Response<Page<Ticket>> response = new Response<Page<Ticket>>();
		Page<Ticket> tickets = null;
		
		if(numero > 0) {
			tickets = ticketService.findByNumber(page, count, numero);
		}else {
			User user = userFromRequest(request);
			if(user.getProFile().equals(ProFileEnum.ROLE_TECNICIAN)) {
				if(designado) {
					tickets = ticketService.findByParametrosAndUsuarioDesignado(page, count, titulo, status, prioridade, user.getId());
				}else {
					tickets = ticketService.findByParametros(page, count, titulo, status, prioridade);
				}
			}else if(user.getProFile().equals(ProFileEnum.ROLE_CUSTOMER)) {
				tickets = ticketService.findByParametrosAndCurrentUser(page, count, titulo, status, prioridade, user.getId());
			}
		}
		response.setData(tickets);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value="{id}/{status}")
	@PreAuthorize("hasAnyRole('CUSTOMER' , 'TECNICIAN')")
	public ResponseEntity<Response<Ticket>> alterarStatus(HttpServletRequest request,
			                                                   @PathVariable String id,
			                                                   @PathVariable String status,
			                                                   @RequestBody Ticket ticket,
			                                                   BindingResult result){
		Response<Ticket> response = new Response<Ticket>();
		try {
			validateStatusAlteracao(id, status, result);		
			if(result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			Ticket ticketCurrent = ticketService.findById(ticket.getId());
			ticketCurrent.setStatus(StatusEnum.getStatus(status));
			if(status.equals("designado")) {
				ticketCurrent.setAssinaturaUsuario(userFromRequest(request));
			}
			Ticket ticketPersisted = ticketService.createOrUpdate(ticketCurrent);
			StatusAlteracao statusAlteracao = new StatusAlteracao();
			statusAlteracao.setUsuarioAlteracao(userFromRequest(request));
			statusAlteracao.setDataAlteracao(new Date());
			statusAlteracao.setStatus(StatusEnum.getStatus(status));
			statusAlteracao.setTicket(ticketPersisted);
			ticketService.createStatusAlteracao(statusAlteracao);
			response.setData(ticketPersisted);
			
		} catch (Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok().body(response);
	}
	
	private void validateStatusAlteracao(String id,String status,BindingResult result) {
		if(id == null || id.equals("")) {
			result.addError(new ObjectError("Ticket ", "ID não informado"));
		}
		if(status == null || status.equals("")) {
			result.addError(new ObjectError("Ticket ", "Status não informado"));
		}
	}
	
	
	@GetMapping(value="{/sumarios}")
	@PreAuthorize("hasAnyRole('CUSTOMER' , 'TECNICIAN')")
	public ResponseEntity<Response<Sumario>> findSumario(){
		Response<Sumario> response = new Response<Sumario>();
		Sumario sumario = new Sumario();
		int quantidadeNovo = 0;
		int quantidadeResolvido = 0;
		int quantidadeAprovado = 0;
		int quantidadeReprovado = 0;
		int quantidadeDesignado = 0;
		int quantidadeFechado = 0;
		
		Iterable<Ticket> tikets = ticketService.findAll();
		if(tikets != null) {
			for (Iterator<Ticket> iterator = tikets.iterator() ; iterator.hasNext();) {
				Ticket tiket = (Ticket) iterator.next();
				if(tiket.getStatus().equals(StatusEnum.Novo)) {
					quantidadeNovo++;
				}
				if(tiket.getStatus().equals(StatusEnum.Resolvido)) {
					quantidadeResolvido++;
				}
				if(tiket.getStatus().equals(StatusEnum.Aprovado)) {
					quantidadeAprovado++;
				}
				if(tiket.getStatus().equals(StatusEnum.Reprovado)) {
					quantidadeReprovado++;
				}
				if(tiket.getStatus().equals(StatusEnum.Designado)) {
					quantidadeDesignado++;
				}
				if(tiket.getStatus().equals(StatusEnum.Fechado)) {
					quantidadeFechado++;
				}
			}
		}
		
		sumario.setQuantidadeNovo(quantidadeNovo);
		sumario.setQuantidadeResolvido(quantidadeResolvido);
		sumario.setQuantidadeAprovado(quantidadeAprovado);
		sumario.setQuantidadeReprovado(quantidadeReprovado);
		sumario.setQuantidadeDesignado(quantidadeDesignado);
		sumario.setQuantidadeFechado(quantidadeFechado);
		response.setData(sumario);
		return ResponseEntity.ok().body(response);
	}
 }
