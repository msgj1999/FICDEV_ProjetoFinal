package com.example.demo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.security.UserRole;

import jakarta.persistence.criteria.Predicate;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario getUsuario(int id) throws NotFoundException {
        return usuarioRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public void saveUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    public Usuario updateUsuario(Usuario usuario, int id) throws NotFoundException {
        Usuario atualizado = usuarioRepository.findById(id).orElseThrow(NotFoundException::new);

        atualizado.setNome(usuario.getNome());
        atualizado.setEmail(usuario.getEmail());
        atualizado.setSenha(usuario.getSenha());

        usuarioRepository.save(atualizado);
        return atualizado;
    }

    public Usuario deleteUsuario(int id) throws NotFoundException {
        Usuario deletado = usuarioRepository.findById(id).orElseThrow(NotFoundException::new);
        usuarioRepository.delete(deletado);
        return deletado;
    }
    
    public List<Usuario> buscarUsuariosPorFiltro(String termo) {
        Specification<Usuario> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.like(cb.lower(root.get("nome")), "%" + termo.toLowerCase() + "%"));

            predicates.add(cb.like(cb.lower(root.get("email")), "%" + termo.toLowerCase() + "%"));

            // Busca por ID
            try {
                int id = Integer.parseInt(termo);
                predicates.add(cb.equal(root.get("id"), id));
            } catch (NumberFormatException e) {
                // Ignorar se o termo não for um número válido
            }
            
            // Busca por Role (se o termo corresponder a um UserRole válido)
            if (Arrays.stream(UserRole.values()).anyMatch(role -> role.name().equalsIgnoreCase(termo))) {
                predicates.add(cb.equal(root.get("role"), UserRole.valueOf(termo.toUpperCase())));
            }


            return cb.or(predicates.toArray(new Predicate[0]));
        };

        return usuarioRepository.findAll(spec);
    }
    
    public long countUsuarios() {
        return usuarioRepository.count();
    }

}