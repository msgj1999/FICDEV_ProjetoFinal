package com.example.demo.view;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.entities.*;
import com.example.demo.entities.Armazem;
import com.example.demo.service.ArmazemService;
import com.example.demo.service.MunicaoService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Controller
@RequestMapping("/armazem/view")
public class ArmazemControllerView {

    @Autowired
    ArmazemService armazemService;
    
    @Autowired
    MunicaoService municaoService;
    
	@Autowired
	private Validator validator;

    @GetMapping("/listar")
    public ModelAndView listaArmazens() {
        var view = new ModelAndView("listaArmazem");
        view.addObject("armazens", armazemService.getAllArmazens());
        return view;
    }

    @GetMapping("/remover/{id}")
    public String removerArmazem(@PathVariable("id") int id) throws NotFoundException {
        armazemService.deleteArmazem(id);
        return "redirect:/armazem/view/listar";
    }

    @GetMapping("/cadastrar")
    public ModelAndView cadastrarArmazem() {
        var view = new ModelAndView("cadastroArmazem");
        view.addObject("armazem", new Armazem(0, 0, null, null, null));
        // Recupere a lista de munições disponíveis
        List<Municao> municoes = municaoService.getAllMunicoes();
        view.addObject("municoes", municoes);
        
        return view;
    }

    @PostMapping("/cadastrar")
    public ModelAndView saveArmazem(Armazem armazem) {
        Set<ConstraintViolation<Armazem>> violations = validator.validate(armazem);
        String problemas = "";
        String mensagens = "";
        if (!violations.isEmpty()) {
            problemas = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(" - "));
        } else {
            armazemService.saveArmazem(armazem);
            mensagens = "Salvo com sucesso!";
        }
        ModelAndView modelAndView = new ModelAndView("cadastroArmazem");
        modelAndView.addObject("sucesso", mensagens);
        modelAndView.addObject("error", problemas);
        return modelAndView;
    }

    @GetMapping("/atualizar/{id}")
    public ModelAndView formUpdate(@PathVariable("id") int id) throws NotFoundException {
        ModelAndView modelAndView = new ModelAndView("cadastroArmazem");
        Armazem armazem = armazemService.getArmazem(id);
        modelAndView.addObject("armazem", armazem);
        return modelAndView;
    }
    
}
