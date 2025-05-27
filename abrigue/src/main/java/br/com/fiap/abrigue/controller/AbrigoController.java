package br.com.fiap.abrigue.controller;

import br.com.fiap.abrigue.model.entity.Abrigo;
import br.com.fiap.abrigue.model.enums.StatusAbrigo;
import br.com.fiap.abrigue.service.AbrigoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/abrigos")
public class AbrigoController {

    @Autowired
    private AbrigoService abrigoService;

    @GetMapping
    public String listar(Model model, @RequestParam(required = false) String busca) {
        if (busca != null && !busca.trim().isEmpty()) {
            model.addAttribute("abrigos", abrigoService.buscarPorNome(busca));
            model.addAttribute("busca", busca);
        } else {
            model.addAttribute("abrigos", abrigoService.listarTodos());
        }
        return "abrigo/lista";
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("abrigo", new Abrigo());
        model.addAttribute("statusOptions", StatusAbrigo.values());
        return "abrigo/cadastro";
    }

    @PostMapping
    public String salvar(@Valid @ModelAttribute Abrigo abrigo,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("statusOptions", StatusAbrigo.values());
            return "abrigo/cadastro";
        }

        try {
            abrigoService.salvar(abrigo);
            redirectAttributes.addFlashAttribute("sucesso", "Abrigo salvo com sucesso!");
            return "redirect:/abrigos";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao salvar abrigo: " + e.getMessage());
            model.addAttribute("statusOptions", StatusAbrigo.values());
            return "abrigo/cadastro";
        }
    }

    @GetMapping("/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        Optional<Abrigo> abrigo = abrigoService.buscarPorId(id);

        if (abrigo.isPresent()) {
            model.addAttribute("abrigo", abrigo.get());
            model.addAttribute("vagasDisponiveis",
                    abrigoService.calcularVagasDisponiveis(abrigo.get()));
            model.addAttribute("percentualOcupacao",
                    abrigoService.calcularPercentualOcupacao(abrigo.get()));
            return "abrigo/detalhes";
        } else {
            return "redirect:/abrigos";
        }
    }

    @GetMapping("/{id}/editar")
    public String editarForm(@PathVariable Long id, Model model) {
        Optional<Abrigo> abrigo = abrigoService.buscarPorId(id);

        if (abrigo.isPresent()) {
            model.addAttribute("abrigo", abrigo.get());
            model.addAttribute("statusOptions", StatusAbrigo.values());
            return "abrigo/cadastro";
        } else {
            return "redirect:/abrigos";
        }
    }

    @PostMapping("/{id}/deletar")
    public String deletar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            abrigoService.deletar(id);
            redirectAttributes.addFlashAttribute("sucesso", "Abrigo excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao excluir abrigo: " + e.getMessage());
        }
        return "redirect:/abrigos";
    }
}
