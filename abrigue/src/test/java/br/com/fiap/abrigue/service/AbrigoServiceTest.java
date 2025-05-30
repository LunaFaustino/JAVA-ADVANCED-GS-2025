package br.com.fiap.abrigue.service;

import br.com.fiap.abrigue.model.entity.Abrigo;
import br.com.fiap.abrigue.model.enums.StatusAbrigo;
import br.com.fiap.abrigue.repository.AbrigoRepository;
import br.com.fiap.abrigue.service.impl.AbrigoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AbrigoServiceTest {

    @Mock
    private AbrigoRepository abrigoRepository;

    @Mock
    private MessagePublisherService messagePublisherService;

    @InjectMocks
    private AbrigoServiceImpl abrigoService;

    private Abrigo abrigo;

    @BeforeEach
    void setUp() {
        abrigo = new Abrigo();
        abrigo.setId(1L);
        abrigo.setNome("Abrigo Teste");
        abrigo.setEndereco("Rua Teste, 123");
        abrigo.setCapacidadeMaxima(100);
        abrigo.setVagasOcupadas(50);
        abrigo.setStatus(StatusAbrigo.ATIVO);
    }

    @Test
    void testCalcularVagasDisponiveis() {
        int vagasDisponiveis = abrigoService.calcularVagasDisponiveis(abrigo);

        assertEquals(50, vagasDisponiveis);
    }

    @Test
    void testCalcularPercentualOcupacao() {
        double percentual = abrigoService.calcularPercentualOcupacao(abrigo);

        assertEquals(50.0, percentual, 0.01);
    }

    @Test
    void testCalcularPercentualOcupacaoCapacidadeZero() {
        abrigo.setCapacidadeMaxima(0);
        abrigo.setVagasOcupadas(0);

        double percentual = abrigoService.calcularPercentualOcupacao(abrigo);

        assertEquals(0.0, percentual);
    }

    @Test
    void testSalvarAbrigoLotado() {
        // Given
        abrigo.setVagasOcupadas(100); // Igual à capacidade máxima
        when(abrigoRepository.save(any(Abrigo.class))).thenReturn(abrigo);

        Abrigo resultado = abrigoService.salvar(abrigo);

        assertEquals(StatusAbrigo.LOTADO, resultado.getStatus());
        verify(abrigoRepository).save(abrigo);
    }

    @Test
    void testListarTodos() {
        List<Abrigo> abrigos = Arrays.asList(abrigo);
        when(abrigoRepository.findAll()).thenReturn(abrigos);

        List<Abrigo> resultado = abrigoService.listarTodos();

        assertEquals(1, resultado.size());
        assertEquals(abrigo.getNome(), resultado.get(0).getNome());
        verify(abrigoRepository).findAll();
    }

    @Test
    void testBuscarPorId() {
        when(abrigoRepository.findById(1L)).thenReturn(Optional.of(abrigo));

        Optional<Abrigo> resultado = abrigoService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(abrigo.getNome(), resultado.get().getNome());
        verify(abrigoRepository).findById(1L);
    }

    @Test
    void testDeletar() {
        abrigoService.deletar(1L);

        verify(abrigoRepository).deleteById(1L);
    }
}
