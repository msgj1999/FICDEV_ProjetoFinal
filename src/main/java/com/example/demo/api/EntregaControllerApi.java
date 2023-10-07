package com.example.demo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public Page<Entrega> listarEntregas(
        @PageableDefault(size = 8, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return entregaService.getAllEntregas(pageable);
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
            entregaService.cadastrarEntrega(entrega, null);
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
    public ResponseEntity<Page<Entrega>> buscarEntregas(
            @RequestParam(value = "termo", required = false) String termo,
            @PageableDefault(size = 8, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Entrega> pageEntregas = entregaService.buscarEntregasPorFiltro(termo, pageable);
        
        return ResponseEntity.ok(pageEntregas);
    }

    
    @GetMapping("/total-entregas")
    public ResponseEntity<Integer> buscarTotalEntregas() {
        int total = entregaService.buscarTotalEntregas();
        return ResponseEntity.ok(total);
    }
    
}