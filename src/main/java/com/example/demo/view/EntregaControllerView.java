package com.example.demo.view;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.data.domain.Sort;


import com.example.demo.entities.Entrega;
import com.example.demo.service.EntregaService;
import com.example.demo.service.MunicaoService;

import jakarta.validation.ConstraintViolation;
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
    public ModelAndView listaEntregas(
        @PageableDefault(size = 8, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        ModelAndView view = new ModelAndView("listaEntrega");
        Page<Entrega> entregas = entregaService.getAllEntregas(pageable);
        view.addObject("entregas", entregas);
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
        view.addObject("entrega", new Entrega());
        view.addObject("municoes", municaoService.getAllMunicoes());
        return view;
    }

    @PostMapping("/cadastrar")
    public ModelAndView saveEntrega(Entrega entrega) {
        Set<ConstraintViolation<Entrega>> violations = validator.validate(entrega);
        String problemas = "";
        String mensagens = "";

        if (!violations.isEmpty()) {
            problemas = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(" - "));
        } else {
            try {
            	entregaService.cadastrarEntrega(entrega); // Chame o método de serviço aqui
                mensagens = "Salvo com sucesso!";
            } catch (NotFoundException e) {
                problemas = e.getMessage();
            }
        }
        ModelAndView modelAndView = new ModelAndView("cadastroEntrega");
        modelAndView.addObject("sucesso", mensagens);
        modelAndView.addObject("error", problemas);
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
    
    @GetMapping("/buscar")
    public ModelAndView buscarEntregas(
        @RequestParam(value = "termo", required = false) String termo,
        @PageableDefault(size = 8, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Entrega> pageEntregas = entregaService.buscarEntregasPorFiltro(termo, pageable);
        ModelAndView view = new ModelAndView("listaEntrega");
        view.addObject("entregas", pageEntregas);
        return view;
    }

}
