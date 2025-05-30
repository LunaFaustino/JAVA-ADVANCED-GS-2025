package br.com.fiap.abrigue.service;

import br.com.fiap.abrigue.model.entity.Recurso;
import br.com.fiap.abrigue.model.enums.TipoRecurso;
import br.com.fiap.abrigue.repository.RecursoRepository;
import br.com.fiap.abrigue.service.impl.RecursoServiceImpl;
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
class RecursoServiceTest {

    @Mock
    private RecursoRepository recursoRepository;

    @Mock
    private MessagePublisherService messagePublisherService;

    @InjectMocks
    private RecursoServiceImpl recursoService;

    private Recurso recurso;

    @BeforeEach
    void setUp() {
        recurso = new Recurso();
        recurso.setId(1L);
        recurso.setNome("Água");
        recurso.setQuantidade(15);
        recurso.setTipo(TipoRecurso.ALIMENTO);
    }

    @Test
    void testIsEstoqueBaixoRetornaTrue() {
        recurso.setQuantidade(5);

        boolean resultado = recursoService.isEstoqueBaixo(recurso);

        assertTrue(resultado);
    }

    @Test
    void testIsEstoqueBaixoRetornaFalse() {
        recurso.setQuantidade(20);

        boolean resultado = recursoService.isEstoqueBaixo(recurso);

        assertFalse(resultado);
    }

    @Test
    void testAdicionarQuantidade() {
        when(recursoRepository.findById(1L)).thenReturn(Optional.of(recurso));
        when(recursoRepository.save(any(Recurso.class))).thenReturn(recurso);

        Recurso resultado = recursoService.adicionarQuantidade(1L, 10);

        assertEquals(25, resultado.getQuantidade());
        verify(recursoRepository).save(recurso);
    }

    @Test
    void testRemoverQuantidade() {
        when(recursoRepository.findById(1L)).thenReturn(Optional.of(recurso));
        when(recursoRepository.save(any(Recurso.class))).thenReturn(recurso);

        Recurso resultado = recursoService.removerQuantidade(1L, 5);

        assertEquals(10, resultado.getQuantidade());
        verify(recursoRepository).save(recurso);
    }

    @Test
    void testRemoverQuantidadeInsuficiente() {
        when(recursoRepository.findById(1L)).thenReturn(Optional.of(recurso));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> recursoService.removerQuantidade(1L, 20));

        assertTrue(exception.getMessage().contains("Quantidade insuficiente"));
    }

    @Test
    void testSalvarRecurso() {
        when(recursoRepository.save(any(Recurso.class))).thenReturn(recurso);

        Recurso resultado = recursoService.salvar(recurso);

        assertNotNull(resultado);
        assertNotNull(resultado.getDataAtualizacao());
        verify(recursoRepository).save(recurso);
    }
}
