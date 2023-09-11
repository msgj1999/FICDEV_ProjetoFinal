package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Usuario;
import com.example.demo.repository.UsuarioRepository;

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
        atualizado.setCargo(usuario.getCargo());

        usuarioRepository.save(atualizado);
        return atualizado;
    }

    public Usuario deleteUsuario(int id) throws NotFoundException {
        Usuario deletado = usuarioRepository.findById(id).orElseThrow(NotFoundException::new);
        usuarioRepository.delete(deletado);
        return deletado;
    }
}