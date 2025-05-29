package br.com.fiap.abrigue.controller;

import br.com.fiap.abrigue.model.entity.Abrigo;
import br.com.fiap.abrigue.service.AbrigoService;
import br.com.fiap.abrigue.service.AssistenteAIService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/assistente")
public class AssistenteController {

    private final AssistenteAIService assistenteAIService;
    private final AbrigoService abrigoService;

    public AssistenteController(AssistenteAIService assistenteAIService, AbrigoService abrigoService) {
        this.assistenteAIService = assistenteAIService;
        this.abrigoService = abrigoService;
    }

    @GetMapping
    public String index(Model model) {
        List<Abrigo> abrigos = abrigoService.listarTodos();

        int totalCapacidade = abrigos.stream().mapToInt(Abrigo::getCapacidadeMaxima).sum();
        int totalOcupadas = abrigos.stream().mapToInt(Abrigo::getVagasOcupadas).sum();

        model.addAttribute("abrigos", abrigos);
        model.addAttribute("totalCapacidade", totalCapacidade);
        model.addAttribute("totalOcupadas", totalOcupadas);

        return "assistente/index";
    }

    @PostMapping("/analisar-abrigo")
    public String analisarAbrigo(@RequestParam Long abrigoId,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        try {
            Optional<Abrigo> abrigo = abrigoService.buscarPorId(abrigoId);
            if (abrigo.isEmpty()) {
                redirectAttributes.addFlashAttribute("erro", "Abrigo não encontrado.");
                return "redirect:/assistente";
            }

            String analise = assistenteAIService.analisarAbrigo(abrigoId);

            model.addAttribute("analise", analise);
            model.addAttribute("abrigo", abrigo.get());
            model.addAttribute("tipoAnalise", "individual");

            return "assistente/resultado";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao processar análise: " + e.getMessage());
            return "redirect:/assistente";
        }
    }

    @PostMapping("/analisar-geral")
    public String analisarGeral(Model model, RedirectAttributes redirectAttributes) {
        try {
            String analise = assistenteAIService.analisarTodosAbrigos();

            model.addAttribute("analise", analise);
            model.addAttribute("tipoAnalise", "geral");

            return "assistente/resultado";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao processar análise geral: " + e.getMessage());
            return "redirect:/assistente";
        }
    }
}