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
public class Manutencao {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String status;
	@DateTimeFormat (pattern="yyyy-MM-dd")
	private LocalDate dataManutencao;
	
	@ManyToOne
	@JoinColumn(name="id_municao")
	private Municao municao;

	public Manutencao(int id, String status, @FutureOrPresent LocalDate dataManutencao, Municao municao) {
		super();
		this.id = id;
		this.status = status;
		this.dataManutencao = dataManutencao;
		this.municao = municao;
	}
	
	public Manutencao() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDate getDataManutencao() {
		return dataManutencao;
	}

	public void setDataManutencao(LocalDate dataManutencao) {
		this.dataManutencao = dataManutencao;
	}

	public Municao getMunicao() {
		return municao;
	}

	public void setMunicao(Municao municao) {
		this.municao = municao;
	}
	
	
}
