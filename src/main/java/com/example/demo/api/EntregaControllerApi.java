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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Entrega;
import com.example.demo.service.EntregaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/entrega/api")
public class EntregaControllerApi {

    @Autowired
    private EntregaService entregaService;

    @GetMapping
    public List<Entrega> listarEntregas() {
        return entregaService.getAllEntregas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Entrega> listarEntrega(@PathVariable int id) {
        try {
            Entrega entrega = entregaService.getEntrega(id);
            return ResponseEntity.ok(entrega);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> cadastrarEntrega(@RequestBody @Valid Entrega entrega) {
        try {
            entregaService.cadastrarEntrega(entrega);
            return ResponseEntity.ok("Entrega cadastrada com sucesso!");
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Entrega> updateEntrega(@PathVariable int id, @RequestBody @Valid Entrega entrega) {
        try {
            Entrega atualizado = entregaService.updateEntrega(entrega, id);
            return ResponseEntity.ok(atualizado);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Entrega> deleteEntrega(@PathVariable int id) {
        try {
            Entrega entrega = entregaService.deleteEntrega(id);
            return ResponseEntity.ok(entrega);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<List<Entrega>> buscarEntregas(@RequestParam(value = "termo", required = false) String termo) {
        List<Entrega> entregas = entregaService.buscarEntregasPorFiltro(termo);
        return ResponseEntity.ok(entregas);
    }
    
    @GetMapping("/total-entregas")
    public ResponseEntity<Integer> buscarTotalEntregas() {
        int total = entregaService.buscarTotalEntregas();
        return ResponseEntity.ok(total);
    }
    
}