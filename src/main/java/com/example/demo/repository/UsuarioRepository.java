package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>, JpaSpecificationExecutor<Usuario> {
	@Query(value = "SELECT * FROM usuario u WHERE u.email = ?1", nativeQuery = true)
	Optional<Usuario> findByLogin(String login);

	/*
	 * @Query("SELECT u FROM Usuario u ORDER BY u.id") List<Usuario>
	 * findAllOrderedById();
	 */

	Page<Usuario> findAll(Pageable pageable);

	Page<Usuario> findAll(Specification<Usuario> spec, Pageable pageable);
}
