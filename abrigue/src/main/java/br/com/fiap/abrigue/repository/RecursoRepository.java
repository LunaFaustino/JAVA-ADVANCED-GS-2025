package br.com.fiap.abrigue.repository;

import br.com.fiap.abrigue.model.entity.Recurso;
import br.com.fiap.abrigue.model.enums.TipoRecurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecursoRepository extends JpaRepository<Recurso, Long> {

    List<Recurso> findByAbrigoId(Long abrigoId);

    List<Recurso> findByTipo(TipoRecurso tipo);

    List<Recurso> findByNomeContainingIgnoreCase(String nome);

    @Query("SELECT r FROM Recurso r WHERE r.quantidade <= 10")
    List<Recurso> findRecursosComEstoqueBaixo();

    @Query("SELECT r FROM Recurso r WHERE r.abrigo.id = ?1 AND r.tipo = ?2")
    List<Recurso> findByAbrigoIdAndTipo(Long abrigoId, TipoRecurso tipo);
}
