package br.com.fiap.abrigue.controller;

import br.com.fiap.abrigue.model.entity.Abrigo;
import br.com.fiap.abrigue.model.enums.StatusAbrigo;
import br.com.fiap.abrigue.service.AbrigoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AbrigoController.class)
class AbrigoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AbrigoService abrigoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void testListarAbrigos() throws Exception {
        Abrigo abrigo = new Abrigo();
        abrigo.setId(1L);
        abrigo.setNome("Abrigo Teste");
        abrigo.setEndereco("Rua Teste, 123");
        abrigo.setCapacidadeMaxima(100);
        abrigo.setVagasOcupadas(50);
        abrigo.setStatus(StatusAbrigo.ATIVO);

        when(abrigoService.listarTodos()).thenReturn(Arrays.asList(abrigo));

        mockMvc.perform(get("/abrigos"))
                .andExpect(status().isOk())
                .andExpect(view().name("abrigo/lista"))
                .andExpect(model().attributeExists("abrigos"));

        verify(abrigoService).listarTodos();
    }

    @Test
    @WithMockUser
    void testNovoAbrigoForm() throws Exception {
        mockMvc.perform(get("/abrigos/novo"))
                .andExpect(status().isOk())
                .andExpect(view().name("abrigo/cadastro"))
                .andExpect(model().attributeExists("abrigo"))
                .andExpect(model().attributeExists("statusOptions"));
    }

    @Test
    @WithMockUser
    void testSalvarAbrigoValido() throws Exception {
        Abrigo abrigo = new Abrigo();
        abrigo.setNome("Novo Abrigo");
        abrigo.setEndereco("Rua Nova, 456");
        abrigo.setCapacidadeMaxima(50);
        abrigo.setStatus(StatusAbrigo.ATIVO);

        when(abrigoService.salvar(any(Abrigo.class))).thenReturn(abrigo);

        mockMvc.perform(post("/abrigos")
                        .with(csrf())
                        .param("nome", "Novo Abrigo")
                        .param("endereco", "Rua Nova, 456")
                        .param("capacidadeMaxima", "50")
                        .param("status", "ATIVO"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/abrigos"));

        verify(abrigoService).salvar(any(Abrigo.class));
    }

    @Test
    @WithMockUser
    void testDetalhesAbrigo() throws Exception {
        Abrigo abrigo = new Abrigo();
        abrigo.setId(1L);
        abrigo.setNome("Abrigo Detalhes");
        abrigo.setCapacidadeMaxima(100);
        abrigo.setVagasOcupadas(40);

        when(abrigoService.buscarPorId(1L)).thenReturn(Optional.of(abrigo));
        when(abrigoService.calcularVagasDisponiveis(abrigo)).thenReturn(60);
        when(abrigoService.calcularPercentualOcupacao(abrigo)).thenReturn(40.0);

        mockMvc.perform(get("/abrigos/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("abrigo/detalhes"))
                .andExpect(model().attributeExists("abrigo"))
                .andExpect(model().attributeExists("vagasDisponiveis"))
                .andExpect(model().attributeExists("percentualOcupacao"));
    }

    @Test
    @WithMockUser
    void testSalvarAbrigoComErrosValidacao() throws Exception {
        mockMvc.perform(post("/abrigos")
                        .with(csrf())
                        .param("nome", "") // Nome vazio - inválido
                        .param("endereco", "Rua Nova, 456")
                        .param("capacidadeMaxima", "50")
                        .param("status", "ATIVO"))
                .andExpect(status().isOk())
                .andExpect(view().name("abrigo/cadastro"))
                .andExpect(model().hasErrors());

        verify(abrigoService, never()).salvar(any(Abrigo.class));
    }
}
