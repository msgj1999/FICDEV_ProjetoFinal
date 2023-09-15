package com.example.demo.view;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.entities.Municao;
import com.example.demo.service.MunicaoService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Controller
@RequestMapping("/municao/view")
public class MunicaoControllerView {

    @Autowired
    MunicaoService municaoService;
    
	@Autowired
	private Validator validator;

    @GetMapping("/listar")
    public ModelAndView listaMunicoes() {
        var view = new ModelAndView("listaMunicao");
        view.addObject("municoes", municaoService.getAllMunicoes());
        return view;
    }

    @GetMapping("/remover/{id}")
    public String removerMunicao(@PathVariable("id") int id) throws NotFoundException {
        municaoService.deleteMunicao(id);
        return "redirect:/municao/view/listar";
    }

    @GetMapping("/cadastrar")
    public ModelAndView cadastrarMunicao() {
        var view = new ModelAndView("cadastroMunicao");
        view.addObject("municao", new Municao());
        return view;
    }

    @PostMapping("/cadastrar")
    public ModelAndView saveMunicao(Municao municao) {
        Set<ConstraintViolation<Municao>> violations = validator.validate(municao);
        String problemas = "";
        String mensagens = "";
        if (!violations.isEmpty()) {
            problemas = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(" - "));
        } else {
            municaoService.saveMunicao(municao);
            mensagens = "Salvo com sucesso!";
        }
        ModelAndView modelAndView = new ModelAndView("cadastroMunicao");
        modelAndView.addObject("sucesso", mensagens);
        modelAndView.addObject("error", problemas);
        return modelAndView;
    }

    @GetMapping("/atualizar/{id}")
    public ModelAndView formUpdate(@PathVariable("id") int id) throws NotFoundException {
        ModelAndView modelAndView = new ModelAndView("cadastroMunicao");
        Municao municao = municaoService.getMunicao(id);
        modelAndView.addObject("municao", municao);
        return modelAndView;
    }
    
    @GetMapping("/buscar")
    public ModelAndView buscarMunicoes(@RequestParam(value = "termo", required = false) String termo) {
        ModelAndView view = new ModelAndView("listaMunicao");
        List<Municao> municoes = municaoService.buscarMunicoesPorFiltro(termo);
        view.addObject("municoes", municoes);
        return view;
    }
}
