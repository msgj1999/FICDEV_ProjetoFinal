package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Municao;
import com.example.demo.repository.MunicaoRepository;

@Service
public class MunicaoService {

    @Autowired
    private MunicaoRepository municaoRepository;

    public List<Municao> getAllMunicoes() {
        return municaoRepository.findAll();
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
        atualizada.setPericulosidade(municao.getPericulosidade());
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
}