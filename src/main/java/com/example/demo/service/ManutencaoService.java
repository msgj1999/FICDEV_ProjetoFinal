package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Manutencao;
import com.example.demo.repository.ManutencaoRepository;

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
}