package com.carolinachang.helpdesk.dto;

import java.io.Serializable;

public class Sumario implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer quantidadeNovo;
	private Integer quantidadeResolvido;
	private Integer quantidadeAprovado;
	private Integer quantidadeReprovado;
	private Integer quantidadeDesignado;
	private Integer quantidadeFechado;
	
	
	public Integer getQuantidadeNovo() {
		return quantidadeNovo;
	}
	public void setQuantidadeNovo(Integer quantidadeNovo) {
		this.quantidadeNovo = quantidadeNovo;
	}
	public Integer getQuantidadeResolvido() {
		return quantidadeResolvido;
	}
	public void setQuantidadeResolvido(Integer quantidadeResolvido) {
		this.quantidadeResolvido = quantidadeResolvido;
	}
	public Integer getQuantidadeAprovado() {
		return quantidadeAprovado;
	}
	public void setQuantidadeAprovado(Integer quantidadeAprovado) {
		this.quantidadeAprovado = quantidadeAprovado;
	}
	public Integer getQuantidadeReprovado() {
		return quantidadeReprovado;
	}
	public void setQuantidadeReprovado(Integer quantidadeReprovado) {
		this.quantidadeReprovado = quantidadeReprovado;
	}
	public Integer getQuantidadeDesignado() {
		return quantidadeDesignado;
	}
	public void setQuantidadeDesignado(Integer quantidadeDesignado) {
		this.quantidadeDesignado = quantidadeDesignado;
	}
	public Integer getQuantidadeFechado() {
		return quantidadeFechado;
	}
	public void setQuantidadeFechado(Integer quantidadeFechado) {
		this.quantidadeFechado = quantidadeFechado;
	}
	
	
	
}
