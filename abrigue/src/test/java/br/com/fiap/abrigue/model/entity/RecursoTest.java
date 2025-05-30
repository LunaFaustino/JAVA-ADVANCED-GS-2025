package br.com.fiap.abrigue.model.entity;

import br.com.fiap.abrigue.model.enums.TipoRecurso;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RecursoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testRecursoValido() {
        Recurso recurso = new Recurso();
        recurso.setNome("Água Mineral");
        recurso.setQuantidade(50);
        recurso.setTipo(TipoRecurso.ALIMENTO);
        recurso.setDescricao("Garrafas de 500ml");

        Set<ConstraintViolation<Recurso>> violations = validator.validate(recurso);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testQuantidadeNegativa() {
        Recurso recurso = new Recurso();
        recurso.setNome("Água Mineral");
        recurso.setQuantidade(-10); // Inválido
        recurso.setTipo(TipoRecurso.ALIMENTO);

        Set<ConstraintViolation<Recurso>> violations = validator.validate(recurso);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Quantidade deve ser positiva")));
    }

    @Test
    void testNomeObrigatorio() {
        Recurso recurso = new Recurso();
        recurso.setQuantidade(50);
        recurso.setTipo(TipoRecurso.ALIMENTO);

        Set<ConstraintViolation<Recurso>> violations = validator.validate(recurso);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Nome do recurso é obrigatório")));
    }
}
