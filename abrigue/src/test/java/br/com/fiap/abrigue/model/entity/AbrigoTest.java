package br.com.fiap.abrigue.model.entity;

import br.com.fiap.abrigue.model.enums.StatusAbrigo;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AbrigoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testAbrigoValido() {
        Abrigo abrigo = new Abrigo();
        abrigo.setNome("Abrigo Válido");
        abrigo.setEndereco("Rua Válida, 123");
        abrigo.setCapacidadeMaxima(50);
        abrigo.setVagasOcupadas(20);
        abrigo.setStatus(StatusAbrigo.ATIVO);

        Set<ConstraintViolation<Abrigo>> violations = validator.validate(abrigo);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testAbrigoNomeObrigatorio() {
        Abrigo abrigo = new Abrigo();
        abrigo.setEndereco("Rua Válida, 123");
        abrigo.setCapacidadeMaxima(50);

        Set<ConstraintViolation<Abrigo>> violations = validator.validate(abrigo);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Nome é obrigatório")));
    }

    @Test
    void testAbrigoCapacidadeMinima() {
        Abrigo abrigo = new Abrigo();
        abrigo.setNome("Abrigo Teste");
        abrigo.setEndereco("Rua Válida, 123");
        abrigo.setCapacidadeMaxima(0); // Inválido

        Set<ConstraintViolation<Abrigo>> violations = validator.validate(abrigo);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Capacidade deve ser maior que 0")));
    }
}
