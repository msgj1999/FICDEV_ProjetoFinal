package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

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
        municaoRepository.save(atualizada);
        return atualizada;
    }

    public Municao deleteMunicao(int id) throws NotFoundException {
        Municao deletada = municaoRepository.findById(id).orElseThrow(NotFoundException::new);
        municaoRepository.delete(deletada);
        return deletada;
    }
    
    public List<Municao> searchMunicoes(String query) {
        List<Municao> allMunicoes = municaoRepository.findAll();

        // Filtrar a lista de todas as munições com base na consulta
        List<Municao> resultado = allMunicoes.stream()
                .filter(municao -> 
                    containsIgnoreCase(municao.getTipo(), query) ||
                    containsIgnoreCase(municao.getCalibre(), query) ||
                    containsIgnoreCase(municao.getPericulosidade(), query)
                )
                .collect(Collectors.toList());

        return resultado;
    }

    // Método auxiliar para verificar se uma string contém outra ignorando maiúsculas/minúsculas
    private boolean containsIgnoreCase(String source, String query) {
        return source != null && source.toLowerCase().contains(query.toLowerCase());
    }
}