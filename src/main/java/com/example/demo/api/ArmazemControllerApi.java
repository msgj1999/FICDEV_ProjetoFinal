package com.example.demo.api;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Armazem;
import com.example.demo.service.ArmazemService;
import org.springframework.web.bind.annotation.RequestParam;


import jakarta.validation.Valid;

@RestController
@RequestMapping("/armazem/api")
public class ArmazemControllerApi {

    @Autowired
    private ArmazemService armazemService;

    @GetMapping
    public List<Armazem> listarArmazens() {
        return armazemService.getAllArmazens();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Armazem> listarArmazem(@PathVariable int id) {
        try {
            Armazem armazem = armazemService.getArmazem(id);
            return ResponseEntity.ok(armazem);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public void cadastrarArmazem(@RequestBody @Valid Armazem armazem) {
        armazemService.saveArmazem(armazem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Armazem> updateArmazem(@PathVariable int id, @RequestBody @Valid Armazem armazem) {
        try {
            Armazem atualizado = armazemService.updateArmazem(armazem, id);
            return ResponseEntity.ok(atualizado);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Armazem> deleteArmazem(@PathVariable int id) {
        try {
            Armazem armazem = armazemService.deleteArmazem(id);
            return ResponseEntity.ok(armazem);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
}