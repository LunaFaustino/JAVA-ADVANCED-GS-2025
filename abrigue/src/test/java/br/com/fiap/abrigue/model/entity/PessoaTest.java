package br.com.fiap.abrigue.model.entity;

import br.com.fiap.abrigue.model.enums.SituacaoEspecial;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PessoaTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testPessoaValida() {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("João Silva");
        pessoa.setCpf("123.456.789-00");
        pessoa.setIdade(30);
        pessoa.setSituacaoEspecial(SituacaoEspecial.NENHUMA);

        Set<ConstraintViolation<Pessoa>> violations = validator.validate(pessoa);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testCpfInvalido() {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("João Silva");
        pessoa.setCpf("12345678900"); // Formato inválido
        pessoa.setIdade(30);

        Set<ConstraintViolation<Pessoa>> violations = validator.validate(pessoa);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("CPF deve estar no formato")));
    }

    @Test
    void testIdadeNegativa() {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("João Silva");
        pessoa.setCpf("123.456.789-00");
        pessoa.setIdade(-5); // Inválido

        Set<ConstraintViolation<Pessoa>> violations = validator.validate(pessoa);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Idade deve ser positiva")));
    }
}
