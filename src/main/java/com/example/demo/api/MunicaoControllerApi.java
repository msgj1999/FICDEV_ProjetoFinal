package com.example.demo.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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

import com.example.demo.entities.Municao;
import com.example.demo.exceptions.MunicaoAssociadaEntregaException;
import com.example.demo.service.MunicaoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/municao/api")
public class MunicaoControllerApi {

    @Autowired
    private MunicaoService municaoService;

    @GetMapping
    public Page<Municao> listarMunicoes(
        @PageableDefault(size = 8, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return municaoService.getAllMunicoes(pageable);
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
    public ResponseEntity<?> cadastrarMunicao(@RequestBody @Valid Municao municao) {
        municaoService.saveMunicao(municao);
		return ResponseEntity.ok("Munição cadastrada com sucesso!");
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

    /*@DeleteMapping("/{id}")
    public ResponseEntity<Municao> deleteMunicao(@PathVariable int id) {
        try {
            Municao municao = municaoService.deleteMunicao(id);
            return ResponseEntity.ok(municao);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (MunicaoAssociadaEntregaException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Não é possível excluir a munição porque está associada a uma entrega.");
        }
    }*/

    
    @GetMapping("/buscar")
    public ResponseEntity<Page<Municao>> buscarMunicoes(
            @RequestParam(value = "termo", required = false) String termo,
            @PageableDefault(size = 8, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Municao> pageMunicoes = municaoService.buscarMunicoesPorFiltro(termo, pageable);
        
        return ResponseEntity.ok(pageMunicoes);
    }
    
    @GetMapping("/total-estoque")
    public ResponseEntity<Integer> buscarTotalMunicoesEmEstoque() {
        int total = municaoService.buscarTotalMunicoesEmEstoque();
        return ResponseEntity.ok(total);
    }
    
    @GetMapping("/dados-municoes")
    public ResponseEntity<List<Map<String, Object>>> buscarDadosMunicoes() {
        List<Map<String, Object>> dadosMunicoes = municaoService.getDadosMunicoes();
        return ResponseEntity.ok(dadosMunicoes);
    }

}