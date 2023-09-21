package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.demo.entities.Manutencao;

public interface ManutencaoRepository extends JpaRepository<Manutencao, Integer>, JpaSpecificationExecutor<Manutencao>{
	
	Page<Manutencao> findAll(Pageable pageable);
	Page<Manutencao> findAll(Specification<Manutencao> spec, Pageable pageable);

}
