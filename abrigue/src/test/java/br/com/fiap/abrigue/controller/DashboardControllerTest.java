package br.com.fiap.abrigue.controller;

import br.com.fiap.abrigue.model.entity.Abrigo;
import br.com.fiap.abrigue.service.AbrigoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DashboardController.class)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AbrigoService abrigoService;

    @Test
    @WithMockUser
    void testDashboardComDados() throws Exception {
        Abrigo abrigo1 = new Abrigo();
        abrigo1.setCapacidadeMaxima(100);
        abrigo1.setVagasOcupadas(60);

        Abrigo abrigo2 = new Abrigo();
        abrigo2.setCapacidadeMaxima(50);
        abrigo2.setVagasOcupadas(20);

        when(abrigoService.listarTodos()).thenReturn(Arrays.asList(abrigo1, abrigo2));

        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/index"))
                .andExpect(model().attribute("totalAbrigos", 2))
                .andExpect(model().attribute("totalVagas", 150))
                .andExpect(model().attribute("totalOcupadas", 80))
                .andExpect(model().attribute("totalDisponiveis", 70));
    }

    @Test
    @WithMockUser
    void testDashboardSemDados() throws Exception {
        when(abrigoService.listarTodos()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/index"))
                .andExpect(model().attribute("totalAbrigos", 0))
                .andExpect(model().attribute("totalVagas", 0))
                .andExpect(model().attribute("totalOcupadas", 0))
                .andExpect(model().attribute("totalDisponiveis", 0));
    }
}
