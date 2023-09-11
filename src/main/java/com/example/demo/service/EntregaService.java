package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Entrega;
import com.example.demo.repository.EntregaRepository;

@Service
public class EntregaService {

    @Autowired
    private EntregaRepository entregaRepository;

    public List<Entrega> getAllEntregas() {
        return entregaRepository.findAll();
    }

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
}