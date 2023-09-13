package com.example.demo.view;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.entities.Entrega;
import com.example.demo.service.EntregaService;
import com.example.demo.service.MunicaoService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;

@Controller
@RequestMapping("/entrega/view")
public class EntregaControllerView {

    @Autowired
    EntregaService entregaService;

    @Autowired
    MunicaoService municaoService;

    @Autowired
    private Validator validator;

    @GetMapping("/listar")
    public ModelAndView listaEntregas() {
        var view = new ModelAndView("listaEntrega");
        view.addObject("entregas", entregaService.getAllEntregas());
        return view;
    }

    @GetMapping("/remover/{id}")
    public String removerEntrega(@PathVariable("id") int id) throws NotFoundException {
        entregaService.deleteEntrega(id);
        return "redirect:/entrega/view/listar";
    }

    @GetMapping("/cadastrar")
    public ModelAndView cadastrarEntrega() {
        var view = new ModelAndView("cadastroEntrega");
        view.addObject("entrega", new Entrega(0, null, 0, null, null, null));
        view.addObject("municoes", municaoService.getAllMunicoes());
        return view;
    }

    @PostMapping("/cadastrar")
    public ModelAndView saveEntrega(@Valid Entrega entrega, BindingResult result) {
        if (result.hasErrors()) {
            // Lida com erros de validação aqui, se necessário
        } else {
            try {
                entregaService.cadastrarEntrega(entrega);
                return new ModelAndView("redirect:/entrega/view/listar");
            } catch (NotFoundException e) {
                // Trate a exceção de quantidade insuficiente no armazém aqui
            }
        }

        // Se algo der errado, retorne para a página de cadastro com mensagens de erro
        ModelAndView modelAndView = new ModelAndView("cadastroEntrega");
        modelAndView.addObject("entrega", entrega);
        modelAndView.addObject("municoes", municaoService.getAllMunicoes());
        modelAndView.addObject("erro", "Erro ao cadastrar a entrega. Verifique a quantidade disponível no armazém.");
        return modelAndView;
    }

    @GetMapping("/atualizar/{id}")
    public ModelAndView formUpdate(@PathVariable("id") int id) throws NotFoundException {
        ModelAndView modelAndView = new ModelAndView("cadastroEntrega");
        Entrega entrega = entregaService.getEntrega(id);
        modelAndView.addObject("entrega", entrega);
        modelAndView.addObject("municoes", municaoService.getAllMunicoes());
        return modelAndView;
    }
}
