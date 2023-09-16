package com.example.demo.entities;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.PastOrPresent;

@Entity
public class Municao {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String tipo;
	private String calibre;
	private int quantidade;
	private float peso;
	private float coeficienteBalistico;
	@PastOrPresent
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataFabricacao;
	@Future
	@DateTimeFormat (pattern="yyyy-MM-dd")
	private LocalDate dataValidade;
	
	
	public Municao(int id, String tipo, String calibre, int quantidade, float peso, float coeficienteBalistico,
			@PastOrPresent LocalDate dataFabricacao, @Future LocalDate dataValidade) {
		super();
		this.id = id;
		this.tipo = tipo;
		this.calibre = calibre;
		this.quantidade = quantidade;
		this.peso = peso;
		this.coeficienteBalistico = coeficienteBalistico;
		this.dataFabricacao = dataFabricacao;
		this.dataValidade = dataValidade;
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


	public int getQuantidade() {
		return quantidade;
	}


	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}


	public float getPeso() {
		return peso;
	}


	public void setPeso(float peso) {
		this.peso = peso;
	}


	public float getCoeficienteBalistico() {
		return coeficienteBalistico;
	}


	public void setCoeficienteBalistico(float coeficienteBalistico) {
		this.coeficienteBalistico = coeficienteBalistico;
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