package br.com.fiap.abrigue.integration;

import br.com.fiap.abrigue.config.TestConfig;
import br.com.fiap.abrigue.model.entity.Abrigo;
import br.com.fiap.abrigue.model.enums.StatusAbrigo;
import br.com.fiap.abrigue.repository.AbrigoRepository;
import br.com.fiap.abrigue.service.AbrigoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
@Transactional
class AbrigoIntegrationTest {

    @Autowired
    private AbrigoService abrigoService;

    @Autowired
    private AbrigoRepository abrigoRepository;

    @Test
    void testCicloCompletoCRUDAbrigo() {
        Abrigo abrigo = new Abrigo();
        abrigo.setNome("Abrigo Integração");
        abrigo.setEndereco("Rua Integração, 123");
        abrigo.setCapacidadeMaxima(100);
        abrigo.setVagasOcupadas(50);
        abrigo.setStatus(StatusAbrigo.ATIVO);

        Abrigo abrigoSalvo = abrigoService.salvar(abrigo);
        assertNotNull(abrigoSalvo.getId());
        assertEquals("Abrigo Integração", abrigoSalvo.getNome());

        Optional<Abrigo> abrigoEncontrado = abrigoService.buscarPorId(abrigoSalvo.getId());
        assertTrue(abrigoEncontrado.isPresent());
        assertEquals(abrigoSalvo.getNome(), abrigoEncontrado.get().getNome());

        abrigoSalvo.setNome("Abrigo Atualizado");
        abrigoSalvo.setVagasOcupadas(80);
        Abrigo abrigoAtualizado = abrigoService.salvar(abrigoSalvo);
        assertEquals("Abrigo Atualizado", abrigoAtualizado.getNome());
        assertEquals(80, abrigoAtualizado.getVagasOcupadas());

        List<Abrigo> abrigos = abrigoService.listarTodos();
        assertFalse(abrigos.isEmpty());
        assertTrue(abrigos.stream().anyMatch(a -> a.getNome().equals("Abrigo Atualizado")));

        abrigoService.deletar(abrigoSalvo.getId());
        Optional<Abrigo> abrigoDeletado = abrigoService.buscarPorId(abrigoSalvo.getId());
        assertFalse(abrigoDeletado.isPresent());
    }

    @Test
    void testBuscarPorNome() {
        Abrigo abrigo1 = new Abrigo();
        abrigo1.setNome("Casa de Apoio São José");
        abrigo1.setEndereco("Rua A, 123");
        abrigo1.setCapacidadeMaxima(50);
        abrigo1.setStatus(StatusAbrigo.ATIVO);

        Abrigo abrigo2 = new Abrigo();
        abrigo2.setNome("Abrigo Municipal");
        abrigo2.setEndereco("Rua B, 456");
        abrigo2.setCapacidadeMaxima(100);
        abrigo2.setStatus(StatusAbrigo.ATIVO);

        abrigoService.salvar(abrigo1);
        abrigoService.salvar(abrigo2);

        List<Abrigo> resultado = abrigoService.buscarPorNome("apoio");

        assertEquals(1, resultado.size());
        assertEquals("Casa de Apoio São José", resultado.get(0).getNome());
    }

    @Test
    void testCalculosDeOcupacao() {
        Abrigo abrigo = new Abrigo();
        abrigo.setNome("Abrigo para Teste de Cálculos");
        abrigo.setEndereco("Rua Cálculo, 789");
        abrigo.setCapacidadeMaxima(200);
        abrigo.setVagasOcupadas(120);
        abrigo.setStatus(StatusAbrigo.ATIVO);

        Abrigo abrigoSalvo = abrigoService.salvar(abrigo);

        int vagasDisponiveis = abrigoService.calcularVagasDisponiveis(abrigoSalvo);
        double percentualOcupacao = abrigoService.calcularPercentualOcupacao(abrigoSalvo);

        assertEquals(80, vagasDisponiveis);
        assertEquals(60.0, percentualOcupacao, 0.01);
    }

    @Test
    void testStatusLotadoAutomatico() {
        Abrigo abrigo = new Abrigo();
        abrigo.setNome("Abrigo que Ficará Lotado");
        abrigo.setEndereco("Rua Lotação, 999");
        abrigo.setCapacidadeMaxima(50);
        abrigo.setVagasOcupadas(50); // Lotado - 100% de ocupação
        abrigo.setStatus(StatusAbrigo.ATIVO);

        Abrigo abrigoSalvo = abrigoService.salvar(abrigo);

        assertEquals(StatusAbrigo.LOTADO, abrigoSalvo.getStatus());
    }

    @Test
    void testListarAbrigosComVagas() {
        Abrigo abrigoComVagas = new Abrigo();
        abrigoComVagas.setNome("Abrigo com Vagas");
        abrigoComVagas.setEndereco("Rua Disponível, 111");
        abrigoComVagas.setCapacidadeMaxima(50);
        abrigoComVagas.setVagasOcupadas(30);
        abrigoComVagas.setStatus(StatusAbrigo.ATIVO);

        Abrigo abrigoLotado = new Abrigo();
        abrigoLotado.setNome("Abrigo Lotado");
        abrigoLotado.setEndereco("Rua Cheia, 222");
        abrigoLotado.setCapacidadeMaxima(20);
        abrigoLotado.setVagasOcupadas(20);
        abrigoLotado.setStatus(StatusAbrigo.ATIVO);

        abrigoService.salvar(abrigoComVagas);
        abrigoService.salvar(abrigoLotado);

        List<Abrigo> abrigosComVagas = abrigoService.listarAbrigosComVagas();

        assertEquals(1, abrigosComVagas.size());
        assertEquals("Abrigo com Vagas", abrigosComVagas.get(0).getNome());
    }

    @Test
    void testListarAbrigosAtivos() {
        Abrigo abrigoAtivo = new Abrigo();
        abrigoAtivo.setNome("Abrigo Ativo");
        abrigoAtivo.setEndereco("Rua Ativa, 333");
        abrigoAtivo.setCapacidadeMaxima(40);
        abrigoAtivo.setVagasOcupadas(20);
        abrigoAtivo.setStatus(StatusAbrigo.ATIVO);

        Abrigo abrigoInativo = new Abrigo();
        abrigoInativo.setNome("Abrigo Inativo");
        abrigoInativo.setEndereco("Rua Inativa, 444");
        abrigoInativo.setCapacidadeMaxima(30);
        abrigoInativo.setVagasOcupadas(0);
        abrigoInativo.setStatus(StatusAbrigo.INATIVO);

        abrigoService.salvar(abrigoAtivo);
        abrigoService.salvar(abrigoInativo);

        List<Abrigo> abrigosAtivos = abrigoService.listarAtivos();

        assertEquals(1, abrigosAtivos.size());
        assertEquals("Abrigo Ativo", abrigosAtivos.get(0).getNome());
        assertEquals(StatusAbrigo.ATIVO, abrigosAtivos.get(0).getStatus());
    }
}