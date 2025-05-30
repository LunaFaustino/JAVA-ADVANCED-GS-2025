package br.com.fiap.abrigue.repository;

import br.com.fiap.abrigue.model.entity.Abrigo;
import br.com.fiap.abrigue.model.entity.Pessoa;
import br.com.fiap.abrigue.model.enums.SituacaoEspecial;
import br.com.fiap.abrigue.model.enums.StatusAbrigo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class PessoaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Test
    void testFindByCpf() {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("Maria Silva");
        pessoa.setCpf("123.456.789-00");
        pessoa.setIdade(25);
        pessoa.setSituacaoEspecial(SituacaoEspecial.NENHUMA);

        entityManager.persistAndFlush(pessoa);

        Optional<Pessoa> resultado = pessoaRepository.findByCpf("123.456.789-00");

        assertTrue(resultado.isPresent());
        assertEquals("Maria Silva", resultado.get().getNome());
    }

    @Test
    void testFindByAbrigoId() {
        Abrigo abrigo = new Abrigo();
        abrigo.setNome("Abrigo Teste");
        abrigo.setEndereco("Rua Teste, 123");
        abrigo.setCapacidadeMaxima(50);
        abrigo.setVagasOcupadas(0);
        abrigo.setStatus(StatusAbrigo.ATIVO);
        entityManager.persistAndFlush(abrigo);

        Pessoa pessoa1 = new Pessoa();
        pessoa1.setNome("João");
        pessoa1.setCpf("111.111.111-11");
        pessoa1.setAbrigo(abrigo);
        pessoa1.setSituacaoEspecial(SituacaoEspecial.NENHUMA);

        Pessoa pessoa2 = new Pessoa();
        pessoa2.setNome("Pedro");
        pessoa2.setCpf("222.222.222-22");
        pessoa2.setAbrigo(abrigo);
        pessoa2.setSituacaoEspecial(SituacaoEspecial.IDOSO);

        entityManager.persistAndFlush(pessoa1);
        entityManager.persistAndFlush(pessoa2);

        List<Pessoa> resultado = pessoaRepository.findByAbrigoId(abrigo.getId());

        assertEquals(2, resultado.size());
    }

    @Test
    void testFindBySituacaoEspecial() {
        Pessoa idoso = new Pessoa();
        idoso.setNome("Sr. Antonio");
        idoso.setCpf("333.333.333-33");
        idoso.setIdade(70);
        idoso.setSituacaoEspecial(SituacaoEspecial.IDOSO);

        Pessoa crianca = new Pessoa();
        crianca.setNome("Ana");
        crianca.setCpf("444.444.444-44");
        crianca.setIdade(8);
        crianca.setSituacaoEspecial(SituacaoEspecial.CRIANCA);

        entityManager.persistAndFlush(idoso);
        entityManager.persistAndFlush(crianca);

        List<Pessoa> idosos = pessoaRepository.findBySituacaoEspecial(SituacaoEspecial.IDOSO);

        assertEquals(1, idosos.size());
        assertEquals("Sr. Antonio", idosos.get(0).getNome());
    }

    @Test
    void testContarPessoasPorAbrigo() {
        Abrigo abrigo = new Abrigo();
        abrigo.setNome("Abrigo Contagem");
        abrigo.setEndereco("Rua Contagem, 456");
        abrigo.setCapacidadeMaxima(30);
        abrigo.setVagasOcupadas(0);
        abrigo.setStatus(StatusAbrigo.ATIVO);
        entityManager.persistAndFlush(abrigo);

        for (int i = 1; i <= 3; i++) {
            Pessoa pessoa = new Pessoa();
            pessoa.setNome("Pessoa " + i);
            pessoa.setCpf(String.format("%03d.%03d.%03d-00", i, i, i));
            pessoa.setIdade(20 + i);
            pessoa.setSituacaoEspecial(SituacaoEspecial.NENHUMA);
            pessoa.setAbrigo(abrigo);
            entityManager.persistAndFlush(pessoa);
        }

        Long contagem = pessoaRepository.contarPessoasPorAbrigo(abrigo.getId());

        assertEquals(3L, contagem);
    }
}