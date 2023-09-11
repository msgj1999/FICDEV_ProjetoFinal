package com.example.demo.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Manutencao;
import com.example.demo.service.ManutencaoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/manutencao/api")
public class ManutencaoControllerApi {

    @Autowired
    private ManutencaoService manutencaoService;

    @GetMapping
    public List<Manutencao> listarManutencoes() {
        return manutencaoService.getAllManutencoes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Manutencao> listarManutencao(@PathVariable int id) {
        try {
            Manutencao manutencao = manutencaoService.getManutencao(id);
            return ResponseEntity.ok(manutencao);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public void cadastrarManutencao(@RequestBody @Valid Manutencao manutencao) {
        manutencaoService.saveManutencao(manutencao);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Manutencao> updateManutencao(@PathVariable int id, @RequestBody @Valid Manutencao manutencao) {
        try {
            Manutencao atualizado = manutencaoService.updateManutencao(manutencao, id);
            return ResponseEntity.ok(atualizado);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Manutencao> deleteManutencao(@PathVariable int id) {
        try {
            Manutencao manutencao = manutencaoService.deleteManutencao(id);
            return ResponseEntity.ok(manutencao);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}