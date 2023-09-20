package com.example.demo.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.demo.entities.Entrega;

public interface EntregaRepository extends JpaRepository<Entrega, Integer>, JpaSpecificationExecutor<Entrega>{

	Page<Entrega> findAll(Pageable pageable);
	Page<Entrega> findAll(Specification<Entrega> spec, Pageable pageable);

}
