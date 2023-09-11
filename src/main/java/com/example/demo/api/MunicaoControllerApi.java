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

import com.example.demo.entities.Municao;
import com.example.demo.service.MunicaoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/municao/api")
public class MunicaoControllerApi {

    @Autowired
    private MunicaoService municaoService;

    @GetMapping
    public List<Municao> listarMunicoes() {
        return municaoService.getAllMunicoes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Municao> listarMunicao(@PathVariable int id) {
        try {
            Municao municao = municaoService.getMunicao(id);
            return ResponseEntity.ok(municao);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public void cadastrarMunicao(@RequestBody @Valid Municao municao) {
        municaoService.saveMunicao(municao);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Municao> updateMunicao(@PathVariable int id, @RequestBody @Valid Municao municao) {
        try {
            Municao atualizado = municaoService.updateMunicao(municao, id);
            return ResponseEntity.ok(atualizado);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Municao> deleteMunicao(@PathVariable int id) {
        try {
            Municao municao = municaoService.deleteMunicao(id);
            return ResponseEntity.ok(municao);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}