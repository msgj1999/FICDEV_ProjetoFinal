package com.example.demo.entities;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.PastOrPresent;

@Entity
public class Armazem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int quantidade;
	@PastOrPresent
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataFabricacao;
	@Future
	@DateTimeFormat (pattern="yyyy-MM-dd")
	private LocalDate dataValidade;
	
	@ManyToOne
	@JoinColumn(name="id_municao")
	private Municao municao;
	
	
	
	public Armazem(int id, int quantidade, @PastOrPresent LocalDate dataFabricacao, @Future LocalDate dataValidade,
			Municao municao) {
		super();
		this.id = id;
		this.quantidade = quantidade;
		this.dataFabricacao = dataFabricacao;
		this.dataValidade = dataValidade;
		this.municao = municao;
	}
	
	public Armazem() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	
    public Municao getMunicao() {
        return municao;
    }

    public void setMunicao(Municao municao) {
        this.municao = municao;
    }

	public LocalDate getDataFabricacao() {
		return dataFabricacao;
	}

	public void setDataFabricacao(LocalDate dataFabricacao) {
		this.dataFabricacao = dataFabricacao;
	}

	public LocalDate getDataValidade() {
		return dataValidade;
	}

	public void setDataValidade(LocalDate dataValidade) {
		this.dataValidade = dataValidade;
	}
	
    
}
