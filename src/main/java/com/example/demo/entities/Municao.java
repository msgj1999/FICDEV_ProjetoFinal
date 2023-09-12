package com.example.demo.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;

@Entity
public class Municao {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String tipo;
	private String calibre;
	private String periculosidade;
	@FutureOrPresent
	
	@ManyToOne
	@JoinColumn(name="id_armazem")
	private Armazem armazem;

	public Municao(int id, String tipo, String calibre, String periculosidade,
			@FutureOrPresent LocalDate dataFabricacao, @Future LocalDate dataValidade, Armazem armazem) {
		super();
		this.id = id;
		this.tipo = tipo;
		this.calibre = calibre;
		this.periculosidade = periculosidade;
		this.armazem = armazem;
	}
	
	public Municao() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getCalibre() {
		return calibre;
	}

	public void setCalibre(String calibre) {
		this.calibre = calibre;
	}

	public String getPericulosidade() {
		return periculosidade;
	}

	public void setPericulosidade(String periculosidade) {
		this.periculosidade = periculosidade;
	}


	public Armazem getArmazem() {
		return armazem;
	}

	public void setArmazem(Armazem armazem) {
		this.armazem = armazem;
	}
	
}
