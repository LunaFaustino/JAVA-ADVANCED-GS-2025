package br.com.fiap.abrigue.controller;

import br.com.fiap.abrigue.model.entity.Abrigo;
import br.com.fiap.abrigue.model.entity.Pessoa;
import br.com.fiap.abrigue.model.enums.SituacaoEspecial;
import br.com.fiap.abrigue.service.AbrigoService;
import br.com.fiap.abrigue.service.PessoaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/pessoas")
public class PessoaController {

    private final PessoaService pessoaService;

    private final AbrigoService abrigoService;

    public PessoaController(PessoaService pessoaService, AbrigoService abrigoService) {
        this.pessoaService = pessoaService;
        this.abrigoService = abrigoService;
    }

    @GetMapping
    public String listar(Model model,
                         @RequestParam(required = false) String busca,
                         @RequestParam(required = false) Long abrigoId) {

        List<Pessoa> pessoas;

        if (abrigoId != null) {
            pessoas = pessoaService.listarPorAbrigo(abrigoId);
            Optional<Abrigo> abrigo = abrigoService.buscarPorId(abrigoId);
            if (abrigo.isPresent()) {
                model.addAttribute("abrigo", abrigo.get());
            }
        } else if (busca != null && !busca.trim().isEmpty()) {
            pessoas = pessoaService.buscarPorNome(busca);
            model.addAttribute("busca", busca);
        } else {
            pessoas = pessoaService.listarTodas();
        }

        model.addAttribute("pessoas", pessoas);
        return "pessoa/lista";
    }

    @GetMapping("/novo")
    public String novoForm(Model model, @RequestParam(required = false) Long abrigoId) {
        Pessoa pessoa = new Pessoa();

        if (abrigoId != null) {
            Optional<Abrigo> abrigo = abrigoService.buscarPorId(abrigoId);
            if (abrigo.isPresent()) {
                pessoa.setAbrigo(abrigo.get());
            }
        }

        model.addAttribute("pessoa", pessoa);
        model.addAttribute("situacaoOptions", SituacaoEspecial.values());
        model.addAttribute("abrigos", abrigoService.listarAtivos());
        return "pessoa/cadastro";
    }

    @PostMapping
    public String salvar(@Valid @ModelAttribute Pessoa pessoa,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        if (pessoaService.cpfJaExiste(pessoa.getCpf(), pessoa.getId())) {
            result.rejectValue("cpf", "error.pessoa", "CPF já cadastrado no sistema");
        }

        if (result.hasErrors()) {
            model.addAttribute("situacaoOptions", SituacaoEspecial.values());
            model.addAttribute("abrigos", abrigoService.listarAtivos());
            return "pessoa/cadastro";
        }

        try {
            pessoaService.salvar(pessoa);
            redirectAttributes.addFlashAttribute("sucesso", "Pessoa cadastrada com sucesso!");

            if (pessoa.getAbrigo() != null) {
                return "redirect:/pessoas?abrigoId=" + pessoa.getAbrigo().getId();
            }
            return "redirect:/pessoas";

        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao salvar pessoa: " + e.getMessage());
            model.addAttribute("situacaoOptions", SituacaoEspecial.values());
            model.addAttribute("abrigos", abrigoService.listarAtivos());
            return "pessoa/cadastro";
        }
    }

    @GetMapping("/{id}/editar")
    public String editarForm(@PathVariable Long id, Model model) {
        Optional<Pessoa> pessoa = pessoaService.buscarPorId(id);

        if (pessoa.isPresent()) {
            model.addAttribute("pessoa", pessoa.get());
            model.addAttribute("situacaoOptions", SituacaoEspecial.values());
            model.addAttribute("abrigos", abrigoService.listarAtivos());
            return "pessoa/cadastro";
        } else {
            return "redirect:/pessoas";
        }
    }

    @PostMapping("/{id}/deletar")
    public String deletar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Pessoa> pessoa = pessoaService.buscarPorId(id);
            Long abrigoId = null;

            if (pessoa.isPresent() && pessoa.get().getAbrigo() != null) {
                abrigoId = pessoa.get().getAbrigo().getId();
            }

            pessoaService.deletar(id);
            redirectAttributes.addFlashAttribute("sucesso", "Pessoa removida com sucesso!");

            if (abrigoId != null) {
                return "redirect:/pessoas?abrigoId=" + abrigoId;
            }
            return "redirect:/pessoas";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao remover pessoa: " + e.getMessage());
            return "redirect:/pessoas";
        }
    }
}
