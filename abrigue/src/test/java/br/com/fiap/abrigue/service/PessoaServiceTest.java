package br.com.fiap.abrigue.service;

import br.com.fiap.abrigue.model.entity.Pessoa;
import br.com.fiap.abrigue.model.enums.SituacaoEspecial;
import br.com.fiap.abrigue.repository.PessoaRepository;
import br.com.fiap.abrigue.service.impl.PessoaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PessoaServiceTest {

    @Mock
    private PessoaRepository pessoaRepository;

    @Mock
    private AbrigoService abrigoService;

    @Mock
    private MessagePublisherService messagePublisherService;

    @InjectMocks
    private PessoaServiceImpl pessoaService;

    private Pessoa pessoa;

    @BeforeEach
    void setUp() {
        pessoa = new Pessoa();
        pessoa.setId(1L);
        pessoa.setNome("João Silva");
        pessoa.setCpf("123.456.789-00");
        pessoa.setIdade(30);
        pessoa.setSituacaoEspecial(SituacaoEspecial.NENHUMA);
    }

    @Test
    void testCpfJaExisteRetornaTrue() {
        when(pessoaRepository.findByCpf("123.456.789-00")).thenReturn(Optional.of(pessoa));

        boolean resultado = pessoaService.cpfJaExiste("123.456.789-00", 2L);

        assertTrue(resultado);
        verify(pessoaRepository).findByCpf("123.456.789-00");
    }

    @Test
    void testCpfJaExisteRetornaFalseParaMesmaPessoa() {
        when(pessoaRepository.findByCpf("123.456.789-00")).thenReturn(Optional.of(pessoa));

        boolean resultado = pessoaService.cpfJaExiste("123.456.789-00", 1L);

        assertFalse(resultado);
    }

    @Test
    void testCpfJaExisteRetornaFalseParaCpfNaoExistente() {
        when(pessoaRepository.findByCpf("999.999.999-99")).thenReturn(Optional.empty());

        boolean resultado = pessoaService.cpfJaExiste("999.999.999-99", null);

        assertFalse(resultado);
    }

    @Test
    void testSalvarPessoa() {
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);

        Pessoa resultado = pessoaService.salvar(pessoa);

        assertNotNull(resultado);
        assertNotNull(resultado.getDataEntrada());
        assertEquals(SituacaoEspecial.NENHUMA, resultado.getSituacaoEspecial());
        verify(pessoaRepository).save(pessoa);
    }
}
