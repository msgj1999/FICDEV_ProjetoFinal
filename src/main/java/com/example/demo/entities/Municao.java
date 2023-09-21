package com.example.demo.entities;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

@Entity
public class Municao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "O tipo não pode ser nulo e deve ter no máximo 30 caracteres.")
    @Size(max = 30, message = "O tipo deve ter no máximo {max} caracteres.")
    private String tipo;

    @NotBlank(message = "O calibre não pode ser nulo e deve ter no máximo 30 caracteres.")
    @Size(max = 30, message = "O calibre deve ter no máximo {max} caracteres.")
    private String calibre;

    @NotNull(message = "A quantidade não pode ser nula.")
    @Min(value = 1, message = "A quantidade deve ser maior que zero.")
    @Max(value = 999999, message = "A quantidade deve ser menor ou igual a {value}.")
    private Integer quantidade;

    @NotNull(message = "O peso não pode ser nulo.")
    @DecimalMin(value = "0.01", message = "O peso deve ser maior que zero.")
    @DecimalMax(value = "1000.0", message = "O peso deve ser no máximo {value}.")
    private Float peso;

    @DecimalMax(value = "3.0", message = "O coeficiente balístico deve ser no máximo {value}.")
    private float coeficienteBalistico;

    @NotNull(message = "A data de fabricação não pode ser nula.")
    @PastOrPresent(message = "A data de fabricação deve ser no passado ou presente.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataFabricacao;

    @NotNull(message = "A data de validade não pode ser nula.")
    @Future(message = "A data de validade deve estar no futuro.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataValidade;
	
	
	public Municao(int id, String tipo, String calibre, Integer quantidade, Float peso, float coeficienteBalistico,
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


	public Integer getQuantidade() {
		return quantidade;
	}


	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}


	public Float getPeso() {
		return peso;
	}


	public void setPeso(Float peso) {
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