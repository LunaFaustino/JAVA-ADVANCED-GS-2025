package br.com.fiap.abrigue.controller;

import br.com.fiap.abrigue.model.entity.Abrigo;
import br.com.fiap.abrigue.model.entity.Recurso;
import br.com.fiap.abrigue.model.enums.TipoRecurso;
import br.com.fiap.abrigue.service.AbrigoService;
import br.com.fiap.abrigue.service.RecursoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/recursos")
public class RecursoController {

    private final RecursoService recursoService;

    private final AbrigoService abrigoService;

    public RecursoController(RecursoService recursoService, AbrigoService abrigoService) {
        this.recursoService = recursoService;
        this.abrigoService = abrigoService;
    }

    @GetMapping
    public String listar(Model model,
                         @RequestParam(required = false) String busca,
                         @RequestParam(required = false) Long abrigoId,
                         @RequestParam(required = false) TipoRecurso tipo) {

        List<Recurso> recursos;

        if (abrigoId != null) {
            if (tipo != null) {
                recursos = recursoService.listarPorAbrigoETipo(abrigoId, tipo);
                model.addAttribute("tipoFiltro", tipo);
            } else {
                recursos = recursoService.listarPorAbrigo(abrigoId);
            }

            Optional<Abrigo> abrigo = abrigoService.buscarPorId(abrigoId);
            if (abrigo.isPresent()) {
                model.addAttribute("abrigo", abrigo.get());
            }
        } else if (busca != null && !busca.trim().isEmpty()) {
            recursos = recursoService.buscarPorNome(busca);
            model.addAttribute("busca", busca);
        } else if (tipo != null) {
            recursos = recursoService.listarPorTipo(tipo);
            model.addAttribute("tipoFiltro", tipo);
        } else {
            recursos = recursoService.listarTodos();
        }

        model.addAttribute("recursos", recursos);
        model.addAttribute("tiposRecurso", TipoRecurso.values());
        return "recurso/lista";
    }

    @GetMapping("/novo")
    public String novoForm(Model model, @RequestParam(required = false) Long abrigoId) {
        Recurso recurso = new Recurso();

        if (abrigoId != null) {
            Optional<Abrigo> abrigo = abrigoService.buscarPorId(abrigoId);
            if (abrigo.isPresent()) {
                recurso.setAbrigo(abrigo.get());
            }
        }

        model.addAttribute("recurso", recurso);
        model.addAttribute("tipoOptions", TipoRecurso.values());
        model.addAttribute("abrigos", abrigoService.listarAtivos());
        return "recurso/cadastro";
    }

    @PostMapping
    public String salvar(@Valid @ModelAttribute Recurso recurso,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("tipoOptions", TipoRecurso.values());
            model.addAttribute("abrigos", abrigoService.listarAtivos());
            return "recurso/cadastro";
        }

        try {
            recursoService.salvar(recurso);
            redirectAttributes.addFlashAttribute("sucesso", "Recurso salvo com sucesso!");

            if (recurso.getAbrigo() != null) {
                return "redirect:/recursos?abrigoId=" + recurso.getAbrigo().getId();
            }
            return "redirect:/recursos";

        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao salvar recurso: " + e.getMessage());
            model.addAttribute("tipoOptions", TipoRecurso.values());
            model.addAttribute("abrigos", abrigoService.listarAtivos());
            return "recurso/cadastro";
        }
    }

    @GetMapping("/{id}/editar")
    public String editarForm(@PathVariable Long id, Model model) {
        Optional<Recurso> recurso = recursoService.buscarPorId(id);

        if (recurso.isPresent()) {
            model.addAttribute("recurso", recurso.get());
            model.addAttribute("tipoOptions", TipoRecurso.values());
            model.addAttribute("abrigos", abrigoService.listarAtivos());
            return "recurso/cadastro";
        } else {
            return "redirect:/recursos";
        }
    }

    @PostMapping("/{id}/deletar")
    public String deletar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Recurso> recurso = recursoService.buscarPorId(id);
            Long abrigoId = null;

            if (recurso.isPresent() && recurso.get().getAbrigo() != null) {
                abrigoId = recurso.get().getAbrigo().getId();
            }

            recursoService.deletar(id);
            redirectAttributes.addFlashAttribute("sucesso", "Recurso excluído com sucesso!");

            if (abrigoId != null) {
                return "redirect:/recursos?abrigoId=" + abrigoId;
            }
            return "redirect:/recursos";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao excluir recurso: " + e.getMessage());
            return "redirect:/recursos";
        }
    }
}
