package com.example.demo.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.dto.UsuarioDTO;
import com.example.demo.service.UsuarioService;

@Controller
public class PerfilControllerView {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/perfil")
    public String perfil(Model model) {
        try {
            UsuarioDTO usuarioDTO = usuarioService.getUsuarioLogado();
            model.addAttribute("usuario", usuarioDTO);
        } catch (NotFoundException e) {
        }
        return "perfil";
    }
}