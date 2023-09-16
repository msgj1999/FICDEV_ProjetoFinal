package com.example.demo.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Manutencao;
import com.example.demo.repository.ManutencaoRepository;

import jakarta.persistence.criteria.Predicate;

@Service
public class ManutencaoService {

    @Autowired
    private ManutencaoRepository manutencaoRepository;

    public List<Manutencao> getAllManutencoes() {
        return manutencaoRepository.findAll();
    }

    public Manutencao getManutencao(int id) throws NotFoundException {
        return manutencaoRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public void saveManutencao(Manutencao manutencao) {
        manutencaoRepository.save(manutencao);
    }

    public Manutencao updateManutencao(Manutencao manutencao, int id) throws NotFoundException {
        Manutencao atualizada = manutencaoRepository.findById(id).orElseThrow(NotFoundException::new);

        atualizada.setStatus(manutencao.getStatus());
        atualizada.setDataManutencao(manutencao.getDataManutencao());

        manutencaoRepository.save(atualizada);
        return atualizada;
    }
    public Manutencao deleteManutencao(int id) throws NotFoundException {
        Manutencao deletada = manutencaoRepository.findById(id).orElseThrow(NotFoundException::new);
        manutencaoRepository.delete(deletada);
        return deletada;
    }
    
    public List<Manutencao> buscarManutencoesPorFiltro(String termo) {
        Specification<Manutencao> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.like(cb.lower(root.get("status")), "%" + termo.toLowerCase() + "%"));

            // Busca por data de Manutenção
            try {
                LocalDate dataManutencao = LocalDate.parse(termo, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                predicates.add(cb.equal(root.get("dataManutencao"), dataManutencao));
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

        return manutencaoRepository.findAll(spec);
    }
    
    public int buscarTotalManutencoes() {
        List<Manutencao> manutencoes = manutencaoRepository.findAll();
        return manutencoes.size();
    }

}