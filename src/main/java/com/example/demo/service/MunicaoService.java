package com.example.demo.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Municao;
import com.example.demo.repository.MunicaoRepository;

import jakarta.persistence.criteria.Predicate;

@Service
public class MunicaoService {

    @Autowired
    private MunicaoRepository municaoRepository;

    public List<Municao> getAllMunicoes() {
        return municaoRepository.findAllOrderedById();
    }

    public Municao getMunicao(int id) throws NotFoundException {
        return municaoRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public void saveMunicao(Municao municao) {
        municaoRepository.save(municao);
    }

    public Municao updateMunicao(Municao municao, int id) throws NotFoundException {
        Municao atualizada = municaoRepository.findById(id).orElseThrow(NotFoundException::new);

        atualizada.setTipo(municao.getTipo());
        atualizada.setCalibre(municao.getCalibre());
        atualizada.setQuantidade(municao.getQuantidade());
        atualizada.setPeso(municao.getPeso());
        atualizada.setCoeficienteBalistico(municao.getCoeficienteBalistico());
        atualizada.setDataFabricacao(municao.getDataFabricacao());
        atualizada.setDataValidade(municao.getDataValidade());

        municaoRepository.save(atualizada);
        return atualizada;
    }


    public Municao deleteMunicao(int id) throws NotFoundException {
        Municao deletada = municaoRepository.findById(id).orElseThrow(NotFoundException::new);
        municaoRepository.delete(deletada);
        return deletada;
    }
    
    public List<Municao> buscarMunicoesPorFiltro(String termo) {
        Specification<Municao> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

	    predicates.add(cb.like(cb.lower(root.get("tipo")), "%" + termo.toLowerCase() + "%"));

	    predicates.add(cb.like(cb.lower(root.get("calibre")), "%" + termo.toLowerCase() + "%"));


            // Busca por quantidade
            try {
                int quantidade = Integer.parseInt(termo);
                predicates.add(cb.equal(root.get("quantidade"), quantidade));
            } catch (NumberFormatException e) {
                // Ignorar se o termo não for um número válido
            }

	    try {
            	float peso = Float.parseFloat(termo);
            	predicates.add(cb.equal(root.get("peso"), peso));
            } catch (NumberFormatException e) {
            	// Ignorar se o termo não for um número válido
            }

	    try {
            	float coeficienteBalistico = Float.parseFloat(termo);
            	predicates.add(cb.equal(root.get("coeficienteBalistico"), coeficienteBalistico));
            } catch (NumberFormatException e) {
            	// Ignorar se o termo não for um número válido
            }


            try {
                LocalDate dataFabricacao = LocalDate.parse(termo, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                predicates.add(cb.equal(root.get("dataFabricacao"), dataFabricacao));
            } catch (DateTimeParseException e) {
                // Ignorar se o termo não for uma data válida
            }

            try {
                LocalDate dataValidade = LocalDate.parse(termo, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                predicates.add(cb.equal(root.get("dataValidade"), dataValidade));
            } catch (DateTimeParseException e) {
                // Ignorar se o termo não for uma data válida
            }

            // Busca por ID
            try {
                int id = Integer.parseInt(termo);
                predicates.add(cb.equal(root.get("id"), id));
            } catch (NumberFormatException e) {
                // Ignorar se o termo não for um número válido
            }

            return cb.or(predicates.toArray(new Predicate[0]));
        };

        return municaoRepository.findAll(spec);
    }
    
    public int buscarTotalMunicoesEmEstoque() {
        List<Municao> municoes = municaoRepository.findAll();
        int total = 0;
        for (Municao municao : municoes) {
            total += municao.getQuantidade();
        }
        return total;
    }
    
    public List<Map<String, Object>> getDadosMunicoes() {
        List<Object[]> dados = municaoRepository.findTipoCalibreQuantidade(); // Consulta personalizada no repositório
        List<Map<String, Object>> dadosMunicoes = new ArrayList<>();

        for (Object[] dado : dados) {
            Map<String, Object> municaoData = new HashMap<>();
            municaoData.put("tipo", dado[0]);
            municaoData.put("calibre", dado[1]);
            municaoData.put("quantidade", dado[2]);
            dadosMunicoes.add(municaoData);
        }

        return dadosMunicoes;
    }

	public Optional<Municao> findById(int id) {
		return municaoRepository.findById(id); 
	}

}