package com.example.demo.view;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entities.Usuario;
import com.example.demo.service.UsuarioService;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/usuario/view")
public class UsuarioControllerView {
    
	@Autowired
    private PasswordEncoder passwordEncode;
	
    @Autowired
    UsuarioService usuarioService;
    
    
    @GetMapping("/listar")
    public ModelAndView listaUsuarios(
        @PageableDefault(size = 8, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        ModelAndView view = new ModelAndView("listaUsuario");
        Page<Usuario> usuarios = usuarioService.getAllUsuarios(pageable);
        view.addObject("usuarios", usuarios);
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
    public ModelAndView saveUsuario(@Valid @ModelAttribute("usuario") Usuario usuario, BindingResult result, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView("cadastroUsuario");

        if (result.hasErrors()) {
            modelAndView.addObject("error", "Erro na validação. Por favor, verifique os campos.");
            return modelAndView;
        }
        usuario.setSenha(passwordEncode.encode(usuario.getSenha()));
        usuarioService.saveUsuario(usuario);
		redirectAttributes.addFlashAttribute("sucesso", "Usuário cadastrado com sucesso!");
		return new ModelAndView("redirect:/usuario/view/listar");
    }

    
    @GetMapping("/atualizar/{id}")
    public ModelAndView formUpdate(@PathVariable("id") int id) throws NotFoundException {
        ModelAndView modelAndView = new ModelAndView("cadastroUsuario");
        Usuario usuario = usuarioService.getUsuario(id);
        modelAndView.addObject("usuario", usuario);
        return modelAndView;
    }
    
    @GetMapping("/buscar")
    public ModelAndView buscarUsuarios(
        @RequestParam(value = "termo", required = false) String termo,
        @PageableDefault(size = 8, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Usuario> pageUsuarios = usuarioService.buscarUsuariosPorFiltro(termo, pageable);
        ModelAndView view = new ModelAndView("listaUsuario");
        view.addObject("usuarios", pageUsuarios);
        return view;
    }
    
}
