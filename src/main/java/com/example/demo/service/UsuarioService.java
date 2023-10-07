package com.example.demo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.UsuarioDTO;
import com.example.demo.entities.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.security.UserRole;

import jakarta.persistence.criteria.Predicate;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private UserActionService userActionService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    

    /*public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAllOrderedById();
    }*/

    public Usuario getUsuario(int id) throws NotFoundException {
        return usuarioRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public void saveUsuario(Usuario usuario) throws NotFoundException {
    	
        Usuario usuarioLogado = getUsuarioLogado2(); 
        String username = usuarioLogado.getNome();
        userActionService.registerUserAction(username, "Cadastrou um novo usuário");
        
        usuarioRepository.save(usuario);
    }

    public Usuario updateUsuario(Usuario usuario, int id) throws NotFoundException {
        Usuario atualizado = usuarioRepository.findById(id).orElseThrow(NotFoundException::new);

        atualizado.setNome(usuario.getNome());
        atualizado.setEmail(usuario.getEmail());
        
        if (!usuario.getSenha().isEmpty()) {
            atualizado.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }
        
        atualizado.setRole(usuario.getRole());
        
        Usuario usuarioLogado = getUsuarioLogado2(); 
        String username = usuarioLogado != null ? usuarioLogado.getNome() : "Usuário Desconhecido";
        userActionService.registerUserAction(username, "Editou um usuário com ID: " + id);

        usuarioRepository.save(atualizado);
        return atualizado;
    }

    public Usuario deleteUsuario(int id) throws NotFoundException {
        Usuario deletado = usuarioRepository.findById(id).orElseThrow(NotFoundException::new);
        
        Usuario usuarioLogado = getUsuarioLogado2(); 
        String username = usuarioLogado != null ? usuarioLogado.getNome() : "Usuário Desconhecido";
        userActionService.registerUserAction(username, "Excluiu um usuário com ID: " + id);
        
        usuarioRepository.delete(deletado);
        return deletado;
    }
    
    public Page<Usuario> buscarUsuariosPorFiltro(String termo, Pageable pageable) {
        Specification<Usuario> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.like(cb.lower(root.get("nome")), "%" + termo.toLowerCase() + "%"));

            predicates.add(cb.like(cb.lower(root.get("email")), "%" + termo.toLowerCase() + "%"));

            try {
                int id = Integer.parseInt(termo);
                predicates.add(cb.equal(root.get("id"), id));
            } catch (NumberFormatException e) {
            }
            
            if (Arrays.stream(UserRole.values()).anyMatch(role -> role.name().equalsIgnoreCase(termo))) {
                predicates.add(cb.equal(root.get("role"), UserRole.valueOf(termo.toUpperCase())));
            }


            return cb.or(predicates.toArray(new Predicate[0]));
        };

        return usuarioRepository.findAll(spec, pageable);
    }
    
    public long countUsuarios() {
        return usuarioRepository.count();
    }
    
    public Page<Usuario> getAllUsuarios(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }
    
    public Usuario getUsuarioLogado2() throws NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String nomeUsuarioLogado = authentication.getName();

            return usuarioRepository.findByLogin(nomeUsuarioLogado)
                .orElseThrow(() -> new NotFoundException());
        }

        return null;
    }
    
    
    public UsuarioDTO getUsuarioLogado() throws NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String nomeUsuarioLogado = authentication.getName();
            Usuario usuario = usuarioRepository.findByLogin(nomeUsuarioLogado)
                    .orElseThrow(() -> new NotFoundException());

            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setId(usuario.getId());
            usuarioDTO.setNome(usuario.getNome());
            usuarioDTO.setEmail(usuario.getEmail());
            usuarioDTO.setRole(usuario.getRole());

            return usuarioDTO;
        }

        return null;
    }


}