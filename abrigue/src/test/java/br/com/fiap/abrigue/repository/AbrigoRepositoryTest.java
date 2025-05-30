package br.com.fiap.abrigue.repository;

import br.com.fiap.abrigue.model.entity.Abrigo;
import br.com.fiap.abrigue.model.enums.StatusAbrigo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class AbrigoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AbrigoRepository abrigoRepository;

    @Test
    void testFindByStatus() {
        Abrigo abrigo1 = new Abrigo();
        abrigo1.setNome("Abrigo Ativo");
        abrigo1.setEndereco("Rua A, 123");
        abrigo1.setCapacidadeMaxima(50);
        abrigo1.setVagasOcupadas(20);
        abrigo1.setStatus(StatusAbrigo.ATIVO);

        Abrigo abrigo2 = new Abrigo();
        abrigo2.setNome("Abrigo Inativo");
        abrigo2.setEndereco("Rua B, 456");
        abrigo2.setCapacidadeMaxima(30);
        abrigo2.setVagasOcupadas(0);
        abrigo2.setStatus(StatusAbrigo.INATIVO);

        entityManager.persistAndFlush(abrigo1);
        entityManager.persistAndFlush(abrigo2);

        List<Abrigo> abrigosAtivos = abrigoRepository.findByStatus(StatusAbrigo.ATIVO);

        assertEquals(1, abrigosAtivos.size());
        assertEquals("Abrigo Ativo", abrigosAtivos.get(0).getNome());
    }

    @Test
    void testFindByNomeContainingIgnoreCase() {
        Abrigo abrigo = new Abrigo();
        abrigo.setNome("Centro de Acolhimento");
        abrigo.setEndereco("Rua Central, 789");
        abrigo.setCapacidadeMaxima(100);
        abrigo.setVagasOcupadas(50);
        abrigo.setStatus(StatusAbrigo.ATIVO);

        entityManager.persistAndFlush(abrigo);

        List<Abrigo> resultado = abrigoRepository.findByNomeContainingIgnoreCase("centro");

        assertEquals(1, resultado.size());
        assertEquals("Centro de Acolhimento", resultado.get(0).getNome());
    }

    @Test
    void testFindAbrigosComVagas() {
        Abrigo abrigoComVagas = new Abrigo();
        abrigoComVagas.setNome("Abrigo com Vagas");
        abrigoComVagas.setEndereco("Rua X, 111");
        abrigoComVagas.setCapacidadeMaxima(50);
        abrigoComVagas.setVagasOcupadas(30);
        abrigoComVagas.setStatus(StatusAbrigo.ATIVO);

        Abrigo abrigoLotado = new Abrigo();
        abrigoLotado.setNome("Abrigo Lotado");
        abrigoLotado.setEndereco("Rua Y, 222");
        abrigoLotado.setCapacidadeMaxima(20);
        abrigoLotado.setVagasOcupadas(20);
        abrigoLotado.setStatus(StatusAbrigo.LOTADO);

        entityManager.persistAndFlush(abrigoComVagas);
        entityManager.persistAndFlush(abrigoLotado);

        List<Abrigo> resultado = abrigoRepository.findAbrigosComVagas();

        assertEquals(1, resultado.size());
        assertEquals("Abrigo com Vagas", resultado.get(0).getNome());
    }
}
