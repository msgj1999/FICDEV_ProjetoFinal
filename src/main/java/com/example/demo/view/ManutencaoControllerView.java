package com.example.demo.view;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    


    @GetMapping("/listar")
    public ModelAndView listaManutencoes(
        @PageableDefault(size = 8, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        ModelAndView view = new ModelAndView("listaManutencao");
        Page<Manutencao> manutencoes = manutencaoService.getAllManutencoes(pageable);
        view.addObject("manutencoes", manutencoes);
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
    public ModelAndView saveManutencao(@Valid @ModelAttribute("manutencao") Manutencao manutencao, BindingResult result, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView("cadastroManutencao");

        if (result.hasErrors()) {
            modelAndView.addObject("error", "Erro na validação. Por favor, verifique os campos.");
            return modelAndView;
        }

        manutencaoService.saveManutencao(manutencao);
		redirectAttributes.addFlashAttribute("sucesso", "Manutenção cadastrada com sucesso!");
		return new ModelAndView("redirect:/manutencao/view/listar");
    }

    @GetMapping("/atualizar/{id}")
    public ModelAndView formUpdate(@PathVariable("id") int id) throws NotFoundException {
        ModelAndView modelAndView = new ModelAndView("editarManutencao");
        Manutencao manutencao = manutencaoService.getManutencao(id);
        modelAndView.addObject("manutencao", manutencao);
        modelAndView.addObject("municoes", municaoService.getAllMunicoes());
        return modelAndView;
    }
    
    @PostMapping("/atualizar/{id}")
    public String atualizarManutencao(@PathVariable("id") int id, Manutencao manutencao) throws NotFoundException {
        manutencaoService.updateManutencao(manutencao, id);
        return "redirect:/manutencao/view/listar";
    }


    
    @GetMapping("/buscar")
    public ModelAndView buscarManutencoes(
        @RequestParam(value = "termo", required = false) String termo,
        @PageableDefault(size = 8, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Manutencao> pageManutencoes = manutencaoService.buscarManutencoesPorFiltro(termo, pageable);
        ModelAndView view = new ModelAndView("listaManutencao");
        view.addObject("manutencoes", pageManutencoes);
        return view;
    }


}
