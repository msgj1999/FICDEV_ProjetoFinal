package com.example.demo.view;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.Expections.BusinessException;
import com.example.demo.entities.Municao;
import com.example.demo.service.MunicaoService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/municao/view")
public class MunicaoControllerView {

    @Autowired
    MunicaoService municaoService;
    

    @GetMapping("/listar")
    public ModelAndView listaMunicoes(
        @PageableDefault(size = 7, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        ModelAndView view = new ModelAndView("listaMunicao");
        Page<Municao> municoes = municaoService.getAllMunicoes(pageable);
        view.addObject("municoes", municoes);
        return view;
    }

    @GetMapping("/remover/{id}")
    public String removerMunicao(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            municaoService.deleteMunicao(id);
            redirectAttributes.addFlashAttribute("sucesso", "Munição excluída com sucesso!");
        } catch (EntityNotFoundException e) {
        } catch (BusinessException e) {
        	return "redirect:/municao/view/listar?error=rn1";
        } catch (NotFoundException e) {
        }
        
        return "redirect:/municao/view/listar?success=s2";
    }


    @GetMapping("/cadastrar")
    public ModelAndView cadastrarMunicao() {
        var view = new ModelAndView("cadastroMunicao");
        view.addObject("municao", new Municao());
        return view;
    }

    @PostMapping("/cadastrar")
    public ModelAndView saveMunicao(@Valid @ModelAttribute("municao") Municao municao, BindingResult result) {
        ModelAndView modelAndView = new ModelAndView("cadastroMunicao");

        if (result.hasErrors()) {
            modelAndView.addObject("error", "Erro na validação. Por favor, verifique os campos.");
            return modelAndView;
        }

        try {
            municaoService.saveMunicao(municao);

            return new ModelAndView("redirect:/municao/view/listar?success=s1");
            
        } catch (BusinessException e) {
            modelAndView.addObject("error", "Erro no cadastro: " + e.getMessage());
            return modelAndView;
        } catch (NotFoundException e) {
            return new ModelAndView("redirect:/municao/view/listar");
        }
    }

    @GetMapping("/atualizar/{id}")
    public ModelAndView formUpdate(@PathVariable("id") int id) throws NotFoundException {
        ModelAndView modelAndView = new ModelAndView("editarMunicao");
        Municao municao = municaoService.getMunicao(id);
        modelAndView.addObject("municao", municao);
        return modelAndView;
    }
    
    @PostMapping("/atualizar/{id}")
    public ModelAndView atualizarMunicao(@PathVariable("id") int id, @Valid Municao municao, BindingResult result, Model model) throws NotFoundException {
        if (result.hasErrors()) {
            model.addAttribute("error", "Erro na validação. Por favor, verifique os campos.");
            return new ModelAndView("editarMunicao");
        }

        municaoService.updateMunicao(municao, id);
        model.addAttribute("sucesso", "Munição atualizada com sucesso!");
        return new ModelAndView("redirect:/municao/view/listar?success=s3");
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
