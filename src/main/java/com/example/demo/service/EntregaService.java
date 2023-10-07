package com.example.demo.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.example.demo.entities.Entrega;
import com.example.demo.entities.Municao;
import com.example.demo.entities.Usuario;
import com.example.demo.repository.EntregaRepository;

import jakarta.persistence.criteria.Predicate;

@Service
public class EntregaService {

    @Autowired
    private EntregaRepository entregaRepository;
    
    @Autowired
    private MunicaoService municaoService;

    @Autowired
    private UserActionService userActionService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    public Entrega getEntrega(int id) throws NotFoundException {
        return entregaRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public void saveEntrega(Entrega entrega) throws NotFoundException {
        
        entregaRepository.save(entrega);
    }

    public Entrega updateEntrega(Entrega entrega, int id) throws NotFoundException {
        Entrega atualizada = entregaRepository.findById(id).orElseThrow(NotFoundException::new);

        atualizada.setNomePolicial(entrega.getNomePolicial());
        atualizada.setQuantidade(entrega.getQuantidade());
        atualizada.setDataEntrega(entrega.getDataEntrega());
        atualizada.setObservacoes(entrega.getObservacoes());
        
        Usuario usuarioLogado = usuarioService.getUsuarioLogado2(); 
        String username = usuarioLogado != null ? usuarioLogado.getNome() : "Usuário Desconhecido";
        userActionService.registerUserAction(username, "Editou uma entrega com ID: " + id);

        entregaRepository.save(atualizada);
        return atualizada;
    }

    public Entrega deleteEntrega(int id) throws NotFoundException {
        Entrega deletada = entregaRepository.findById(id).orElseThrow(NotFoundException::new);
        
        Usuario usuarioLogado = usuarioService.getUsuarioLogado2(); 
        String username = usuarioLogado != null ? usuarioLogado.getNome() : "Usuário Desconhecido";
        userActionService.registerUserAction(username, "Excluiu uma entrega com ID: " + id);
        
        entregaRepository.delete(deletada);
        return deletada;
    }
    
    public void cadastrarEntrega(Entrega entrega, BindingResult result) throws NotFoundException {
        Municao municao = municaoService.findById(entrega.getMunicao().getId()).orElseThrow();

        if (municao != null && entrega.getQuantidade() <= municao.getQuantidade()) {
            try {
                municao.setQuantidade(municao.getQuantidade() - entrega.getQuantidade());
                entregaRepository.save(entrega);

                Usuario usuarioLogado = usuarioService.getUsuarioLogado2();
                String username = usuarioLogado.getNome();
                userActionService.registerUserAction(username, "Cadastrou uma nova entrega");
            } catch (Exception e) {
                throw new NotFoundException();
            }
        } else {
            result.rejectValue("quantidade", "error.entrega", "Quantidade insuficiente em estoque");
        }
    }


    
    public Page<Entrega> buscarEntregasPorFiltro(String termo, Pageable pageable) {
        Specification<Entrega> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.like(cb.lower(root.get("municao").get("tipo")), "%" + termo.toLowerCase() + "%"));
            predicates.add(cb.like(cb.lower(root.get("municao").get("calibre")), "%" + termo.toLowerCase() + "%"));

            try {
                int quantidade = Integer.parseInt(termo);
                predicates.add(cb.equal(root.get("quantidade"), quantidade));
            } catch (NumberFormatException e) {
            }

            try {
                LocalDate dataEntrega = LocalDate.parse(termo, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                predicates.add(cb.equal(root.get("dataEntrega"), dataEntrega));
            } catch (DateTimeParseException e) {
            }

            predicates.add(cb.like(cb.lower(root.get("observacoes")), "%" + termo.toLowerCase() + "%"));

            try {
                int id = Integer.parseInt(termo);
                predicates.add(cb.equal(root.get("id"), id));
            } catch (NumberFormatException e) {
            }

            return cb.or(predicates.toArray(new Predicate[0]));
        };

        return entregaRepository.findAll(spec, pageable);
    }


    public int buscarTotalEntregas() {
        List<Entrega> entregas = entregaRepository.findAll();
        return entregas.size();
    }

    public Page<Entrega> getAllEntregas(Pageable pageable) {
        return entregaRepository.findAll(pageable);
    }
}