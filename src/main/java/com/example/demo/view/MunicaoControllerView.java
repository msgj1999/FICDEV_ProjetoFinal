package com.example.demo.view;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
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

import com.example.demo.entities.Municao;
import com.example.demo.service.MunicaoService;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/municao/view")
public class MunicaoControllerView {

    @Autowired
    MunicaoService municaoService;
    

    @GetMapping("/listar")
    public ModelAndView listaMunicoes(
        @PageableDefault(size = 8, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        ModelAndView view = new ModelAndView("listaMunicao");
        Page<Municao> municoes = municaoService.getAllMunicoes(pageable);
        view.addObject("municoes", municoes);
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
    public ModelAndView saveMunicao(@Valid @ModelAttribute("municao") Municao municao, BindingResult result, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView("cadastroMunicao");

        if (result.hasErrors()) {
            modelAndView.addObject("error", "Erro na validação. Por favor, verifique os campos.");
            return modelAndView;
        }

        municaoService.saveMunicao(municao);
		redirectAttributes.addFlashAttribute("sucesso", "Munição cadastrada com sucesso!");
		return new ModelAndView("redirect:/municao/view/listar");
    }

    @GetMapping("/atualizar/{id}")
    public ModelAndView formUpdate(@PathVariable("id") int id) throws NotFoundException {
        ModelAndView modelAndView = new ModelAndView("cadastroMunicao");
        Municao municao = municaoService.getMunicao(id);
        modelAndView.addObject("municao", municao);
        return modelAndView;
    }
    
    @GetMapping("/buscar")
    public ModelAndView buscarMunicoes(
        @RequestParam(value = "termo", required = false) String termo,
        @PageableDefault(size = 8, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Municao> pageMunicoes = municaoService.buscarMunicoesPorFiltro(termo, pageable);
        ModelAndView view = new ModelAndView("listaMunicao");
        view.addObject("municoes", pageMunicoes);
        return view;
    }
}
