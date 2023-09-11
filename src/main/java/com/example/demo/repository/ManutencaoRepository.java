package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Manutencao;

public interface ManutencaoRepository extends JpaRepository<Manutencao, Integer>{

}
