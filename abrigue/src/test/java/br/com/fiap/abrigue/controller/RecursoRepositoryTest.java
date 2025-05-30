package br.com.fiap.abrigue.controller;

import br.com.fiap.abrigue.model.entity.Abrigo;
import br.com.fiap.abrigue.model.entity.Recurso;
import br.com.fiap.abrigue.model.enums.StatusAbrigo;
import br.com.fiap.abrigue.model.enums.TipoRecurso;
import br.com.fiap.abrigue.repository.RecursoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RecursoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RecursoRepository recursoRepository;

    @Test
    void testFindRecursosComEstoqueBaixo() {
        // Given
        Abrigo abrigo = new Abrigo();
        abrigo.setNome("Abrigo Teste");
        abrigo.setEndereco("Rua Teste, 123");
        abrigo.setCapacidadeMaxima(50);
        abrigo.setVagasOcupadas(20);
        abrigo.setStatus(StatusAbrigo.ATIVO);
        entityManager.persistAndFlush(abrigo);

        Recurso recursoEstoqueBaixo = new Recurso();
        recursoEstoqueBaixo.setNome("Água");
        recursoEstoqueBaixo.setQuantidade(5); // Estoque baixo
        recursoEstoqueBaixo.setTipo(TipoRecurso.ALIMENTO);
        recursoEstoqueBaixo.setAbrigo(abrigo);

        Recurso recursoEstoqueNormal = new Recurso();
        recursoEstoqueNormal.setNome("Arroz");
        recursoEstoqueNormal.setQuantidade(50); // Estoque normal
        recursoEstoqueNormal.setTipo(TipoRecurso.ALIMENTO);
        recursoEstoqueNormal.setAbrigo(abrigo);

        entityManager.persistAndFlush(recursoEstoqueBaixo);
        entityManager.persistAndFlush(recursoEstoqueNormal);

        List<Recurso> resultado = recursoRepository.findRecursosComEstoqueBaixo();

        assertEquals(1, resultado.size());
        assertEquals("Água", resultado.get(0).getNome());
        assertEquals(5, resultado.get(0).getQuantidade());
    }

    @Test
    void testFindByTipo() {
        Abrigo abrigo = new Abrigo();
        abrigo.setNome("Abrigo Teste");
        abrigo.setEndereco("Rua Teste, 123");
        abrigo.setCapacidadeMaxima(50);
        abrigo.setVagasOcupadas(20);
        abrigo.setStatus(StatusAbrigo.ATIVO);
        entityManager.persistAndFlush(abrigo);

        Recurso medicamento = new Recurso();
        medicamento.setNome("Paracetamol");
        medicamento.setQuantidade(20);
        medicamento.setTipo(TipoRecurso.MEDICAMENTO);
        medicamento.setAbrigo(abrigo);

        Recurso alimento = new Recurso();
        alimento.setNome("Feijão");
        alimento.setQuantidade(30);
        alimento.setTipo(TipoRecurso.ALIMENTO);
        alimento.setAbrigo(abrigo);

        entityManager.persistAndFlush(medicamento);
        entityManager.persistAndFlush(alimento);

        List<Recurso> medicamentos = recursoRepository.findByTipo(TipoRecurso.MEDICAMENTO);

        assertEquals(1, medicamentos.size());
        assertEquals("Paracetamol", medicamentos.get(0).getNome());
    }

    @Test
    void testFindByAbrigoIdAndTipo() {
        Abrigo abrigo = new Abrigo();
        abrigo.setNome("Abrigo Teste");
        abrigo.setEndereco("Rua Teste, 123");
        abrigo.setCapacidadeMaxima(50);
        abrigo.setVagasOcupadas(20);
        abrigo.setStatus(StatusAbrigo.ATIVO);
        entityManager.persistAndFlush(abrigo);

        Recurso higiene1 = new Recurso();
        higiene1.setNome("Sabonete");
        higiene1.setQuantidade(15);
        higiene1.setTipo(TipoRecurso.HIGIENE);
        higiene1.setAbrigo(abrigo);

        Recurso higiene2 = new Recurso();
        higiene2.setNome("Shampoo");
        higiene2.setQuantidade(10);
        higiene2.setTipo(TipoRecurso.HIGIENE);
        higiene2.setAbrigo(abrigo);

        entityManager.persistAndFlush(higiene1);
        entityManager.persistAndFlush(higiene2);

        List<Recurso> resultado = recursoRepository.findByAbrigoIdAndTipo(abrigo.getId(), TipoRecurso.HIGIENE);

        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(r -> r.getTipo() == TipoRecurso.HIGIENE));
        assertTrue(resultado.stream().allMatch(r -> r.getAbrigo().getId().equals(abrigo.getId())));
    }
}
