package com.example.demo.dto;

import com.example.demo.security.UserRole;

public class UsuarioDTO {

    private int id;
    private String nome;
    private String email;
    private UserRole role;
    
	public UsuarioDTO(int id, String nome, String email, UserRole role) {
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.role = role;
	}

	public UsuarioDTO() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}
    
    
}
