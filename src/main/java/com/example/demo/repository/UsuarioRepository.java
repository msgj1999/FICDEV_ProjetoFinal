package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
	 @Query(
		        value = "SELECT * FROM usuario u WHERE u.email = ?1",
		        nativeQuery = true
		    )
	Optional<Usuario> findByLogin(String login);
	 
	 List<Usuario> findByNome(String nome);

	List<Usuario> findByNomeContainingIgnoreCase(String termo);

	List<Usuario> findByEmailContainingIgnoreCase(String termo);
}
