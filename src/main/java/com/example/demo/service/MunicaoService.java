package com.example.demo.service;

import java.time.LocalDate;
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
    
    public List<Municao> searchMunicoes(String query, LocalDate dataFabricacaoMin, LocalDate dataFabricacaoMax, LocalDate dataValidadeMin, LocalDate dataValidadeMax) {
        List<Municao> allMunicoes = municaoRepository.findAll();

        List<Municao> resultado = allMunicoes.stream()
                .filter(municao -> 
                    containsIgnoreCase(municao.getTipo(), query) ||
                    containsIgnoreCase(municao.getCalibre(), query) ||
                    Integer.toString(municao.getQuantidade()).contains(query) ||
                    Float.toString(municao.getPeso()).contains(query) ||
                    Float.toString(municao.getCoeficienteBalistico()).contains(query) ||
                    (dataFabricacaoMin == null || municao.getDataFabricacao().compareTo(dataFabricacaoMin) >= 0) &&
                    (dataFabricacaoMax == null || municao.getDataFabricacao().compareTo(dataFabricacaoMax) <= 0) &&
                    (dataValidadeMin == null || municao.getDataValidade().compareTo(dataValidadeMin) >= 0) &&
                    (dataValidadeMax == null || municao.getDataValidade().compareTo(dataValidadeMax) <= 0)
                )
                .collect(Collectors.toList());

        return resultado;
    }



    // Método auxiliar para verificar se uma string contém outra ignorando maiúsculas/minúsculas
    private boolean containsIgnoreCase(String source, String query) {
        return source != null && source.toLowerCase().contains(query.toLowerCase());
    }
}