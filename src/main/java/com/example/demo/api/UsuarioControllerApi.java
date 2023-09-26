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

import com.example.demo.entities.Usuario;
import com.example.demo.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuario/api")
public class UsuarioControllerApi {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public Page<Usuario> listarUsuarios(
        @PageableDefault(size = 8, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return usuarioService.getAllUsuarios(pageable);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> listarUsuario(@PathVariable int id) {
        try {
            Usuario usuario = usuarioService.getUsuario(id);
            return ResponseEntity.ok(usuario);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> cadastrarUsuario(@RequestBody @Valid Usuario usuario) {
        usuarioService.saveUsuario(usuario);
		return ResponseEntity.ok("Usuário cadastrada com sucesso!");
    }


    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable int id, @RequestBody @Valid Usuario usuario) {
        try {
            Usuario atualizado = usuarioService.updateUsuario(usuario, id);
            return ResponseEntity.ok(atualizado);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Usuario> deleteUsuario(@PathVariable int id) {
        try {
            Usuario usuario = usuarioService.deleteUsuario(id);
            return ResponseEntity.ok(usuario);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<Page<Usuario>> buscarUsuarios(
            @RequestParam(value = "termo", required = false) String termo,
            @PageableDefault(size = 8, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Usuario> pageUsuarios = usuarioService.buscarUsuariosPorFiltro(termo, pageable);
        
        return ResponseEntity.ok(pageUsuarios);
    }
    
    @GetMapping("/total-usuarios")
    public ResponseEntity<Long> contarUsuarios() {
        long totalUsuarios = usuarioService.countUsuarios();
        return ResponseEntity.ok(totalUsuarios);
    }
    

    @GetMapping("/nome-usuario-logado")
    public ResponseEntity<String> getNomeUsuarioLogado() {
        try {
            Usuario usuarioLogado = usuarioService.getUsuarioLogado2();
            if (usuarioLogado != null) {
                String nomeUsuarioLogado = usuarioLogado.getNome();
                return ResponseEntity.ok(nomeUsuarioLogado);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}