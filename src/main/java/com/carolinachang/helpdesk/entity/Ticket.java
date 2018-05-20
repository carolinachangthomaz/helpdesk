package com.carolinachang.helpdesk.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.carolinachang.helpdesk.enuns.PrioridadeEnum;
import com.carolinachang.helpdesk.enuns.StatusEnum;

@Document
public class Ticket {

	@Id
	private String id;
	@DBRef(lazy = true)
	private User usuario;
	
	private Date date;
	
	private String titulo;
	
	private Integer numero;
	
	private StatusEnum status;
	
	private PrioridadeEnum prioridade;
	
	
	@DBRef(lazy = true)
	private User assinaturaUsuario;
	
	private String descricao;
	
	private String image;
	
	@Transient
	private List<StatusAlteracao> listStatusAlteracao;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getUsuario() {
		return usuario;
	}

	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer number) {
		this.numero = number;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public PrioridadeEnum getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(PrioridadeEnum priority) {
		this.prioridade = priority;
	}

	public User getAssinaturaUsuario() {
		return assinaturaUsuario;
	}

	public void setAssinaturaUsuario(User assinaturaUsuario) {
		this.assinaturaUsuario = assinaturaUsuario;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public List<StatusAlteracao> getListStatusAlteracao() {
		return listStatusAlteracao;
	}

	public void setListStatusAlteracao(List<StatusAlteracao> listStatusAlteracao) {
		this.listStatusAlteracao = listStatusAlteracao;
	}
	
}
