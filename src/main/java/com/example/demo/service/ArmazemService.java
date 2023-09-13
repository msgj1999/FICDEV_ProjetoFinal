package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Armazem;
import com.example.demo.entities.Municao;
import com.example.demo.repository.ArmazemRepository;

@Service
public class ArmazemService {

    @Autowired
    private ArmazemRepository armazemRepository;
    
    public List<Armazem> getAllArmazens() {
        return armazemRepository.findAll();
    }

    public Armazem getArmazem(int id) throws NotFoundException {
        return armazemRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public void saveArmazem(Armazem armazem) {
        armazemRepository.save(armazem);
    }

    public Armazem updateArmazem(Armazem armazem, int id) throws NotFoundException {
        Armazem atualizado = armazemRepository.findById(id).orElseThrow(NotFoundException::new);

        atualizado.setQuantidade(armazem.getQuantidade());
        atualizado.setDataFabricacao(armazem.getDataFabricacao());
        atualizado.setDataValidade(armazem.getDataValidade());

        armazemRepository.save(atualizado);
        return atualizado;
    }

    public Armazem deleteArmazem(int id) throws NotFoundException {
        Armazem deletado = armazemRepository.findById(id).orElseThrow(NotFoundException::new);
        armazemRepository.delete(deletado);
        return deletado;
    }
    
    public Armazem obterArmazemPorMunicao(Municao municao) {
        return armazemRepository.findByMunicao(municao);
    }


}