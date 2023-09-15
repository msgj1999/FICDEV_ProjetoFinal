package com.example.demo.view;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.entities.Manutencao;
import com.example.demo.service.ManutencaoService;
import com.example.demo.service.MunicaoService;

import jakarta.validation.*;

@Controller
@RequestMapping("/manutencao/view")
public class ManutencaoControllerView {

    @Autowired
    ManutencaoService manutencaoService;

    @Autowired
    MunicaoService municaoService;
    
	@Autowired
	private Validator validator;

    @GetMapping("/listar")
    public ModelAndView listaManutencoes() {
        var view = new ModelAndView("listaManutencao");
        view.addObject("manutencoes", manutencaoService.getAllManutencoes());
        return view;
    }

    @GetMapping("/remover/{id}")
    public String removerManutencao(@PathVariable("id") int id) throws NotFoundException {
        manutencaoService.deleteManutencao(id);
        return "redirect:/manutencao/view/listar";
    }

    @GetMapping("/cadastrar")
    public ModelAndView cadastrarManutencao() {
        var view = new ModelAndView("cadastroManutencao");
        view.addObject("manutencao", new Manutencao(0, null, null, null));
        view.addObject("municoes", municaoService.getAllMunicoes());
        return view;
    }

    @PostMapping("/cadastrar")
    public ModelAndView saveManutencao(Manutencao manutencao) {
        Set<ConstraintViolation<Manutencao>> violations = validator.validate(manutencao);
        String problemas = "";
        String mensagens = "";
        if (!violations.isEmpty()) {
            problemas = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(" - "));
        } else {
            manutencaoService.saveManutencao(manutencao);
            mensagens = "Salvo com sucesso!";
        }
        ModelAndView modelAndView = new ModelAndView("cadastroManutencao");
        modelAndView.addObject("sucesso", mensagens);
        modelAndView.addObject("error", problemas);
        return modelAndView;
    }

    @GetMapping("/atualizar/{id}")
    public ModelAndView formUpdate(@PathVariable("id") int id) throws NotFoundException {
        ModelAndView modelAndView = new ModelAndView("cadastroManutencao");
        Manutencao manutencao = manutencaoService.getManutencao(id);
        modelAndView.addObject("manutencao", manutencao);
        modelAndView.addObject("municoes", municaoService.getAllMunicoes());
        return modelAndView;
    }
    
    @GetMapping("/buscar")
    public ModelAndView buscarManutencoes(@RequestParam(name = "busca", required = false) String busca) {
        ModelAndView modelAndView = new ModelAndView("listaManutencao");
        if (busca != null && !busca.isEmpty()) {
            try {
                LocalDate data = LocalDate.parse(busca); // Tente converter a string em LocalDate
                List<Manutencao> manutencoes = manutencaoService.buscarPorDataManutencao(data);
                modelAndView.addObject("manutencoes", manutencoes);
            } catch (DateTimeParseException ex) {
                // Tratar erro de formatação da data, pode ser um status ou data não válida
                List<Manutencao> manutencoes = manutencaoService.buscarPorStatus(busca);
                modelAndView.addObject("manutencoes", manutencoes);
            }
        } else {
            modelAndView.addObject("manutencoes", manutencaoService.getAllManutencoes());
        }
        return modelAndView;
    }



}
