package com.example.demo.entities;

import java.util.Collection;
import java.util.List;

import org.hibernate.proxy.HibernateProxy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.security.UserRole;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Usuario implements UserDetails{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotBlank(message="Nome deve ser preenchido")
	private String nome;
	@NotBlank(message="Email deve ser preenchido")
	private String email;
	@NotBlank(message="Senha deve ser preenchida")
	private String senha;
	
	private UserRole role = UserRole.TECNICO;
	
	public Usuario(int id, String nome, String email, String senha, UserRole role) {
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		if(role!=null) this.role = role;
	}
	
	public Usuario() {
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
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		switch (this.role) {
		case ADMIN:
			return List.of(new SimpleGrantedAuthority("ADMIN"), new SimpleGrantedAuthority("GESTOR"), new SimpleGrantedAuthority("TECNICO"));
		case GESTOR:
			return List.of(new SimpleGrantedAuthority("GESTOR"), new SimpleGrantedAuthority("TECNICO"));
		default:
			return List.of(new SimpleGrantedAuthority("TECNICO"));
		}
	}

	@Override
	public final boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		Class<?> oEffectiveClass = o instanceof HibernateProxy
				? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
				: o.getClass();
		Class<?> thisEffectiveClass = this instanceof HibernateProxy
				? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
				: this.getClass();
		if (thisEffectiveClass != oEffectiveClass)
			return false;
		Usuario usuario = (Usuario) o;
		return  getId() == usuario.getId();
	}

	@Override
	public final int hashCode() {
		return getClass().hashCode();
	}

	public String getPassword() {
		// TODO Auto-generated method stub
		return this.senha;
	}

	public String getUsername() {
		// TODO Auto-generated method stub
		return this.email;
	}

	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
}
