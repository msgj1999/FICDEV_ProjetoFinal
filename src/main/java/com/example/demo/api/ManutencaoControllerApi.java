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

import com.example.demo.entities.Manutencao;
import com.example.demo.service.ManutencaoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/manutencao/api")
public class ManutencaoControllerApi {

    @Autowired
    private ManutencaoService manutencaoService;

    @GetMapping
    public Page<Manutencao> listarManutencoes(
        @PageableDefault(size = 8, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return manutencaoService.getAllManutencoes(pageable);
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
    public ResponseEntity<?> cadastrarManutencao(@RequestBody @Valid Manutencao manutencao) {
        manutencaoService.saveManutencao(manutencao);
		return ResponseEntity.ok("Manutenção cadastrada com sucesso!");
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
    
    @GetMapping("/buscar")
    public ResponseEntity<Page<Manutencao>> buscarManutencoes(
            @RequestParam(value = "termo", required = false) String termo,
            @PageableDefault(size = 8, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Manutencao> pageManutencoes = manutencaoService.buscarManutencoesPorFiltro(termo, pageable);
        
        return ResponseEntity.ok(pageManutencoes);
    }

    @GetMapping("/total-manutencoes")
    public int getTotalManutencoes() {
        return manutencaoService.buscarTotalManutencoes();
    }

}