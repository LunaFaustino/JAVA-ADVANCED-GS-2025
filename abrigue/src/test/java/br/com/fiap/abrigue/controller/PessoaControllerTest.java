package br.com.fiap.abrigue.controller;

import br.com.fiap.abrigue.model.entity.Abrigo;
import br.com.fiap.abrigue.model.entity.Pessoa;
import br.com.fiap.abrigue.model.enums.StatusAbrigo;
import br.com.fiap.abrigue.service.AbrigoService;
import br.com.fiap.abrigue.service.PessoaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PessoaController.class)
class PessoaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PessoaService pessoaService;

    @MockitoBean
    private AbrigoService abrigoService;

    @Test
    @WithMockUser
    void testListarPessoas() throws Exception {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1L);
        pessoa.setNome("João Silva");
        pessoa.setCpf("123.456.789-00");
        pessoa.setIdade(30);

        when(pessoaService.listarTodas()).thenReturn(Arrays.asList(pessoa));

        mockMvc.perform(get("/pessoas"))
                .andExpect(status().isOk())
                .andExpect(view().name("pessoa/lista"))
                .andExpect(model().attributeExists("pessoas"));

        verify(pessoaService).listarTodas();
    }

    @Test
    @WithMockUser
    void testNovaPessoaForm() throws Exception {
        Abrigo abrigo = new Abrigo();
        abrigo.setId(1L);
        abrigo.setNome("Abrigo Teste");
        abrigo.setStatus(StatusAbrigo.ATIVO);

        when(abrigoService.listarAtivos()).thenReturn(Arrays.asList(abrigo));

        mockMvc.perform(get("/pessoas/novo"))
                .andExpect(status().isOk())
                .andExpect(view().name("pessoa/cadastro"))
                .andExpect(model().attributeExists("pessoa"))
                .andExpect(model().attributeExists("situacaoOptions"))
                .andExpect(model().attributeExists("abrigos"));
    }

    @Test
    @WithMockUser
    void testSalvarPessoaValida() throws Exception {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("Maria Silva");
        pessoa.setCpf("987.654.321-00");
        pessoa.setIdade(25);

        when(pessoaService.cpfJaExiste(anyString(), any())).thenReturn(false);
        when(pessoaService.salvar(any(Pessoa.class))).thenReturn(pessoa);
        when(abrigoService.listarAtivos()).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/pessoas")
                        .with(csrf())
                        .param("nome", "Maria Silva")
                        .param("cpf", "987.654.321-00")
                        .param("idade", "25")
                        .param("situacaoEspecial", "NENHUMA"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pessoas"));

        verify(pessoaService).salvar(any(Pessoa.class));
    }

    @Test
    @WithMockUser
    void testSalvarPessoaCpfDuplicado() throws Exception {
        when(pessoaService.cpfJaExiste("123.456.789-00", null)).thenReturn(true);
        when(abrigoService.listarAtivos()).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/pessoas")
                        .with(csrf())
                        .param("nome", "João Silva")
                        .param("cpf", "123.456.789-00")
                        .param("idade", "30")
                        .param("situacaoEspecial", "NENHUMA"))
                .andExpect(status().isOk())
                .andExpect(view().name("pessoa/cadastro"))
                .andExpect(model().hasErrors());

        verify(pessoaService, never()).salvar(any(Pessoa.class));
    }
}