package com.example.demo.view;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.entities.Usuario;
import com.example.demo.service.UsuarioService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Controller
@RequestMapping("/usuario/view")
public class UsuarioControllerView {
    
	@Autowired
    private PasswordEncoder passwordEncode;
	
    @Autowired
    UsuarioService usuarioService;
    
    @Autowired
    private Validator validator;
    
    @GetMapping("/listar")
    public ModelAndView listaUsuarios() {
        var view = new ModelAndView("listaUsuario");
        view.addObject("usuarios", usuarioService.getAllUsuarios());
        return view;
    }
    
    @GetMapping("/remover/{id}")
    public String removerUsuario(@PathVariable("id") int id) throws NotFoundException {
        usuarioService.deleteUsuario(id);
        return "redirect:/usuario/view/listar";
    }
    
    @GetMapping("/cadastrar")
    public ModelAndView cadastrarUsuario() {
        var view = new ModelAndView("cadastroUsuario");
        view.addObject("usuario", new Usuario(0, null, null, null, null));
        return view;
    }
    
    @PostMapping("/cadastrar")
    public ModelAndView saveUsuario(Usuario usuario) {
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);
        String problemas = "";
        String mensagens = "";
        if (!violations.isEmpty()) {
            problemas = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(" - "));
        } else {
        	usuario.setSenha(passwordEncode.encode(usuario.getSenha()));
            usuarioService.saveUsuario(usuario);
            mensagens = "Salvo com sucesso!";
        }
        ModelAndView modelAndView = new ModelAndView("cadastroUsuario");
        modelAndView.addObject("sucesso", mensagens);
        modelAndView.addObject("error", problemas);
        return modelAndView;
    }
    
    @GetMapping("/atualizar/{id}")
    public ModelAndView formUpdate(@PathVariable("id") int id) throws NotFoundException {
        ModelAndView modelAndView = new ModelAndView("cadastroUsuario");
        Usuario usuario = usuarioService.getUsuario(id);
        modelAndView.addObject("usuario", usuario);
        return modelAndView;
    }
    
    @GetMapping("/buscar")
    public ModelAndView buscarUsuarios(@RequestParam(value = "termo", required = false) String termo) {
        ModelAndView view = new ModelAndView("listaUsuario");
        List<Usuario> usuarios = usuarioService.buscarUsuariosPorFiltro(termo);
        view.addObject("usuarios", usuarios);
        return view;
    }
}
