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
	private Boolean status = true;
	@FutureOrPresent
	@DateTimeFormat (pattern="yyyy-MM-dd")
	private LocalDate dataManutencao;
	
	@ManyToOne
	@JoinColumn(name="id_armazem")
	private Armazem armazem;

	public Manutencao(int id, Boolean status, @FutureOrPresent LocalDate dataManutencao, Armazem armazem) {
		super();
		this.id = id;
		this.status = status;
		this.dataManutencao = dataManutencao;
		this.armazem = armazem;
	}
	
	public Manutencao() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public LocalDate getDataManutencao() {
		return dataManutencao;
	}

	public void setDataManutencao(LocalDate dataManutencao) {
		this.dataManutencao = dataManutencao;
	}

	public Armazem getArmazem() {
		return armazem;
	}

	public void setArmazem(Armazem armazem) {
		this.armazem = armazem;
	}
	
	
}
