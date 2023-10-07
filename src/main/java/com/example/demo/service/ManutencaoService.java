package com.example.demo.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Manutencao;
import com.example.demo.entities.Usuario;
import com.example.demo.repository.ManutencaoRepository;

import jakarta.persistence.criteria.Predicate;

@Service
public class ManutencaoService {

    @Autowired
    private ManutencaoRepository manutencaoRepository;
    
    @Autowired
    private UserActionService userActionService;
    
    @Autowired
    private UsuarioService usuarioService;

    public Manutencao getManutencao(int id) throws NotFoundException {
        return manutencaoRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public void saveManutencao(Manutencao manutencao) throws NotFoundException {
    	
        Usuario usuarioLogado = usuarioService.getUsuarioLogado2(); 
        String username = usuarioLogado.getNome();
        userActionService.registerUserAction(username, "Cadastrou uma nova manutenção");
        
        manutencaoRepository.save(manutencao);
    }

    public Manutencao updateManutencao(Manutencao manutencao, int id) throws NotFoundException {
        Manutencao atualizada = manutencaoRepository.findById(id).orElseThrow(NotFoundException::new);

        atualizada.setStatus(manutencao.getStatus());
        atualizada.setDataManutencao(manutencao.getDataManutencao());
        
        Usuario usuarioLogado = usuarioService.getUsuarioLogado2(); 
        String username = usuarioLogado != null ? usuarioLogado.getNome() : "Usuário Desconhecido";
        userActionService.registerUserAction(username, "Editou uma manutenção com ID: " + id);

        manutencaoRepository.save(atualizada);
        return atualizada;
    }
    public Manutencao deleteManutencao(int id) throws NotFoundException {
        Manutencao deletada = manutencaoRepository.findById(id).orElseThrow(NotFoundException::new);
        
        Usuario usuarioLogado = usuarioService.getUsuarioLogado2(); 
        String username = usuarioLogado != null ? usuarioLogado.getNome() : "Usuário Desconhecido";
        userActionService.registerUserAction(username, "Excluiu uma manutenção com ID: " + id);
        
        manutencaoRepository.delete(deletada);
        return deletada;
    }
    
    public Page<Manutencao> buscarManutencoesPorFiltro(String termo, Pageable pageable) {
        Specification<Manutencao> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.like(cb.lower(root.get("status")), "%" + termo.toLowerCase() + "%"));

            try {
                LocalDate dataManutencao = LocalDate.parse(termo, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                predicates.add(cb.equal(root.get("dataManutencao"), dataManutencao));
            } catch (DateTimeParseException e) {
            }

            try {
                int id = Integer.parseInt(termo);
                predicates.add(cb.equal(root.get("id"), id));
            } catch (NumberFormatException e) {
            }

            return cb.or(predicates.toArray(new Predicate[0]));
        };

        return manutencaoRepository.findAll(spec, pageable);
    }
    
    public int buscarTotalManutencoes() {
        List<Manutencao> manutencoes = manutencaoRepository.findAll();
        return manutencoes.size();
    }
    
    public Page<Manutencao> getAllManutencoes(Pageable pageable) {
        return manutencaoRepository.findAll(pageable);
    }


}