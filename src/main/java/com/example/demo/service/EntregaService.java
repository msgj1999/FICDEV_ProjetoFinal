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

import com.example.demo.entities.Entrega;
import com.example.demo.entities.Municao;
import com.example.demo.repository.EntregaRepository;

import jakarta.persistence.criteria.Predicate;

@Service
public class EntregaService {

    @Autowired
    private EntregaRepository entregaRepository;
    
    @Autowired
    private MunicaoService municaoService;

    /*public List<Entrega> getAllEntregas() {
        return entregaRepository.findAll();
    }*/

    public Entrega getEntrega(int id) throws NotFoundException {
        return entregaRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public void saveEntrega(Entrega entrega) {
        entregaRepository.save(entrega);
    }

    public Entrega updateEntrega(Entrega entrega, int id) throws NotFoundException {
        Entrega atualizada = entregaRepository.findById(id).orElseThrow(NotFoundException::new);

        atualizada.setNomePolicial(entrega.getNomePolicial());
        atualizada.setQuantidade(entrega.getQuantidade());
        atualizada.setDataEntrega(entrega.getDataEntrega());
        atualizada.setObservacoes(entrega.getObservacoes());

        entregaRepository.save(atualizada);
        return atualizada;
    }

    public Entrega deleteEntrega(int id) throws NotFoundException {
        Entrega deletada = entregaRepository.findById(id).orElseThrow(NotFoundException::new);
        entregaRepository.delete(deletada);
        return deletada;
    }
    
    public void cadastrarEntrega(Entrega entrega) throws NotFoundException {
        Municao municao = municaoService.findById(entrega.getMunicao().getId()).orElseThrow();
        
        if (municao != null && entrega.getQuantidade() <= municao.getQuantidade()) {
            try {
                municao.setQuantidade(municao.getQuantidade() - entrega.getQuantidade()); // Reduza a quantidade de munição
                municaoService.saveMunicao(municao); // Atualize a quantidade de munição

                entregaRepository.save(entrega); // Salve a entrega
            } catch (Exception e) {
                // Lide com qualquer exceção de salvamento aqui
                throw new NotFoundException();
            }
        } else {
            throw new NotFoundException();
        }
    }
    
    public Page<Entrega> buscarEntregasPorFiltro(String termo, Pageable pageable) {
        Specification<Entrega> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Busca por munição (tipo + calibre)
            predicates.add(cb.like(cb.lower(root.get("municao").get("tipo")), "%" + termo.toLowerCase() + "%"));
            predicates.add(cb.like(cb.lower(root.get("municao").get("calibre")), "%" + termo.toLowerCase() + "%"));

            // Busca por quantidade
            try {
                int quantidade = Integer.parseInt(termo);
                predicates.add(cb.equal(root.get("quantidade"), quantidade));
            } catch (NumberFormatException e) {
                // Ignorar se o termo não for um número válido
            }

            // Busca por data de entrega
            try {
                LocalDate dataEntrega = LocalDate.parse(termo, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                predicates.add(cb.equal(root.get("dataEntrega"), dataEntrega));
            } catch (DateTimeParseException e) {
                // Ignorar se o termo não for uma data válida
            }

            // Busca por observações
            predicates.add(cb.like(cb.lower(root.get("observacoes")), "%" + termo.toLowerCase() + "%"));

            // Busca por ID
            try {
                int id = Integer.parseInt(termo);
                predicates.add(cb.equal(root.get("id"), id));
            } catch (NumberFormatException e) {
                // Ignorar se o termo não for um número válido
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