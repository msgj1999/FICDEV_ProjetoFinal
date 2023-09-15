package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;


import com.example.demo.entities.Manutencao;

public interface ManutencaoRepository extends JpaRepository<Manutencao, Integer>{
	
	List<Manutencao> findByStatusContainingIgnoreCase(String status);
	List<Manutencao> findByDataManutencao(LocalDate data);


}
