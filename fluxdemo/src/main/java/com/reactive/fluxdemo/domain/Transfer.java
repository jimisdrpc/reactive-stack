package com.reactive.fluxdemo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Transfer {
	@Id
	private String id;
	private String origem;
	private String destino;
	private String valor;
	private Integer status;

	public Transfer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Transfer(String id, String origem, String destino, String valor, Integer status) {
		super();
		this.id = id;
		this.origem = origem;
		this.destino = destino;
		this.valor = valor;
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
