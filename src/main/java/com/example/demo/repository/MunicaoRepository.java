package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.demo.entities.Municao;

public interface MunicaoRepository extends JpaRepository<Municao, Integer>, JpaSpecificationExecutor<Municao>{

}
