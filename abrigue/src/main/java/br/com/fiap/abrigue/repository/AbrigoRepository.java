package br.com.fiap.abrigue.repository;

import br.com.fiap.abrigue.model.entity.Abrigo;
import br.com.fiap.abrigue.model.enums.StatusAbrigo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AbrigoRepository extends JpaRepository<Abrigo, Long> {

    List<Abrigo> findByStatus(StatusAbrigo status);

    List<Abrigo> findByNomeContainingIgnoreCase(String nome);

    @Query("SELECT a FROM Abrigo a WHERE a.capacidadeMaxima > a.vagasOcupadas")
    List<Abrigo> findAbrigosComVagas();

    @Query("SELECT COUNT(p) FROM Pessoa p WHERE p.abrigo.id = ?1")
    Long contarPessoasPorAbrigo(Long abrigoId);
}
