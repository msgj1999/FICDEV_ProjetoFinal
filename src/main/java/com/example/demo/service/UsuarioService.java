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
import org.springframework.stereotype.Service;

import com.example.demo.entities.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.security.UserRole;

import jakarta.persistence.criteria.Predicate;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /*public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAllOrderedById();
    }*/

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
    
    public Page<Usuario> buscarUsuariosPorFiltro(String termo, Pageable pageable) {
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

        return usuarioRepository.findAll(spec, pageable);
    }
    
    public long countUsuarios() {
        return usuarioRepository.count();
    }
    
    public Page<Usuario> getAllUsuarios(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }
    
    public Usuario getUsuarioLogado() throws NotFoundException {
        // Obtém o contexto de segurança atual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verifica se o usuário está autenticado
        if (authentication != null && authentication.isAuthenticated()) {
            // O nome de usuário (email) do usuário logado
            String nomeUsuarioLogado = authentication.getName();

            // Use o nome de usuário para buscar o usuário correspondente no seu repositório
            return usuarioRepository.findByLogin(nomeUsuarioLogado)
                .orElseThrow(() -> new NotFoundException());
        }

        // Se o usuário não estiver autenticado, retorne null ou trate conforme necessário
        return null;
    }

}