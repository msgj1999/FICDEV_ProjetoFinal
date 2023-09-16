package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entities.Municao;

public interface MunicaoRepository extends JpaRepository<Municao, Integer>, JpaSpecificationExecutor<Municao>{

	@Query("SELECT m.tipo AS tipo, m.calibre AS calibre, SUM(m.quantidade) AS quantidade FROM Municao m GROUP BY m.tipo, m.calibre")
	List<Object[]> findTipoCalibreQuantidade();


}
