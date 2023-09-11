package com.example.demo.entities;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.FutureOrPresent;

@Entity
public class Entrega {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String nomePolicial;
	private int quantidade;
	@FutureOrPresent
	@DateTimeFormat (pattern="yyyy-MM-dd")
	private LocalDate dataEntrega;
	private  String observacoes;
	
	@ManyToOne
	@JoinColumn(name="id_municao")
	private Municao municao;
	public Entrega(int id, String nomePolicial, int quantidade, @FutureOrPresent LocalDate dataEntrega,
			String observacoes, Municao municao) {
		super();
		this.id = id;
		this.nomePolicial = nomePolicial;
		this.quantidade = quantidade;
		this.dataEntrega = dataEntrega;
		this.observacoes = observacoes;
		this.municao = municao;
	}
	
	public Entrega() {
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNomePolicial() {
		return nomePolicial;
	}
	public void setNomePolicial(String nomePolicial) {
		this.nomePolicial = nomePolicial;
	}
	public int getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	public LocalDate getDataEntrega() {
		return dataEntrega;
	}
	public void setDataEntrega(LocalDate dataEntrega) {
		this.dataEntrega = dataEntrega;
	}
	public String getObservacoes() {
		return observacoes;
	}
	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}
	public Municao getMunicao() {
		return municao;
	}
	public void setMunicao(Municao municao) {
		this.municao = municao;
	}
	
	
}