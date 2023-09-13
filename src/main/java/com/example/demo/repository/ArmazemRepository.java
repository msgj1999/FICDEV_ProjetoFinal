package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Armazem;
import com.example.demo.entities.Municao;

public interface ArmazemRepository extends JpaRepository<Armazem, Integer>{

	Armazem findByMunicao(Municao municao);

}
