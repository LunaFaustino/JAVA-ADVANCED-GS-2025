package br.com.fiap.abrigue.controller;

import br.com.fiap.abrigue.model.entity.Abrigo;
import br.com.fiap.abrigue.service.AbrigoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private AbrigoService abrigoService;

    @GetMapping
    public String dashboard(Model model) {
        List<Abrigo> abrigos = abrigoService.listarTodos();

        int totalAbrigos = abrigos.size();
        int totalVagas = abrigos.stream().mapToInt(Abrigo::getCapacidadeMaxima).sum();
        int totalOcupadas = abrigos.stream().mapToInt(Abrigo::getVagasOcupadas).sum();
        int totalDisponiveis = totalVagas - totalOcupadas;

        model.addAttribute("totalAbrigos", totalAbrigos);
        model.addAttribute("totalVagas", totalVagas);
        model.addAttribute("totalOcupadas", totalOcupadas);
        model.addAttribute("totalDisponiveis", totalDisponiveis);
        model.addAttribute("abrigos", abrigos);

        return "dashboard/index";
    }
}
