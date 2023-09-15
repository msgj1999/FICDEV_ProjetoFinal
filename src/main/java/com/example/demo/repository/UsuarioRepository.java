package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>, JpaSpecificationExecutor<Usuario>{
	 @Query(
		        value = "SELECT * FROM usuario u WHERE u.email = ?1",
		        nativeQuery = true
		    )
	Optional<Usuario> findByLogin(String login);
}
