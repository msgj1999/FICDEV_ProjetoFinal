package com.example.demo.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
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
import org.springframework.data.domain.Sort;

import com.example.demo.entities.Entrega;
import com.example.demo.service.EntregaService;
import com.example.demo.service.MunicaoService;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/entrega/view")
public class EntregaControllerView {

    @Autowired
    EntregaService entregaService;

    @Autowired
    MunicaoService municaoService;



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
        ModelAndView view = new ModelAndView("cadastroEntrega");
        view.addObject("entrega", new Entrega());
        view.addObject("municoes", municaoService.getAllMunicoes());
        return view;
    }

    @PostMapping("/cadastrar")
    public ModelAndView saveEntrega(@Valid @ModelAttribute("entrega") Entrega entrega, BindingResult result, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView("cadastroEntrega");

        if (result.hasErrors()) {
            modelAndView.addObject("error", "Erro na validação. Por favor, verifique os campos.");
            modelAndView.addObject("municoes", municaoService.getAllMunicoes());
            return modelAndView;
        }

        try {
            entregaService.cadastrarEntrega(entrega);
            redirectAttributes.addFlashAttribute("sucesso", "Entrega cadastrada com sucesso!");
            return new ModelAndView("redirect:/entrega/view/listar");
        } catch (NotFoundException e) {
            modelAndView.addObject("error", e.getMessage());
            modelAndView.addObject("municoes", municaoService.getAllMunicoes());
            return modelAndView;
        }
    }


    @GetMapping("/atualizar/{id}")
    public ModelAndView formUpdate(@PathVariable("id") int id, RedirectAttributes redirectAttributes) throws NotFoundException {
        ModelAndView modelAndView = new ModelAndView("editarEntrega");
        Entrega entrega = entregaService.getEntrega(id);
        modelAndView.addObject("entrega", entrega);
        modelAndView.addObject("municoes", municaoService.getAllMunicoes());
        return modelAndView;
    }

    @PostMapping("/atualizar/{id}")
    public ModelAndView atualizarEntrega(@PathVariable("id") int id, @Valid @ModelAttribute("entrega") Entrega entrega, BindingResult result, RedirectAttributes redirectAttributes) throws NotFoundException {
        ModelAndView modelAndView = new ModelAndView("editarEntrega");

        if (result.hasErrors()) {
            modelAndView.addObject("error", "Erro na validação. Por favor, verifique os campos.");
            modelAndView.addObject("municoes", municaoService.getAllMunicoes());
            return modelAndView;
        }

        try {
            entregaService.updateEntrega(entrega, id);
            redirectAttributes.addFlashAttribute("sucesso", "Entrega atualizada com sucesso!");
            return new ModelAndView("redirect:/entrega/view/listar");
        } catch (NotFoundException e) {
            modelAndView.addObject("error", e.getMessage());
            modelAndView.addObject("municoes", municaoService.getAllMunicoes());
            return modelAndView;
        }
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
